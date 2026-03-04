package com.zettl.mathifier.domain.model

data class Problem(
    val operation: MathOperation,
    val operand1: Int,
    val operand2: Int,
    val correctAnswer: Int
) {
    val displayText: String
        get() = "$operand1 ${operation.symbol} $operand2 = ?"
}
