package com.example.first_test

import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.hypot

data class ChargePair(val charge: Int, val hi: Double, val lo: Double, val v: Double)

class FiringData(private val mortar: Array<Int>, private val target: Array<Int>) {
    val range = hypot(((target[0] - mortar[0]).toDouble()), ((target[1] - mortar[1]).toDouble())) * rangeMultiplier.toDouble()
    val altDif = (target[2] - mortar[2]).toDouble()
    val azimuth = angle() / 360.0 * artDegree

    fun angle(): Double {
        var angle: Double
        val xDif = target[0] - mortar[0]
        val yDif = target[1] - mortar[1]
        if ((xDif) == 0) {
            if ((yDif) > 0)
                angle = 0.0
            else
                angle = 180.0
        }
        else angle = atan(((yDif).toDouble() / (xDif).toDouble())) * 180.0 / PI
        angle = when {
            target[0] > mortar[0] -> 90.0 - angle
            target[0] < mortar[0] -> 270.0 - angle
            else -> 0.0
        }
        return angle
    }
}

var mCoordinates: Array<Int?> = Array(3) { null }
var tCoordinates: Array<Int?> = Array(3) { null }
var deflectionArray: Array<Int?> = Array(2) { null }
var rangeMultiplier = 100
var mortarCharges: Array<Double>? = null
var specialCharges: Map<Int, String>? = null
var artDegree = 6400.0
var zeros = "Select format"
var useDeflection = false
val stdDispersion = 50
const val G = 9.81
const val MAX_ANGLE = 85.0
const val MIN_ANGLE = 45.0