package com.zettl.mathifier.domain.model

enum class MathOperation(val symbol: String, val dbValue: String) {
    ADD("+", "ADD"),
    SUB("−", "SUB"),
    MUL("×", "MUL"),
    DIV("÷", "DIV");

    companion object {
        fun fromDbValue(value: String): MathOperation? = entries.find { it.dbValue == value }
        fun fromDbValues(values: List<String>): Set<MathOperation> =
            values.mapNotNull { fromDbValue(it) }.toSet()
    }
}
