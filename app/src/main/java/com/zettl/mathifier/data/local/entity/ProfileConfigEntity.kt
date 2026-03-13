package com.zettl.mathifier.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "profile_configs",
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
data class ProfileConfigEntity(
    val profileId: Long,
    val operations: String,
    val minNumber: Int,
    val maxNumber: Int,
    val questionCount: Int
)
