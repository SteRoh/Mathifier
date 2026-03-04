package com.zettl.mathifier.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_profiles")
data class StudentProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val displayName: String,
    val avatarId: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)
