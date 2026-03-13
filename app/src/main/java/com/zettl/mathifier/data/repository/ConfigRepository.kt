package com.zettl.mathifier.data.repository

import com.zettl.mathifier.data.local.dao.ConfigDao
import com.zettl.mathifier.data.local.entity.ProfileConfigEntity
import com.zettl.mathifier.domain.model.MathOperation
import com.zettl.mathifier.domain.model.SessionConfig

class ConfigRepository(private val configDao: ConfigDao) {

    suspend fun getConfigForProfile(profileId: Long): SessionConfig {
        val entity = configDao.getConfigForProfile(profileId)
        return entity?.toDomain() ?: defaultConfig()
    }

    suspend fun saveConfigForProfile(profileId: Long, config: SessionConfig) {
        configDao.insertOrReplace(config.toEntity(profileId))
    }

    suspend fun deleteConfigForProfile(profileId: Long) {
        configDao.deleteForProfile(profileId)
    }

    private fun defaultConfig(): SessionConfig = SessionConfig(
        operations = setOf(MathOperation.ADD),
        minNumber = 1,
        maxNumber = 10,
        questionCount = 10
    )

    private fun ProfileConfigEntity.toDomain(): SessionConfig = SessionConfig(
        operations = operations.split(",").mapNotNull { MathOperation.fromDbValue(it.trim()) }.toSet(),
        minNumber = minNumber,
        maxNumber = maxNumber,
        questionCount = questionCount
    )

    private fun SessionConfig.toEntity(profileId: Long): ProfileConfigEntity = ProfileConfigEntity(
        profileId = profileId,
        operations = operations.joinToString(",") { it.dbValue },
        minNumber = minNumber,
        maxNumber = maxNumber,
        questionCount = questionCount
    )
}
