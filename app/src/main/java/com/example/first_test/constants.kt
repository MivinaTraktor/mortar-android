package com.example.first_test

import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.hypot

data class ChargePair(val charge: Int, val lo: Double, val loTime: Double,
                      val hi: Double, val hiTime: Double, val v: Double)

class FiringData(private val mortar: List<Int>, private val target: List<Int>) {
    val range = hypot(((target[0] - mortar[0]).toDouble()), ((target[1] - mortar[1]).toDouble()))
    val altDif = (target[2] - mortar[2]).toDouble()
    val azimuth = angle() / 360.0 * mortarData.artDegree

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

var mCoordinates: MutableList<Int?> = MutableList(3) { null }
var tCoordinates: MutableList<Int?> = MutableList(3) { null }
var deflectionArray: MutableList<Int?> = MutableList(2) { null }
var mortarData = MortarData()
var heightArr: List<List<Int>> = emptyList()
var spinnerArtyPos = 0
var spinnerMapPos = 0
var useDeflection = false
val stdDispersion = 50
val gunArray = arrayOf("D-30", "2B14", "2B11", "M252", "M224", "M6")
val entryDelimiter = ","
val lineDelimiter = ";"
const val G = 9.81
const val MAX_ANGLE = 85.0
const val MIN_ANGLE = 45.0