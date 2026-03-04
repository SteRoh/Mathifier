package com.zettl.mathifier.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zettl.mathifier.data.local.entity.StudentProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("SELECT * FROM student_profiles ORDER BY createdAt ASC")
    fun getAllProfiles(): Flow<List<StudentProfileEntity>>

    @Query("SELECT * FROM student_profiles WHERE id = :id")
    suspend fun getProfileById(id: Long): StudentProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: StudentProfileEntity): Long

    @Update
    suspend fun update(profile: StudentProfileEntity)

    @Query("DELETE FROM student_profiles WHERE id = :id")
    suspend fun deleteById(id: Long)
}
