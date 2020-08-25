package com.example.first_test

data class Solution constructor(val elMinus: Int, val elPlus: Int, val altMinus: Int, val altPlus: Int)

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