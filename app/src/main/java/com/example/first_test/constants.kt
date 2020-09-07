package com.example.first_test

data class Values (val charge: Int, val elMinus: Int, val elPlus: Int, val altMinus: Int, val altPlus: Int)
data class Entry (val elevation: Int, val altCorrection: Int)
data class Charge (val min: Int, val max: Int, val valArray: Map<Int, Entry>)
data class Display (val charge: Float, val plusCorrect: Float, val range: Float, val altDif: Float, val elevation: Float)

var mCoordinates = Array(3) { 0 }
var rangeMultiplier = 100
var lastElev = 0F
var lastAz = 0.0
var elCor = 0F
var azCor = 0.0
var mortarArray: Array<Charge>? = null
val MIL = 17.777778F
val RNG_INCR = 50F
val ALT_INCR = 100F
const val HI = 50
var zeros = "Select format"
val stdDispersion = 25