package com.zettl.mathifier.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zettl.mathifier.data.local.dao.ConfigDao
import com.zettl.mathifier.data.local.dao.ProgressDao
import com.zettl.mathifier.data.local.dao.ProfileDao
import com.zettl.mathifier.data.local.dao.SessionDao
import com.zettl.mathifier.data.local.entity.EarnedBadgeEntity
import com.zettl.mathifier.data.local.entity.ProfileConfigEntity
import com.zettl.mathifier.data.local.entity.ProfileProgressEntity
import com.zettl.mathifier.data.local.entity.SessionEntity
import com.zettl.mathifier.data.local.entity.SessionItemEntity
import com.zettl.mathifier.data.local.entity.StudentProfileEntity

@Database(
    entities = [
        StudentProfileEntity::class,
        ProfileConfigEntity::class,
        SessionEntity::class,
        SessionItemEntity::class,
        ProfileProgressEntity::class,
        EarnedBadgeEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MathifierDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun configDao(): ConfigDao
    abstract fun sessionDao(): SessionDao
    abstract fun progressDao(): ProgressDao
}
