package com.example.first_test

data class Solution constructor(val elMinus: Int, val elPlus: Int, val altMinus: Int, val altPlus: Int)

fun calc(sol: Solution, alt_dif: Int, range: Double): String {
    var string = ""
    val (elMinus, elPlus, altMinus, altPlus) = sol
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
    string += "Elevation = ${"%.1f".format(elev)}\n"
    //string += "+${LOW}m = -${"%.1f".format(plusCorrect * LOW)}\t"
    //string += "+${MED}m = -${"%.1f".format(plusCorrect * MED)}\t"
    //string += "+${HI}m = -${"%.1f".format(plusCorrect * HI)}"
    resetValues()
    return string
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