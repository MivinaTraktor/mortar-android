package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.VectorEnabledTintResources
import kotlinx.android.synthetic.main.activity_indirect.*
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.hypot

class IndirectActivity : AppCompatActivity() {

    private var targetArr = IntArray(3)
    private var azimuth_mil = 0F
    private var azimuth_correct = 0F
    private var charge = 0
    private var plusCorrect = 0F
    private var elev = 0F
    private var range = 0F
    private var altDif = 0F
    private lateinit var xLabel: EditText
    private lateinit var yLabel: EditText
    private lateinit var altLabel: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indirect)
        xLabel = findViewById(R.id.targetX)
        yLabel = findViewById(R.id.targetY)
        altLabel = findViewById(R.id.targetAltIndirect)
        xLabel.hint = zeros
        yLabel.hint = zeros
    }

    fun onClickMortar(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickDirect(view: View) {
        val intent = Intent(this, DirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickCalculate(view: View) {
        targetArr[0] = targetX.text.toString().toIntOrNull() ?: 0
        targetArr[1] = targetY.text.toString().toIntOrNull() ?: 0
        targetArr[2] = targetAltIndirect.text.toString().toIntOrNull() ?: 0
        if (!target(mCoordinates, targetArr)) Toast.makeText(applicationContext, "Unable to fire at this range!", Toast.LENGTH_SHORT).show()
        else {
            val finalArr: FloatArray = floatArrayOf(azimuth_mil, azimuth_correct, charge.toFloat(), plusCorrect, elev, range, altDif)
            val intent = Intent(this, TargetActivity::class.java)
            intent.putExtra("solution", finalArr)
            startActivity(intent)
        }
    }

    fun onClickClear(view: View) {
        xLabel.text?.clear()
        yLabel.text?.clear()
        altLabel.text?.clear()
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

    private fun target (mCoordinates: Array<Int>, tCoordinates: IntArray): Boolean {
        val x = mCoordinates[0]
        val y = mCoordinates[1]
        val alt = mCoordinates[2]
        val tgtX = tCoordinates[0]
        val tgtY = tCoordinates[1]
        val tgtAlt = tCoordinates[2]
        altDif = (tgtAlt - alt).toFloat()
        range = (hypot(((tgtX - x).toDouble()), ((tgtY - y).toDouble())) * rangeMultiplier).toFloat()
        val angle: Float
        if ((tgtX - x) == 0) {
            if ((tgtY - y) > 0)
                angle = 0F
            else
                angle = 180F
        }
        else angle = (atan(((tgtY - y).toDouble() / (tgtX - x).toDouble())) * 180 / PI).toFloat()
        val azimuth: Float
        if ((tgtX - x) <= 0)
            azimuth = 270 - angle
        else
            azimuth = 90 - angle
        azimuth_mil = (azimuth * MIL)
        azimuth_correct = (atan(1 / range) * 180 / PI * MIL).toFloat()
        return find(range, altDif)
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
