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
}


