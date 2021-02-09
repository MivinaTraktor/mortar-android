package com.example.first_test

import kotlin.math.*

fun target(m: Array<Int>, t: Array<Int>): Array<Double> {
    val x = m[0]
    val y = m[1]
    val alt = m[2]
    val tgtX = t[0]
    val tgtY = t[1]
    val tgtAlt = t[2]
    val altDif = (tgtAlt - alt).toDouble()
    val range = hypot(((tgtX - x).toDouble()), ((tgtY - y).toDouble())) * rangeMultiplier.toDouble()
    val angle: Double
    if ((tgtX - x) == 0) {
        if ((tgtY - y) > 0)
            angle = 0.0
        else
            angle = 180.0
    }
    else angle = atan(((tgtY - y).toDouble() / (tgtX - x).toDouble())) * 180.0 / PI
    val azimuth: Double
    azimuth = when {
        tgtX > x -> 90.0 - angle
        tgtX < x -> 270.0 - angle
        else -> 0.0
    }
    val azimuth_mil = azimuth * MIL
    return arrayOf(range, altDif, azimuth_mil, azimuth)
}

fun findRange(v: Double, angle: Double): Double {
    val rad = angle.toRadian()
    return v * cos(rad) / G * (v * sin(rad) + sqrt(v.pow(2) * sin(rad).pow(2)))
}

fun Double.toRadian(): Double = this / 180.0 * PI

fun Double.toDegrees(): Double = this * 180.0 / PI

fun calcCoordinates(startArray: Array<Int>, range: Double, azimuth: Double, altDif: Double) {
    val rad = azimuth.toRadian()
    tCoordinates[0] = (range * sin(rad) / rangeMultiplier + startArray[0]).roundToInt()
    tCoordinates[1] = (range * cos(rad) / rangeMultiplier + startArray[1]).roundToInt()
    tCoordinates[2] = (altDif + startArray[2]).roundToInt()
}

fun chargesList(tValues: Array<Double>): List<ChargePair> {
    var result: MutableList<ChargePair> = mutableListOf()
    mortarCharges!!.forEachIndexed { i, multiplier ->
        val v = muzzleVelocity!! * multiplier
        val sol360Hi = atan((v.pow(2) + sqrt(v.pow(4) - G * ((G * tValues[0].pow(2)) + (2 * tValues[1] * v.pow(2))))) / (G * tValues[0])).toDegrees()
        val solMilHi = sol360Hi * MIL
        val sol360Lo = atan((v.pow(2) - sqrt(v.pow(4) - G * ((G * tValues[0].pow(2)) + (2 * tValues[1] * v.pow(2))))) / (G * tValues[0])).toDegrees()
        val solMilLo = sol360Lo * MIL
        if (!(sol360Hi.isNaN() && sol360Lo.isNaN()))
            result.add(ChargePair(i, Solution(solMilHi, sol360Hi), Solution(solMilLo, sol360Lo)))
    }
    return result.toList()
}