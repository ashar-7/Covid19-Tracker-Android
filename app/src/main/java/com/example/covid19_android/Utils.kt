package com.example.covid19_android

import java.text.NumberFormat
import java.util.*

fun formatNumber(num: Number): String =
    NumberFormat.getNumberInstance(Locale.getDefault()).format(num)

fun mapRange(
    num: Double,
    start1: Double, end1: Double,
    start2: Double, end2: Double
): Double {
    val epsilon = 1e-12

    if (kotlin.math.abs(end1 - start1) < epsilon) {
        throw ArithmeticException("/ 0")
    }

    val ratio = (end2 - start2) / (end1 - start1)
    return ratio * (num - start1) + start2
}
