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

fun inCoord(s: String): Int {
    if (s.all { it == '0' })
        return s.toInt()
    return s.padEnd(5, '4').toInt()
}

fun outCoord(num: Int): String {
    if (num == 0)
        return num.toString()
    return num.toString().padStart(5, '0')
}

fun chargesList(range: Double, altDif: Double): List<ChargePair> {
    val result: MutableList<ChargePair> = mutableListOf()
    mortarData.chargeSpeeds.forEachIndexed { i, v ->
        val lo = atanLo(v, range, altDif)
        val hi = atanHi(v, range, altDif)
        if (!(lo.isNaN() && hi.isNaN())) {
            val loTime = range / (v * cos(lo))
            val hiTime = range / (v * cos(hi))
            result.add(
                ChargePair(
                    i, lo.toArtDegrees(), loTime,
                    hi.toArtDegrees(), hiTime, v
                )
            )
        }
    }
    return result.toList()
}

fun atanLo(v: Double, range: Double, altDif: Double): Double =
    atan((v.pow(2) - sqrt(v.pow(4) - G * ((G * range.pow(2)) + (2 * altDif * v.pow(2))))) / (G * range))

fun atanHi(v: Double, range: Double, altDif: Double): Double =
    atan((v.pow(2) + sqrt(v.pow(4) - G * ((G * range.pow(2)) + (2 * altDif * v.pow(2))))) / (G * range))

fun Double.toArtDegrees(): Double = this.toDegrees() / 360.0 * mortarData.artDegree

fun turn(start: Double, value: Double, circle: Double): Double {
    return when {
        start + value > circle -> start + value - circle
        start + value < 0.0 -> start + value + circle
        else -> start + value
    }
}

fun calcDeflection(aof: Int, def: Int, az: Double): Double =
    turn(def.toDouble(), az - aof, mortarData.artDegree)

fun heightInterpolation(x: Int, y: Int, step: Int = 10): String {
    val xLast = x % step
    val yLast = y % step
    val x1 = (x - xLast) / step
    val x2 = x1 + 1
    val y1 = (y - yLast) / step
    val y2 = y1 + 1
    val xRatio = xLast / step.toDouble()
    val yRatio = yLast / step.toDouble()
    val y1val = (heightArr[y1][x2] - heightArr[y1][x1]) * xRatio + heightArr[y1][x1]
    val y2val = (heightArr[y2][x2] - heightArr[y2][x1]) * xRatio + heightArr[y2][x1]
    return ((y2val - y1val) * yRatio + y1val).roundToInt().toString()
}