package com.zettl.mathifier.data.repository

import com.zettl.mathifier.data.local.dao.ProgressDao
import com.zettl.mathifier.data.local.entity.EarnedBadgeEntity
import com.zettl.mathifier.data.local.entity.ProfileProgressEntity
import com.zettl.mathifier.domain.BadgeDefinitions
import com.zettl.mathifier.domain.Scoring
import com.zettl.mathifier.domain.model.MathOperation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ProfileProgressView(
    val totalPoints: Int,
    val currentStreakDays: Int,
    val earnedBadges: List<com.zettl.mathifier.domain.Badge>
)

class ProgressRepository(
    private val progressDao: ProgressDao
) {

    suspend fun getProgressView(profileId: Long): ProfileProgressView {
        val progress = progressDao.getProgress(profileId) ?: ProfileProgressEntity(profileId = profileId)
        val earned = progressDao.getEarnedBadges(profileId).map { BadgeDefinitions.getById(it.badgeId) }.filterNotNull()
        return ProfileProgressView(
            totalPoints = progress.totalPoints,
            currentStreakDays = progress.currentStreakDays,
            earnedBadges = earned
        )
    }

    suspend fun recordSessionResult(
        profileId: Long,
        correctCount: Int,
        totalCount: Int,
        correctInRow: Int,
        sessionCountBefore: Int
    ) {
        val now = System.currentTimeMillis()
        val existing = progressDao.getProgress(profileId) ?: ProfileProgressEntity(profileId = profileId)
        val newPoints = existing.totalPoints + correctCount * Scoring.POINTS_PER_CORRECT
        val newStreak = Scoring.updateStreakDays(existing.lastPracticeDate, existing.currentStreakDays)
        val correctByOp = MathOperation.entries.associateWith { op ->
            progressDao.getCorrectCountForOperation(profileId, op.dbValue)
        }
        val earnedBadgeIds = progressDao.getEarnedBadges(profileId).map { it.badgeId }.toSet()
        val newBadges = Scoring.badgesToEarn(
            sessionCorrectInRow = correctInRow,
            totalSessionsBefore = sessionCountBefore,
            correctCountByOperation = correctByOp,
            newStreakDays = newStreak,
            alreadyEarnedBadgeIds = earnedBadgeIds
        )
        progressDao.insertOrReplaceProgress(
            existing.copy(
                totalPoints = newPoints,
                lastPracticeDate = now,
                currentStreakDays = newStreak
            )
        )
        newBadges.forEach { badgeId ->
            progressDao.insertBadge(EarnedBadgeEntity(profileId = profileId, badgeId = badgeId))
        }
    }

    fun getProgressFlow(profileId: Long): Flow<ProfileProgressView> = flow {
        emit(getProgressView(profileId))
    }
}
