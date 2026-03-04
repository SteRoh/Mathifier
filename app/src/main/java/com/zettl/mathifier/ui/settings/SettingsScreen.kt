package com.zettl.mathifier.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zettl.mathifier.AppContainer
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
    var grade by remember { mutableIntStateOf(1) }
    var addEnabled by remember { mutableStateOf(true) }
    var subEnabled by remember { mutableStateOf(false) }
    var mulEnabled by remember { mutableStateOf(false) }
    var divEnabled by remember { mutableStateOf(false) }
    var minNumber by remember { mutableStateOf("1") }
    var maxNumber by remember { mutableStateOf("10") }
    var questionCount by remember { mutableStateOf("10") }
    var saved by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentProfileId) {
        if (currentProfileId != null) {
            val config = container.configRepository.getConfigForProfile(currentProfileId)
            grade = config.grade
            addEnabled = MathOperation.ADD in config.operations
            subEnabled = MathOperation.SUB in config.operations
            mulEnabled = MathOperation.MUL in config.operations
            divEnabled = MathOperation.DIV in config.operations
            minNumber = config.minNumber.toString()
            maxNumber = config.maxNumber.toString()
            questionCount = config.questionCount.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { padding ->
        if (currentProfileId == null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
            ) {
                Text("Select a student on the home screen first to change settings.")
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                "Practice settings (for this student)",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Grade", style = MaterialTheme.typography.labelLarge)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        (1..4).forEach { g ->
                            Button(
                                onClick = { grade = g },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (grade == g) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text("$g")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Operations", style = MaterialTheme.typography.labelLarge)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = addEnabled, onCheckedChange = { addEnabled = it })
                        Text("Addition")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = subEnabled, onCheckedChange = { subEnabled = it })
                        Text("Subtraction")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = mulEnabled, onCheckedChange = { mulEnabled = it })
                        Text("Multiplication")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = divEnabled, onCheckedChange = { divEnabled = it })
                        Text("Division")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = minNumber,
                onValueChange = { minNumber = it.filter { c -> c.isDigit() } },
                label = { Text("Min number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = maxNumber,
                onValueChange = { maxNumber = it.filter { c -> c.isDigit() } },
                label = { Text("Max number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = questionCount,
                onValueChange = { questionCount = it.filter { c -> c.isDigit() } },
                label = { Text("Questions per session") },
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
                        grade = grade,
                        operations = ops,
                        minNumber = min,
                        maxNumber = max,
                        questionCount = count
                    )
                    scope.launch {
                        container.configRepository.saveConfigForProfile(currentProfileId!!, config)
                        saved = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save settings")
            }
            if (saved) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Saved!", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
