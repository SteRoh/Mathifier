package com.zettl.mathifier.ui.progress

import com.zettl.mathifier.R

object BadgeStrings {
    fun titleRes(id: String): Int = when (id) {
        "first_session" -> R.string.badge_first_session_title
        "ten_correct_row" -> R.string.badge_ten_correct_row_title
        "add_master" -> R.string.badge_add_master_title
        "sub_master" -> R.string.badge_sub_master_title
        "mul_master" -> R.string.badge_mul_master_title
        "div_master" -> R.string.badge_div_master_title
        "week_streak" -> R.string.badge_week_streak_title
        else -> R.string.badge_first_session_title
    }

    fun descRes(id: String): Int = when (id) {
        "first_session" -> R.string.badge_first_session_desc
        "ten_correct_row" -> R.string.badge_ten_correct_row_desc
        "add_master" -> R.string.badge_add_master_desc
        "sub_master" -> R.string.badge_sub_master_desc
        "mul_master" -> R.string.badge_mul_master_desc
        "div_master" -> R.string.badge_div_master_desc
        "week_streak" -> R.string.badge_week_streak_desc
        else -> R.string.badge_first_session_desc
    }
}
