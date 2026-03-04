package com.zettl.mathifier.domain

import com.zettl.mathifier.domain.model.MathOperation

object Scoring {
    const val POINTS_PER_CORRECT = 10

    fun updateStreakDays(lastPracticeDate: Long?, currentStreakDays: Int): Int {
        val today = getTodayStartMillis()
        if (lastPracticeDate == null) return 1
        val lastDay = getDayStartMillis(lastPracticeDate)
        val diffDays = ((today - lastDay) / (24 * 60 * 60 * 1000)).toInt()
        return when {
            diffDays == 0 -> currentStreakDays
            diffDays == 1 -> currentStreakDays + 1
            else -> 1
        }
    }

    private fun getTodayStartMillis(): Long {
        val cal = java.util.Calendar.getInstance()
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getDayStartMillis(timeMillis: Long): Long {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = timeMillis
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun badgesToEarn(
        sessionCorrectInRow: Int,
        totalSessionsBefore: Int,
        correctCountByOperation: Map<MathOperation, Int>,
        newStreakDays: Int,
        alreadyEarnedBadgeIds: Set<String>
    ): List<String> {
        val toEarn = mutableListOf<String>()
        if (totalSessionsBefore == 0 && !alreadyEarnedBadgeIds.contains("first_session")) {
            toEarn.add("first_session")
        }
        if (sessionCorrectInRow >= 10 && !alreadyEarnedBadgeIds.contains("ten_correct_row")) {
            toEarn.add("ten_correct_row")
        }
        correctCountByOperation[MathOperation.ADD]?.let { if (it >= 50 && !alreadyEarnedBadgeIds.contains("add_master")) toEarn.add("add_master") }
        correctCountByOperation[MathOperation.SUB]?.let { if (it >= 50 && !alreadyEarnedBadgeIds.contains("sub_master")) toEarn.add("sub_master") }
        correctCountByOperation[MathOperation.MUL]?.let { if (it >= 50 && !alreadyEarnedBadgeIds.contains("mul_master")) toEarn.add("mul_master") }
        correctCountByOperation[MathOperation.DIV]?.let { if (it >= 50 && !alreadyEarnedBadgeIds.contains("div_master")) toEarn.add("div_master") }
        if (newStreakDays >= 7 && !alreadyEarnedBadgeIds.contains("week_streak")) {
            toEarn.add("week_streak")
        }
        return toEarn
    }
}
