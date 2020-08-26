package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_direct.*
import kotlin.math.PI
import kotlin.math.atan

class DirectActivity : AppCompatActivity() {

    private var charge = 0
    private var azimuth_correct = 0F
    private var plusCorrect = 0F
    private var elev = 0F
    private var range = 0F
    private var altDif = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct)
    }

    fun onClickCalculate(view: View) {
        range = targetRange.text.toString().toFloat()
        altDif = altDifDirect.text.toString().toFloatOrNull() ?: 0F
        azimuth_correct = (atan(1 / range) * 180 / PI * MIL).toFloat()
        if (!find(range, altDif)) Toast.makeText(applicationContext, "Unable to fire at this range!", Toast.LENGTH_SHORT).show()
        else {
            val finalArr: FloatArray = floatArrayOf(0F, azimuth_correct, charge.toFloat(), plusCorrect, elev, range, altDif)
            val intent = Intent(this, TargetActivity::class.java)
            intent.putExtra("solution", finalArr)
            startActivity(intent)
        }
    }

    fun onClickMortar(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickIndirect(view: View) {
        val intent = Intent(this, IndirectActivity::class.java)
        startActivity(intent)
    }

    private fun find(range: Float, altDif: Float): Boolean {
        var valuesMinus: List<Int>
        var valuesPlus: List<Int>
        charge = 0
        applicationContext.assets.open(mortarName).bufferedReader().use { reader ->
            var line = reader.readLine()
            var minMax = line.split(" ").map { it.toInt() }
            val maxTotal = minMax[1]
            if (range.toInt() !in minMax[0]..minMax[1]) {
                return false
            }
            line = reader.readLine()
            mainLoop@ while (line.isNotEmpty()) {
                minMax = line.split(" ").map { it.toInt() }
                if (range.toInt() in minMax[0]..minMax[1]) {
                    line = reader.readLine()
                    valuesPlus = line.split(" ").map { it.toInt() }
                    if (valuesPlus[0] == maxTotal)
                        break@mainLoop
                    valuesMinus = valuesPlus
                    while (valuesPlus[0] <= range.toInt()) {
                        line = reader.readLine()
                        valuesMinus = valuesPlus
                        valuesPlus = line.split(" ").map { it.toInt() }
                    }
                    val sol = Solution(valuesMinus[1], valuesPlus[1], valuesMinus[2], valuesPlus[2])
                    calc(sol, altDif, range)
                    return true
                } else if (range.toInt() < minMax[0]) return true
                charge++
                while (line.isNotEmpty()) {
                    val rangeTemp = line.split(" ").map { it.toInt() }
                    if (rangeTemp[0] == maxTotal)
                        break@mainLoop
                    line = reader.readLine()
                }
                line = reader.readLine()
            }
        }
        return true
    }

    private fun calc(sol: Solution, alt_dif: Float, range: Float) {
        val (elMinus, elPlus, altMinus, altPlus) = sol
        val difference: Float
        if (range % RNG_INCR == 0F)
            difference = RNG_INCR.toFloat()
        else
            difference = range % RNG_INCR
        val altCor100m = altMinus + ((altPlus - altMinus) / RNG_INCR * difference)
        val altCor = altCor100m / ALT_INCR * alt_dif
        elev = elMinus.toFloat() + ((elPlus - elMinus).toFloat() / RNG_INCR.toFloat() * difference) -
                altCor
        plusCorrect = -((elMinus - elPlus).toFloat() / RNG_INCR.toFloat())
        return
    }
}
