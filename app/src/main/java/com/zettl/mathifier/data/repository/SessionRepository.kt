package com.zettl.mathifier.data.repository

import com.zettl.mathifier.data.local.dao.SessionDao
import com.zettl.mathifier.data.local.entity.SessionEntity
import com.zettl.mathifier.data.local.entity.SessionItemEntity
import com.zettl.mathifier.domain.model.MathOperation
import com.zettl.mathifier.domain.model.Problem
import kotlinx.coroutines.flow.Flow

class SessionRepository(private val sessionDao: SessionDao) {

    suspend fun saveSession(
        profileId: Long,
        startedAt: Long,
        endedAt: Long,
        problems: List<Problem>,
        userAnswers: List<Int>
    ): Long {
        val correctCount = problems.zip(userAnswers).count { (p, a) -> p.correctAnswer == a }
        val session = SessionEntity(
            profileId = profileId,
            startedAt = startedAt,
            endedAt = endedAt,
            score = correctCount,
            totalQuestions = problems.size
        )
        val sessionId = sessionDao.insertSession(session)
        val items = problems.zip(userAnswers).mapIndexed { index, (problem, answer) ->
            SessionItemEntity(
                sessionId = sessionId,
                operation = problem.operation.dbValue,
                operand1 = problem.operand1,
                operand2 = problem.operand2,
                correctAnswer = problem.correctAnswer,
                userAnswer = answer,
                correct = problem.correctAnswer == answer,
                orderIndex = index
            )
        }
        sessionDao.insertSessionItems(items)
        return sessionId
    }

    fun getRecentSessions(profileId: Long, limit: Int = 50): Flow<List<SessionEntity>> =
        sessionDao.getRecentSessions(profileId, limit)

    suspend fun getSessionCountForProfile(profileId: Long): Int =
        sessionDao.getSessionCountForProfile(profileId)
}
