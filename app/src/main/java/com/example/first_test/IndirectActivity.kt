package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_indirect.*

class IndirectActivity : AppCompatActivity() {

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
        val targetCalculated = target(mCoordinates.requireNoNulls(), tCoordinates.requireNoNulls())
        if (chargesList(targetCalculated).isEmpty()) {
            Toast.makeText(applicationContext, "Unable to fire at this range!", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, TargetActivity::class.java)
        startActivity(intent)
    }

    fun onClickClear(view: View) {
        targetX.text?.clear()
        targetY.text?.clear()
        targetAltIndirect.text?.clear()
        tCoordinates = Array(3) { null }
    }
}


