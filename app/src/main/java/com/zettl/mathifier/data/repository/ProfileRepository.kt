package com.zettl.mathifier.data.repository

import com.zettl.mathifier.data.local.dao.ProfileDao
import com.zettl.mathifier.data.local.entity.StudentProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepository(
    private val profileDao: ProfileDao
) {

    fun getAllProfiles(): Flow<List<StudentProfileEntity>> = profileDao.getAllProfiles()

    suspend fun getProfileById(id: Long): StudentProfileEntity? = profileDao.getProfileById(id)

    suspend fun addProfile(displayName: String): Long {
        val entity = StudentProfileEntity(displayName = displayName)
        return profileDao.insert(entity)
    }

    suspend fun updateProfile(profile: StudentProfileEntity) {
        profileDao.update(profile)
    }

    suspend fun deleteProfile(id: Long) {
        profileDao.deleteById(id)
    }
}
