package com.example.first_test

data class ChargePair(val charge: Int, val hi: Double, val lo: Double)

var mCoordinates: Array<Int?> = Array(3) { null }
var tCoordinates: Array<Int?> = Array(3) { null }
var deflectionArray: Array<Int?> = Array(3) { null }
var rangeMultiplier = 100
var mortarCharges: Array<Double>? = null
var specialCharges: Map<Int, String>? = null
var muzzleVelocity: Double? = null
var artDegree = 6400.0
var zeros = "Select format"
var useDeflection = false
val stdDispersion = 25
const val G = 9.81
const val MAX_ANGLE = 85.0
const val MIN_ANGLE = 45.0