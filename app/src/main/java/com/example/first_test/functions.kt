package com.example.first_test

import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.hypot
import kotlin.math.roundToInt

data class Solution constructor(val elMinus: Int, val elPlus: Int, val altMinus: Int, val altPlus: Int)

fun target (mCoordinates: Array<Int>, tCoordinates: Array<Int>) {
    var x = mCoordinates[0]
    var y = mCoordinates[1]
    var alt = mCoordinates[2]
    var tgtX = tCoordinates[0]
    var tgtY = tCoordinates[1]
    var tgtAlt = tCoordinates[2]
    val altDif = tgtAlt - alt
    val range = hypot(((tgtX - x).toDouble()), ((tgtY - y).toDouble())) * 10
    var angle: Float
    if ((tgtX - x) == 0) {
        if ((tgtY - y) > 0)
            angle = 0F
        else
            angle = 180F
    }
    else angle = (atan(((tgtY - y).toDouble() / (tgtX - x).toDouble())) * 180 / PI).toFloat()
    var azimuth: Float
    if ((tgtX - x) <= 0)
        azimuth = 270 - angle
    else
        azimuth = 90 - angle
    var azimuth_mil = (azimuth * MIL).toInt()
    var azimuth_correct = atan(1 / range) * 180 / PI * MIL
    lastAz = azimuth_mil.toDouble()
    azCor = azimuth_correct * HI
    println("Range = ${range.roundToInt()}")
    print("Azimuth = ${"%.1f".format(azimuth)} / $azimuth_mil\t")
    print("${LOW}m-> = +${((azimuth_correct * LOW).toInt())}\t")
    print("${MED}m-> = +${((azimuth_correct * MED).toInt())}\t")
    println("${HI}m-> = +${((azimuth_correct * HI).toInt())}")
    println("Altitude difference = $altDif")
    selectTable(range, altDif)
}

fun selectTable(range: Double, alt_dif: Int) {
    var charge = 0
    var file = File(mortarFolder + "ch0.txt")
    if (!file.exists())
        println("No file!")
    while (file.exists()) {
        calc(find(file.toString(), range), alt_dif, range, charge)
        charge++
        file = File(mortarFolder + "ch$charge.txt")
    }
}

fun find(fileName: String, range: Double): Solution {
    var elMinus = 0
    var elPlus = 0
    var altMinus = 0
    var altPlus = 0
    var fr = FileReader(fileName)
    var scan = Scanner(fr)
    if (range.toInt() in scan.nextInt()..(scan.nextInt() + RNG_INCR)){
        loop@ do {
            if (scan.nextInt() >= (range - RNG_INCR)) {
                elMinus = scan.nextInt()
                altMinus = scan.nextInt()
                scan.nextLine()
                scan.nextInt()
                elPlus = scan.nextInt()
                altPlus = scan.nextInt()
                scan.close()
                fr.close()
                break@loop
            }
            scan.nextLine()
        } while (scan.hasNextLine())
    }
    val sol = Solution(elMinus, elPlus, altMinus, altPlus)
    return sol
}

fun calc(sol: Solution, alt_dif: Int, range: Double, charge: Int) {
    val (elMinus, elPlus, altMinus, altPlus) = sol
    if (elMinus == 0){
        return
    }
    println("Charge $charge:")
    val difference: Double
    if (range % RNG_INCR == 0.0)
        difference = RNG_INCR.toDouble()
    else
        difference = range % RNG_INCR
    val altCor100m = altMinus + ((altPlus - altMinus) / RNG_INCR * difference)
    val altCor = altCor100m / ALT_INCR * alt_dif
    val elev: Float = elMinus.toFloat() + ((elPlus - elMinus).toFloat() / RNG_INCR.toFloat() * difference.toFloat()) -
            altCor.toFloat()
    val plusCorrect: Float = (elMinus - elPlus).toFloat() / RNG_INCR.toFloat()
    lastElev = elev
    elCor = plusCorrect * HI
    print("Elevation = ${"%.1f".format(elev)}\t")
    print("+${LOW}m = -${"%.1f".format(plusCorrect * LOW)}\t")
    print("+${MED}m = -${"%.1f".format(plusCorrect * MED)}\t")
    println("+${HI}m = -${"%.1f".format(plusCorrect * HI)}")
}

fun dispersion() {
    if (lastElev == 0F) {
        println("Select a target first!")
        return
    }
    print("Enter desired dispersion in m:")
    val dispM = readLine()!!.toInt()
    val azDisp = azCor / HI.toDouble() * dispM.toDouble()
    val elDisp = elCor / HI * dispM
    println("Dispersion for max charge:")
    println("\t${(lastAz - azDisp).toInt()} ${lastAz.toInt()} ${(lastAz + azDisp).toInt()}")
    println("${(lastElev - elDisp).toInt()}")
    println("${lastElev.toInt()}")
    println("${(lastElev + elDisp).toInt()}")
}

fun resetValues() {
    lastElev = 0F
    lastAz = 0.0
    elCor = 0F
    azCor = 0.0
}