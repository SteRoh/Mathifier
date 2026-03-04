package com.zettl.mathifier.domain

import com.zettl.mathifier.domain.model.MathOperation
import com.zettl.mathifier.domain.model.Problem
import com.zettl.mathifier.domain.model.SessionConfig

class ProblemGenerator {

    fun generateProblems(config: SessionConfig): List<Problem> {
        val ops = config.operations.toList()
        if (ops.isEmpty()) return emptyList()
        return (0 until config.questionCount).map {
            val op = ops[it % ops.size]
            generateProblem(op, config.minNumber, config.maxNumber)
        }
    }

    private fun generateProblem(operation: MathOperation, min: Int, max: Int): Problem {
        return when (operation) {
            MathOperation.ADD -> {
                val a = randomIn(min, max)
                val b = randomIn(min, max)
                Problem(MathOperation.ADD, a, b, a + b)
            }
            MathOperation.SUB -> {
                val a = randomIn(min, max)
                val b = randomIn(min, max)
                val (x, y) = if (a >= b) a to b else b to a
                Problem(MathOperation.SUB, x, y, x - y)
            }
            MathOperation.MUL -> {
                val a = randomIn(min, max)
                val b = randomIn(min, max)
                Problem(MathOperation.MUL, a, b, a * b)
            }
            MathOperation.DIV -> {
                val divisor = randomIn(maxOf(1, min), max)
                val quotient = randomIn(min, max)
                val dividend = divisor * quotient
                Problem(MathOperation.DIV, dividend, divisor, quotient)
            }
        }
    }

    private fun randomIn(min: Int, max: Int): Int {
        if (min >= max) return min
        return (min..max).random()
    }
}
