package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_direct.*
import kotlin.math.PI
import kotlin.math.atan

class DirectActivity : AppCompatActivity() {

    private var range = 0F
    private var altDif = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct)
    }

    fun onClickCalculate(view: View) {
        range = targetRange.text.toString().toFloat()
        altDif = altDifDirect.text.toString().toFloatOrNull() ?: 0F
        val azimuth_correct = (atan(1 / range) * 180 / PI * MIL).toFloat()
        if (range.toInt() !in mortarArray!!.first().min..mortarArray!!.last().max)
            Toast.makeText(applicationContext, "Unable to fire at this range!", Toast.LENGTH_SHORT).show()
        else {
            val solutions = find(range)
            val intent = Intent(this, TargetActivity::class.java)
            solutions.forEachIndexed { index, values ->
                val solution = calc(values, altDif, range)
                intent.putExtra("sol$index", solution)
            }
            intent.putExtra("azimuth", 0F)
            intent.putExtra("azimuthCor", azimuth_correct)
            intent.putExtra("Number of solutions", solutions.size)
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

    private fun find(range: Float): List<Values> {
        val resultArray: MutableList<Values> = mutableListOf()
        val lowRounded = (range - (range % RNG_INCR)).toInt()
        val highRounded = lowRounded + RNG_INCR.toInt()
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

    private fun calc(sol: Values, alt_dif: Float, range: Float): FloatArray {
        val (charge, elMinus, elPlus, altMinus, altPlus) = sol
        val difference = range % RNG_INCR
        val altCor100m = altMinus.toFloat() + ((altPlus - altMinus).toFloat() / RNG_INCR * difference)
        val altCor = altCor100m / ALT_INCR * alt_dif
        val plusCorrect = (elMinus - elPlus).toFloat() / RNG_INCR
        val elev = elMinus.toFloat() - (plusCorrect * difference) -
                altCor
        return floatArrayOf(charge.toFloat(), plusCorrect, range, altDif, elev)
    }
}
