package com.zettl.mathifier

import android.app.Application
import androidx.room.Room
import com.zettl.mathifier.data.datastore.PreferencesDataSource
import com.zettl.mathifier.data.local.MIGRATION_1_2
import com.zettl.mathifier.data.local.MathifierDatabase
import com.zettl.mathifier.data.repository.ConfigRepository
import com.zettl.mathifier.data.repository.ProfileRepository
import com.zettl.mathifier.data.repository.ProgressRepository
import com.zettl.mathifier.data.repository.SessionRepository

class MathifierApplication : Application() {

    val appContainer: AppContainer by lazy {
        val db = Room.databaseBuilder(
            applicationContext,
            MathifierDatabase::class.java,
            "mathifier_db"
        ).addMigrations(MIGRATION_1_2).build()
        val prefs = PreferencesDataSource(applicationContext)
        AppContainer(
            profileRepository = ProfileRepository(db.profileDao()),
            configRepository = ConfigRepository(db.configDao()),
            sessionRepository = SessionRepository(db.sessionDao()),
            progressRepository = ProgressRepository(db.progressDao()),
            preferencesDataSource = prefs
        )
    }
}

data class AppContainer(
    val profileRepository: ProfileRepository,
    val configRepository: ConfigRepository,
    val sessionRepository: SessionRepository,
    val progressRepository: ProgressRepository,
    val preferencesDataSource: PreferencesDataSource
)
