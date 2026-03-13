package com.zettl.mathifier.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import android.app.Activity
import com.zettl.mathifier.AppContainer
import com.zettl.mathifier.R
import com.zettl.mathifier.data.datastore.LANGUAGE_DE
import com.zettl.mathifier.data.datastore.LANGUAGE_EN
import com.zettl.mathifier.data.datastore.LANGUAGE_SYSTEM
import com.zettl.mathifier.ui.theme.kidFriendlyButton
import com.zettl.mathifier.domain.model.MathOperation
import com.zettl.mathifier.domain.model.SessionConfig
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    container: AppContainer,
    currentProfileId: Long?,
    onBack: () -> Unit
) {
    var addEnabled by remember { mutableStateOf(true) }
    var subEnabled by remember { mutableStateOf(false) }
    var mulEnabled by remember { mutableStateOf(false) }
    var divEnabled by remember { mutableStateOf(false) }
    var minNumber by remember { mutableStateOf("1") }
    var maxNumber by remember { mutableStateOf("10") }
    var questionCount by remember { mutableStateOf("10") }
    var languageExpanded by remember { mutableStateOf(false) }
    var currentProfile by remember { mutableStateOf<com.zettl.mathifier.data.local.entity.StudentProfileEntity?>(null) }
    var profileToDelete by remember { mutableStateOf<com.zettl.mathifier.data.local.entity.StudentProfileEntity?>(null) }
    val scope = rememberCoroutineScope()
    val language by container.preferencesDataSource.language.collectAsState(initial = LANGUAGE_SYSTEM)
    val context = LocalContext.current
    val languageOptions = listOf(
        LANGUAGE_SYSTEM to R.string.settings_language_system,
        LANGUAGE_DE to R.string.settings_language_german,
        LANGUAGE_EN to R.string.settings_language_english
    )

    LaunchedEffect(currentProfileId) {
        if (currentProfileId != null) {
            currentProfile = container.profileRepository.getProfileById(currentProfileId)
            val config = container.configRepository.getConfigForProfile(currentProfileId)
            addEnabled = MathOperation.ADD in config.operations
            subEnabled = MathOperation.SUB in config.operations
            mulEnabled = MathOperation.MUL in config.operations
            divEnabled = MathOperation.DIV in config.operations
            minNumber = config.minNumber.toString()
            maxNumber = config.maxNumber.toString()
            questionCount = config.questionCount.toString()
        } else {
            currentProfile = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.sizeIn(minWidth = 64.dp, minHeight = 64.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.practice_back), modifier = Modifier.sizeIn(minWidth = 36.dp, minHeight = 36.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (currentProfileId == null) {
                Text(stringResource(R.string.settings_select_student_first))
            } else {
            Text(
                stringResource(R.string.settings_practice_title),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.settings_operations), style = MaterialTheme.typography.labelLarge)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = addEnabled, onCheckedChange = { addEnabled = it })
                        Text(stringResource(R.string.settings_addition))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = subEnabled, onCheckedChange = { subEnabled = it })
                        Text(stringResource(R.string.settings_subtraction))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = mulEnabled, onCheckedChange = { mulEnabled = it })
                        Text(stringResource(R.string.settings_multiplication))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = divEnabled, onCheckedChange = { divEnabled = it })
                        Text(stringResource(R.string.settings_division))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = minNumber,
                onValueChange = { minNumber = it.filter { c -> c.isDigit() } },
                label = { Text(stringResource(R.string.settings_min_number)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = maxNumber,
                onValueChange = { maxNumber = it.filter { c -> c.isDigit() } },
                label = { Text(stringResource(R.string.settings_max_number)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = questionCount,
                onValueChange = { questionCount = it.filter { c -> c.isDigit() } },
                label = { Text(stringResource(R.string.settings_questions_per_session)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val ops = mutableSetOf<MathOperation>()
                    if (addEnabled) ops.add(MathOperation.ADD)
                    if (subEnabled) ops.add(MathOperation.SUB)
                    if (mulEnabled) ops.add(MathOperation.MUL)
                    if (divEnabled) ops.add(MathOperation.DIV)
                    if (ops.isEmpty()) return@Button
                    val min = minNumber.toIntOrNull() ?: 1
                    val max = maxNumber.toIntOrNull() ?: 10
                    val count = questionCount.toIntOrNull()?.coerceIn(1, 50) ?: 10
                    val config = SessionConfig(
                        operations = ops,
                        minNumber = min,
                        maxNumber = max,
                        questionCount = count
                    )
                    scope.launch {
                        container.configRepository.saveConfigForProfile(currentProfileId!!, config)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().kidFriendlyButton()
            ) {
                Icon(Icons.Default.Save, contentDescription = stringResource(R.string.settings_save))
            }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = stringResource(
                        languageOptions.find { it.first == language }?.second ?: R.string.settings_language_system
                    ),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.settings_language)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { languageExpanded = true },
                    trailingIcon = {
                        IconButton(onClick = { languageExpanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = languageExpanded,
                    onDismissRequest = { languageExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    languageOptions.forEach { (lang, resId) ->
                        DropdownMenuItem(
                            text = { Text(stringResource(resId)) },
                            onClick = {
                                scope.launch {
                                    container.preferencesDataSource.setLanguage(lang)
                                    (context as? Activity)?.recreate()
                                }
                                languageExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            currentProfile?.let { profile ->
                Button(
                    onClick = { profileToDelete = profile },
                    modifier = Modifier.fillMaxWidth().kidFriendlyButton(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.home_delete_profile), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.home_delete_profile))
                }
            }
        }
    }

    profileToDelete?.let { profile ->
        androidx.compose.ui.window.Dialog(onDismissRequest = { profileToDelete = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(R.string.home_delete_confirm, profile.displayName),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { profileToDelete = null }) {
                            Text(stringResource(R.string.home_cancel))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    if (currentProfileId == profile.id) {
                                        container.preferencesDataSource.setCurrentProfileId(null)
                                    }
                                    container.profileRepository.deleteProfile(profile.id)
                                    profileToDelete = null
                                    onBack()
                                }
                            },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(stringResource(R.string.home_delete_profile))
                        }
                    }
                }
            }
        }
    }
}
