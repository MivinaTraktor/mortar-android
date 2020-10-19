package com.example.first_test

fun find(range: Float): List<Values> {
    val resultArray: MutableList<Values> = mutableListOf()
    val lowRounded = (range - (range % RNG_INCR)).toInt()
    val highRounded = lowRounded + RNG_INCR.toInt()
    mortarArray!!.forEachIndexed { charge, values ->
        if (range.toInt() in values.min..values.max) {
            when {
                lowRounded == values.min -> //значение в начале
                    resultArray.add(
                        Values(
                            charge, ChargeEntry(lowRounded, values.entry[lowRounded]!!), ChargeEntry(highRounded, values.entry[highRounded]!!),
                            ChargeEntry(highRounded + RNG_INCR.toInt(), values.entry[highRounded + RNG_INCR.toInt()]!!),
                            ChargeEntry(highRounded + RNG_INCR.toInt() * 2, values.entry[highRounded + RNG_INCR.toInt() * 2]!!)
                        )
                    )
                highRounded == values.max -> {//значение в конце
                    resultArray.add(
                        Values(
                            charge, ChargeEntry(lowRounded, values.entry[lowRounded]!!), ChargeEntry(highRounded, values.entry[highRounded]!!),
                            ChargeEntry(lowRounded - RNG_INCR.toInt() * 2, values.entry[lowRounded - RNG_INCR.toInt() * 2]!!),
                            ChargeEntry(lowRounded - RNG_INCR.toInt(), values.entry[lowRounded - RNG_INCR.toInt()]!!)
                        )
                    )
                }
                else ->
                    resultArray.add(
                        Values(
                            charge, ChargeEntry(lowRounded, values.entry[lowRounded]!!), ChargeEntry(highRounded, values.entry[highRounded]!!),
                            ChargeEntry(lowRounded - RNG_INCR.toInt(), values.entry[lowRounded - RNG_INCR.toInt()]!!),
                            ChargeEntry(highRounded + RNG_INCR.toInt(), values.entry[highRounded + RNG_INCR.toInt()]!!)
                        ))
            }
        }
    }
    return resultArray
}

fun interpolation(x: Float, ent1: Pair<Int, Int>, ent2: Pair<Int, Int>, ent3: Pair<Int, Int>, ent4: Pair<Int, Int>): Float {
    val x1 = ent1.first.toFloat()
    val x2 = ent2.first.toFloat()
    val x3 = ent3.first.toFloat()
    val x4 = ent4.first.toFloat()
    return ent1.second.toFloat() * (x - x2) * (x - x3) * (x - x4) / ((x1 - x2) * (x1 - x3) * (x1 - x4)) + ent2.second.toFloat() * (x - x1) * (x - x3) * (x - x4) / ((x2 - x1) * (x2 - x3) * (x2 - x4)) + ent3.second.toFloat() * (x - x1) * (x - x2) * (x - x4) / ((x3 - x1) * (x3 - x2) * (x3 - x4)) + ent4.second.toFloat() * (x - x1) * (x - x2) * (x - x3) / ((x4 - x1) * (x4 - x2) * (x4 - x3))
}

fun calc(sol: Values, altDif: Float, range: Float): FloatArray {
    val (charge, entLow, entHi, ent3, ent4) = sol
    val altCor100m = interpolation(range, Pair(entLow.range, entLow.entry.altCorrection), Pair(entHi.range, entHi.entry.altCorrection), Pair(ent3.range, ent3.entry.altCorrection), Pair(ent4.range, ent4.entry.altCorrection))
    val altCor = altCor100m / ALT_INCR * altDif
    val plusCorrect = (entLow.entry.elevation - entHi.entry.elevation).toFloat() / RNG_INCR
    val elev = interpolation(range, Pair(entLow.range, entLow.entry.elevation), Pair(entHi.range, entHi.entry.elevation), Pair(ent3.range, ent3.entry.elevation), Pair(ent4.range, ent4.entry.elevation)) -
            altCor
    return floatArrayOf(charge.toFloat(), plusCorrect, range, altDif, elev)
}