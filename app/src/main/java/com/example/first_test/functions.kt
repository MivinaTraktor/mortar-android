package com.example.first_test

import kotlin.math.*

fun findRange(v: Double, angle: Double): Double {
    val rad = angle.toRadian()
    return v * cos(rad) / G * (v * sin(rad) + sqrt(v.pow(2) * sin(rad).pow(2)))
}

fun Double.toRadian(): Double = this / 180.0 * PI

fun Double.toDegrees(): Double = this * 180.0 / PI

fun calcCoordinates(startArray: List<Int>, range: Double, azimuth: Double, altDif: Double) {
    val rad = azimuth.toRadian()
    tCoordinates[0] = (range * sin(rad) + startArray[0]).roundToInt()
    tCoordinates[1] = (range * cos(rad) + startArray[1]).roundToInt()
    tCoordinates[2] = (altDif + startArray[2]).roundToInt()
}

fun formatCoordinates(s: String): Int {
    if (s.all { it == '0' })
        return s.toInt()
    var res = s
    for (i in 0 until 5 - s.length)
        res = res.plus(4)
    return res.toInt()
}

fun chargesList(range: Double, altDif: Double): List<ChargePair> {
    val result: MutableList<ChargePair> = mutableListOf()
    mortarData.chargeSpeeds.forEachIndexed { i, v ->
        val solLo = solutionLo(v, range, altDif)
        val solHi = solutionHi(v, range, altDif)
        if (!(solHi.isNaN() && solLo.isNaN()))
            result.add(ChargePair(i, solHi, solLo, v))
    }
    return result.toList()
}

fun solutionLo(v: Double, range: Double, altDif: Double): Double {
    val sol360Lo =
        atan((v.pow(2) - sqrt(v.pow(4) - G * ((G * range.pow(2)) + (2 * altDif * v.pow(2))))) / (G * range)).toDegrees()
    return sol360Lo / 360.0 * mortarData.artDegree
}

fun solutionHi(v: Double, range: Double, altDif: Double): Double {
    val sol360Hi =
        atan((v.pow(2) + sqrt(v.pow(4) - G * ((G * range.pow(2)) + (2 * altDif * v.pow(2))))) / (G * range)).toDegrees()
    return sol360Hi / 360.0 * mortarData.artDegree
}

fun turn(start: Double, value: Double, circle: Double): Double {
    return when {
        start + value > circle -> start + value - circle
        start + value < 0.0 -> start + value + circle
        else -> start + value
    }
}

fun calcDeflection(aof: Int, def: Int, az: Double): Double =
    turn(def.toDouble(), az - aof, mortarData.artDegree)