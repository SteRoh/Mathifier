package com.zettl.mathifier.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "profile_progress",
    foreignKeys = [
        ForeignKey(
            entity = StudentProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId")],
    primaryKeys = ["profileId"]
)
data class ProfileProgressEntity(
    val profileId: Long,
    val totalPoints: Int = 0,
    val lastPracticeDate: Long? = null,
    val currentStreakDays: Int = 0
)
