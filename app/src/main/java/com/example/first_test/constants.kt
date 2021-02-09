package com.example.first_test

data class ChargeSolution(val charge: Int, val elevation: Double, val elevation360: Double)

var mCoordinates: Array<Int?> = Array(3) { null }
var tCoordinates: Array<Int?> = Array(3) { null }
var rangeMultiplier = 100
var mortarCharges: Array<Double>? = null
var muzzleVelocity: Double? = null
const val MIL = 17.777778
var zeros = "Select format"
val stdDispersion = 25
const val G = 9.81
const val MAX_ANGLE = 85.0
const val MIN_ANGLE = 45.0