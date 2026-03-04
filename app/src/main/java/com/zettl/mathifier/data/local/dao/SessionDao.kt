package com.zettl.mathifier.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zettl.mathifier.data.local.entity.SessionEntity
import com.zettl.mathifier.data.local.entity.SessionItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insertSession(session: SessionEntity): Long

    @Insert
    suspend fun insertSessionItems(items: List<SessionItemEntity>)

    @Query("SELECT * FROM sessions WHERE profileId = :profileId ORDER BY startedAt DESC LIMIT :limit")
    fun getRecentSessions(profileId: Long, limit: Int = 50): Flow<List<SessionEntity>>

    @Query("SELECT * FROM session_items WHERE sessionId = :sessionId ORDER BY orderIndex ASC")
    suspend fun getItemsForSession(sessionId: Long): List<SessionItemEntity>

    @Query("SELECT COUNT(*) FROM sessions WHERE profileId = :profileId")
    suspend fun getSessionCountForProfile(profileId: Long): Int
}
