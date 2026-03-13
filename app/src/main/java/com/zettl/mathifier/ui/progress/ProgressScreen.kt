package com.zettl.mathifier.ui.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.zettl.mathifier.AppContainer
import com.zettl.mathifier.R
import com.zettl.mathifier.data.repository.ProfileProgressView
import com.zettl.mathifier.domain.Badge
import com.zettl.mathifier.ui.progress.BadgeStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    container: AppContainer,
    currentProfileId: Long?,
    onBack: () -> Unit
) {
    var progress by remember { mutableStateOf<ProfileProgressView?>(null) }

    LaunchedEffect(currentProfileId) {
        if (currentProfileId != null) {
            progress = container.progressRepository.getProgressView(currentProfileId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.progress_title)) },
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
        if (currentProfileId == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.progress_select_student_first))
            }
            return@Scaffold
        }
        if (progress == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.progress_loading))
            }
            return@Scaffold
        }
        val p = progress!!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.progress_total_points), style = MaterialTheme.typography.labelLarge)
                        Text("${p.totalPoints}", style = MaterialTheme.typography.headlineMedium)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.progress_current_streak), style = MaterialTheme.typography.labelLarge)
                        Text(stringResource(R.string.progress_days, p.currentStreakDays), style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                stringResource(R.string.progress_badges),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (p.earnedBadges.isEmpty()) {
                Text(
                    stringResource(R.string.progress_no_badges),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(p.earnedBadges) { badge ->
                        BadgeCard(badge = badge)
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgeCard(badge: Badge) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Text(
                    text = stringResource(BadgeStrings.titleRes(badge.id)),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(BadgeStrings.descRes(badge.id)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
