package com.zettl.mathifier.ui.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zettl.mathifier.AppContainer
import com.zettl.mathifier.domain.ProblemGenerator
import com.zettl.mathifier.domain.model.Problem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    container: AppContainer,
    currentProfileId: Long?,
    onBack: () -> Unit,
    onSessionComplete: () -> Unit
) {
    if (currentProfileId == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Select a student first.")
        }
        return
    }

    var config by remember { mutableStateOf<com.zettl.mathifier.domain.model.SessionConfig?>(null) }
    var problems by remember { mutableStateOf<List<Problem>>(emptyList()) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var answers by remember { mutableStateOf<List<Int>>(emptyList()) }
    var showResult by remember { mutableStateOf(false) }
    var sessionStartedAt by remember { mutableStateOf(0L) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentProfileId) {
        config = container.configRepository.getConfigForProfile(currentProfileId)
        config?.let { c ->
            problems = ProblemGenerator().generateProblems(c)
            sessionStartedAt = System.currentTimeMillis()
        }
    }

    if (config == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
        return
    }
    if (problems.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No problems. Enable at least one operation in Settings.")
        }
        return
    }

    val currentProblem = problems.getOrNull(currentIndex)
    val finished = currentIndex >= problems.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Practice") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!finished && currentProblem != null) {
                Text(
                    text = "Question ${currentIndex + 1} of ${problems.size}",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = currentProblem.displayText,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
                if (!showResult) {
                    OutlinedTextField(
                        value = userAnswer,
                        onValueChange = { userAnswer = it.filter { c -> c.isDigit() || c == '-' } },
                        label = { Text("Your answer") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            val answer = userAnswer.toIntOrNull() ?: return@Button
                            answers = answers + answer
                            showResult = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Check")
                    }
                } else {
                    val correct = currentProblem.correctAnswer == answers.lastOrNull()
                    Text(
                        text = if (correct) "Correct!" else "The answer was ${currentProblem.correctAnswer}",
                        style = MaterialTheme.typography.titleLarge,
                        color = if (correct) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            showResult = false
                            userAnswer = ""
                            currentIndex++
                            if (currentIndex >= problems.size) {
                                scope.launch {
                                    val correctCount = problems.zip(answers).count { (p, a) -> p.correctAnswer == a }
                                    var maxInRow = 0
                                    var current = 0
                                    problems.zip(answers).forEach { (p, a) ->
                                        if (p.correctAnswer == a) {
                                            current++
                                            maxInRow = maxOf(maxInRow, current)
                                        } else current = 0
                                    }
                                    val sessionCountBefore = container.sessionRepository.getSessionCountForProfile(currentProfileId)
                                    container.sessionRepository.saveSession(
                                        profileId = currentProfileId,
                                        startedAt = sessionStartedAt,
                                        endedAt = System.currentTimeMillis(),
                                        problems = problems,
                                        userAnswers = answers
                                    )
                                    container.progressRepository.recordSessionResult(
                                        profileId = currentProfileId,
                                        correctCount = correctCount,
                                        totalCount = problems.size,
                                        correctInRow = maxInRow,
                                        sessionCountBefore = sessionCountBefore
                                    )
                                    onSessionComplete()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (currentIndex + 1 >= problems.size) "See results" else "Next")
                    }
                }
            }
        }
    }
}
