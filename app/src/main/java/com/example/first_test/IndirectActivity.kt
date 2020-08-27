package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_indirect.*
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.hypot

class IndirectActivity : AppCompatActivity() {

    private var azimuth_mil = 0F
    private var azimuth_correct = 0F
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
        val targetArr = IntArray(3)
        targetArr[0] = targetX.text.toString().toIntOrNull() ?: 0
        targetArr[1] = targetY.text.toString().toIntOrNull() ?: 0
        targetArr[2] = targetAltIndirect.text.toString().toIntOrNull() ?: 0
        target(mCoordinates, targetArr)
        if (range.toInt() !in mortarArray!!.first().min..mortarArray!!.last().max)
            Toast.makeText(applicationContext, "Unable to fire at this range!", Toast.LENGTH_SHORT).show()
        else {
            val solutions = find(range)
            val intent = Intent(this, TargetActivity::class.java)
            solutions.forEachIndexed { index, values ->
                val solution = calc(values, altDif, range)
                intent.putExtra("sol$index", solution)
            }
            intent.putExtra("azimuth", azimuth_mil)
            intent.putExtra("azimuthCor", azimuth_correct)
            intent.putExtra("Number of solutions", solutions.size)
            startActivity(intent)
        }
    }

    fun onClickClear(view: View) {
        xLabel.text?.clear()
        yLabel.text?.clear()
        altLabel.text?.clear()
    }

    private fun find(range: Float): List<Values> {
        val resultArray: MutableList<Values> = mutableListOf()
        val lowRounded = (range - (range % RNG_INCR)).toInt()
        val highRounded = lowRounded + RNG_INCR
        mortarArray!!.forEachIndexed { charge, values ->
            if (range.toInt() in values.min..values.max) {
                if (highRounded in values.min..values.max) {
                    resultArray.add(
                        Values(
                            charge, values.valArray[lowRounded]!!.elevation, values.valArray[highRounded]!!.elevation,
                            values.valArray[lowRounded]!!.altCorrection, values.valArray[highRounded]!!.altCorrection
                        )
                    )
                } else {
                    resultArray.add(
                        Values(
                            charge, values.valArray[lowRounded]!!.elevation, values.valArray[lowRounded]!!.elevation,
                            values.valArray[lowRounded]!!.altCorrection, values.valArray[lowRounded]!!.altCorrection
                        ))
                }
            }
        }
        return resultArray
    }

    private fun target (mCoordinates: Array<Int>, tCoordinates: IntArray) {
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
        azimuth_mil = azimuth * MIL
        azimuth_correct = (atan(1 / range) * 180 / PI * MIL).toFloat()
    }

    private fun calc(sol: Values, alt_dif: Float, range: Float): FloatArray {
        val (charge, elMinus, elPlus, altMinus, altPlus) = sol
        val difference: Float
        if (range % RNG_INCR == 0F)
            difference = RNG_INCR.toFloat()
        else
            difference = range % RNG_INCR
        val altCor100m = altMinus + ((altPlus - altMinus) / RNG_INCR * difference)
        val altCor = altCor100m / ALT_INCR * alt_dif
        val elev = elMinus.toFloat() + ((elPlus - elMinus).toFloat() / RNG_INCR.toFloat() * difference) -
                altCor
        val plusCorrect = (elMinus - elPlus).toFloat() / RNG_INCR.toFloat()
        return floatArrayOf(charge.toFloat(), plusCorrect, range, altDif, elev)
    }
}


