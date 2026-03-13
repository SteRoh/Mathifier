package com.zettl.mathifier.ui.practice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.zettl.mathifier.AppContainer
import com.zettl.mathifier.R
import com.zettl.mathifier.domain.ProblemGenerator
import com.zettl.mathifier.ui.theme.kidFriendlyButton
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
            Text(stringResource(R.string.practice_select_student_first))
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
            Text(stringResource(R.string.practice_loading))
        }
        return
    }
    if (problems.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.practice_no_problems))
        }
        return
    }

    val currentProblem = problems.getOrNull(currentIndex)
    val finished = currentIndex >= problems.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.practice_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.sizeIn(minWidth = 64.dp, minHeight = 64.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.practice_back), modifier = Modifier.size(36.dp))
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
                val current = currentIndex + 1
                val total = problems.size
                val progress = current.toFloat() / total
                val correctCount = problems.take(answers.size).zip(answers).count { (p, a) -> p.correctAnswer == a }
                val wrongCount = answers.size - correctCount
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BasketChip(
                            icon = Icons.Default.ShoppingBasket,
                            count = correctCount,
                            tint = Color(0xFF2E7D32),
                            contentDescription = stringResource(R.string.practice_correct_desc)
                        )
                        BasketChip(
                            icon = Icons.Default.ShoppingBasket,
                            count = wrongCount,
                            tint = Color(0xFFC62828),
                            contentDescription = stringResource(R.string.practice_incorrect_desc)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.practice_progress_count, current, total),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = currentProblem.displayText,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 56.sp,
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
                        label = { Text(stringResource(R.string.practice_your_answer)) },
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
                        modifier = Modifier.fillMaxWidth().kidFriendlyButton()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = stringResource(R.string.practice_check), modifier = Modifier.size(32.dp))
                    }
                } else {
                    val correct = currentProblem.correctAnswer == answers.lastOrNull()
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + scaleIn(
                            initialScale = 0.5f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (correct) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = if (correct) stringResource(R.string.practice_correct_desc) else stringResource(R.string.practice_incorrect_desc),
                                modifier = Modifier.size(80.dp),
                                tint = if (correct) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (correct) stringResource(R.string.practice_correct) else stringResource(R.string.practice_answer_was, currentProblem.correctAnswer),
                                style = MaterialTheme.typography.titleLarge,
                                color = if (correct) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.error
                            )
                        }
                    }
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
                        modifier = Modifier.fillMaxWidth().kidFriendlyButton()
                    ) {
                        Icon(
                            imageVector = if (currentIndex + 1 >= problems.size) Icons.Default.EmojiEvents else Icons.Default.ArrowForward,
                            contentDescription = if (currentIndex + 1 >= problems.size) stringResource(R.string.practice_see_results) else stringResource(R.string.practice_next),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BasketChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int,
    tint: Color,
    contentDescription: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = tint.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = tint
            )
        }
    }
}
