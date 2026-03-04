package com.zettl.mathifier.domain.model

data class SessionConfig(
    val grade: Int,
    val operations: Set<MathOperation>,
    val minNumber: Int,
    val maxNumber: Int,
    val questionCount: Int
) {
    init {
        require(grade in 1..4) { "Grade must be 1-4" }
        require(operations.isNotEmpty()) { "At least one operation required" }
        require(minNumber <= maxNumber) { "minNumber must be <= maxNumber" }
        require(questionCount in 1..50) { "questionCount must be 1-50" }
    }
}
