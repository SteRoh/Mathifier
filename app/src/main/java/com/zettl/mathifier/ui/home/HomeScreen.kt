package com.zettl.mathifier.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zettl.mathifier.R
import androidx.compose.foundation.layout.sizeIn
import com.zettl.mathifier.AppContainer
import com.zettl.mathifier.data.local.entity.StudentProfileEntity
import com.zettl.mathifier.ui.theme.kidFriendlyButton
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    container: AppContainer,
    currentProfileId: Long?,
    onNavigateToSettings: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToProgress: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val profiles by container.profileRepository.getAllProfiles().collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.mathifier_logo),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = stringResource(R.string.home_title),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.sizeIn(minWidth = 72.dp, minHeight = 72.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.home_add_profile), modifier = Modifier.size(36.dp))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.home_choose_student),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            if (profiles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.home_no_profiles),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(profiles) { profile ->
                        ProfileCard(
                            profile = profile,
                            isSelected = profile.id == currentProfileId,
                            onClick = {
                                scope.launch {
                                    container.preferencesDataSource.setCurrentProfileId(profile.id)
                                }
                            },
                            onSettingsClick = {
                                scope.launch {
                                    container.preferencesDataSource.setCurrentProfileId(profile.id)
                                    onNavigateToSettings()
                                }
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onNavigateToPractice,
                    modifier = Modifier.weight(1f).kidFriendlyButton(),
                    enabled = currentProfileId != null
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = stringResource(R.string.home_practice), modifier = Modifier.size(32.dp))
                }
                Button(
                    onClick = onNavigateToProgress,
                    modifier = Modifier.weight(1f).kidFriendlyButton(),
                    enabled = currentProfileId != null
                ) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = stringResource(R.string.home_my_progress), modifier = Modifier.size(32.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showAddDialog) {
        AddProfileDialog(
            name = newName,
            onNameChange = { newName = it },
            onConfirm = {
                scope.launch {
                    if (newName.isNotBlank()) {
                        container.profileRepository.addProfile(newName.trim())
                        newName = ""
                        showAddDialog = false
                    }
                }
            },
            onDismiss = {
                newName = ""
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun ProfileCard(
    profile: StudentProfileEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = profile.displayName,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { onSettingsClick() },
                modifier = Modifier.sizeIn(minWidth = 64.dp, minHeight = 64.dp)
            ) {
                Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.home_settings), modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
private fun AddProfileDialog(
    name: String,
    onNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.home_add_student),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text(stringResource(R.string.home_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss, modifier = Modifier.sizeIn(minWidth = 72.dp, minHeight = 72.dp)) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.home_cancel), modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    IconButton(onClick = onConfirm, modifier = Modifier.sizeIn(minWidth = 72.dp, minHeight = 72.dp)) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.home_add), modifier = Modifier.size(36.dp))
                    }
                }
            }
        }
    }
}
