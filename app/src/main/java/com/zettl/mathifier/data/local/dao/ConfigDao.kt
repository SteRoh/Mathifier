package com.zettl.mathifier.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zettl.mathifier.data.local.entity.ProfileConfigEntity

@Dao
interface ConfigDao {

    @Query("SELECT * FROM profile_configs WHERE profileId = :profileId")
    suspend fun getConfigForProfile(profileId: Long): ProfileConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(config: ProfileConfigEntity)

    @Query("DELETE FROM profile_configs WHERE profileId = :profileId")
    suspend fun deleteForProfile(profileId: Long)
}
