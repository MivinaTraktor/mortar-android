package com.example.first_test

data class Values (val charge: Int, val entLow: ChargeEntry, val entHi: ChargeEntry, val ent3: ChargeEntry, val ent4: ChargeEntry)
data class ChargeEntry(val range: Int, val entry: Entry)
data class Entry (val elevation: Int, val altCorrection: Int)
data class Charge (val min: Int, val max: Int, val entry: Map<Int, Entry>)
data class Display (val charge: Float, val plusCorrect: Float, val range: Float, val altDif: Float, val elevation: Float)

var mCoordinates = Array(3) { 0 }
var rangeMultiplier = 100
var mortarArray: Array<Charge>? = null
val MIL = 17.777778F
val RNG_INCR = 50F
val ALT_INCR = 100F
var zeros = "Select format"
val stdDispersion = 25