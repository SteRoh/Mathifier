package com.zettl.mathifier.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zettl.mathifier.data.local.entity.EarnedBadgeEntity
import com.zettl.mathifier.data.local.entity.ProfileProgressEntity

@Dao
interface ProgressDao {

    @Query("SELECT * FROM profile_progress WHERE profileId = :profileId")
    suspend fun getProgress(profileId: Long): ProfileProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceProgress(progress: ProfileProgressEntity)

    @Query("SELECT * FROM earned_badges WHERE profileId = :profileId")
    suspend fun getEarnedBadges(profileId: Long): List<EarnedBadgeEntity>

    @Insert
    suspend fun insertBadge(badge: EarnedBadgeEntity)

    @Query("SELECT COUNT(*) FROM session_items si JOIN sessions s ON si.sessionId = s.id WHERE s.profileId = :profileId AND si.operation = :operation AND si.correct = 1")
    suspend fun getCorrectCountForOperation(profileId: Long, operation: String): Int
}
