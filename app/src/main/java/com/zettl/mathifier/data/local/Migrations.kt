package com.zettl.mathifier.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS profile_configs_new (
                profileId INTEGER NOT NULL PRIMARY KEY,
                operations TEXT NOT NULL,
                minNumber INTEGER NOT NULL,
                maxNumber INTEGER NOT NULL,
                questionCount INTEGER NOT NULL,
                FOREIGN KEY(profileId) REFERENCES student_profiles(id) ON DELETE CASCADE
            )
        """.trimIndent())
        database.execSQL("""
            INSERT INTO profile_configs_new (profileId, operations, minNumber, maxNumber, questionCount)
            SELECT profileId, operations, minNumber, maxNumber, questionCount FROM profile_configs
        """.trimIndent())
        database.execSQL("DROP TABLE profile_configs")
        database.execSQL("ALTER TABLE profile_configs_new RENAME TO profile_configs")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_profile_configs_profileId ON profile_configs(profileId)")
    }
}
