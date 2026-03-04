package com.zettl.mathifier.domain

data class Badge(
    val id: String,
    val title: String,
    val description: String
)

object BadgeDefinitions {
    val ALL = listOf(
        Badge("first_session", "First Steps", "Complete your first practice session"),
        Badge("ten_correct_row", "Hot Streak", "Get 10 correct answers in a row"),
        Badge("add_master", "Addition Master", "Answer 50 addition problems correctly"),
        Badge("sub_master", "Subtraction Master", "Answer 50 subtraction problems correctly"),
        Badge("mul_master", "Multiplication Master", "Answer 50 multiplication problems correctly"),
        Badge("div_master", "Division Master", "Answer 50 division problems correctly"),
        Badge("week_streak", "Week Warrior", "Practice 7 days in a row")
    )

    fun getById(id: String): Badge? = ALL.find { it.id == id }
}
