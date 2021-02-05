package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_indirect.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.hypot

class IndirectActivity : AppCompatActivity() {

    private var azimuth_mil = 0F
    private var azimuth_correct = 0F
    private var range = 0F
    private var altDif = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indirect)
        targetX.hint = zeros
        targetY.hint = zeros
        listOf(targetX, targetY, targetAltIndirect).forEachIndexed { i, editText ->
            if (tCoordinates[i] != null)
                editText.setText(tCoordinates[i].toString())
            else
                editText.setText("")
        }
    }

    fun onClickMortar(view: View) {
        listOf(targetX.text.toString(), targetY.text.toString(), targetAltIndirect.text.toString()).forEachIndexed { i, field ->
            if (field.isNotEmpty())
                tCoordinates[i] = field.toInt()
            else
                tCoordinates[i] = null
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickDirect(view: View) {
        listOf(targetX.text.toString(), targetY.text.toString(), targetAltIndirect.text.toString()).forEachIndexed { i, field ->
            if (field.isNotEmpty())
                tCoordinates[i] = field.toInt()
            else
                tCoordinates[i] = null
        }
        val intent = Intent(this, DirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickCalculate(view: View) {
        listOf(targetX.text.toString(), targetY.text.toString(), targetAltIndirect.text.toString()).forEachIndexed { i, field->
            if (field.isNotEmpty())
                tCoordinates[i] = field.toInt()
            else {
                Toast.makeText(applicationContext, "Fill in all target coordinates", Toast.LENGTH_SHORT).show()
                return
            }
        }
        when {
            targetX.text.toString().length > zeros.length || targetY.text.toString().length > zeros.length -> {
                Toast.makeText(applicationContext,"Incorrect coordinate format!", Toast.LENGTH_SHORT).show()
                return
            }
            mCoordinates.contains(null) -> {
                Toast.makeText(applicationContext, "Fill in all mortar coordinates", Toast.LENGTH_SHORT).show()
                return
            }
        }
        target()
        if (range.toInt() !in mortarArray!!.first().min..mortarArray!!.last().max) {
            Toast.makeText(applicationContext, "Unable to fire at this range!", Toast.LENGTH_SHORT).show()
            return
        }
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

    fun onClickClear(view: View) {
        targetX.text?.clear()
        targetY.text?.clear()
        targetAltIndirect.text?.clear()
        tCoordinates = Array(3) { null }
    }

    private fun target() {
        val x = mCoordinates[0]!!
        val y = mCoordinates[1]!!
        val alt = mCoordinates[2]!!
        val tgtX = tCoordinates[0]!!
        val tgtY = tCoordinates[1]!!
        val tgtAlt = tCoordinates[2]!!
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


