package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var minMax: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        minMax = findViewById(R.id.minMaxDist)
        val spinner: Spinner = findViewById(R.id.digitsSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.digitsArray,
            android.R.layout.simple_spinner_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "Select format of coordinates", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when (pos) {
                    0 -> {
                        rangeMultiplier = 100
                        zeros = "000"
                    }
                    1 -> {
                        rangeMultiplier = 10
                        zeros = "0000"
                    }
                    else -> {
                        rangeMultiplier = 1
                        zeros = "00000"
                    }
                }
                mortarX.hint = zeros
                mortarY.hint = zeros
            }

        }
        listOf(100, 10, 1).forEachIndexed { i, item ->
            if (rangeMultiplier == item) spinner.setSelection(i)
        }
        listOf(mortarX, mortarY, mortarAlt).forEachIndexed { i, editText ->
            if (mCoordinates[i] != null)
                editText.setText(mCoordinates[i].toString())
            else
                editText.setText("")
        }
    }

    fun onClickIndirect(view: View) {
        when { // ошибки
            mortarCharges == null -> {
                Toast.makeText(applicationContext,"Select a mortar!", Toast.LENGTH_SHORT).show()
                return
            }
            mortarX.text.toString().length > zeros.length || mortarY.text.toString().length > zeros.length -> {
                Toast.makeText(applicationContext,"Incorrect coordinate format!", Toast.LENGTH_SHORT).show()
                return
            }
        }
        listOf(mortarX.text.toString(), mortarY.text.toString(), mortarAlt.text.toString()).forEachIndexed { i, field->
            if (field.isNotEmpty()) mCoordinates[i] = field.toInt() else mCoordinates[i] = null
        }
        val intent = Intent(this, IndirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickDirect(view: View) {
        if (mortarCharges == null) {
            Toast.makeText(applicationContext,"Select a mortar!", Toast.LENGTH_SHORT).show()
            return
        }
        listOf(mortarX.text.toString(), mortarY.text.toString(), mortarAlt.text.toString()).forEachIndexed { i, field->
            if (field.isNotEmpty()) mCoordinates[i] = field.toInt() else mCoordinates[i] = null
        }
        val intent = Intent(this, DirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickClear(view: View) {
        mortarX.text?.clear()
        mortarY.text?.clear()
        mortarAlt.text?.clear()
        mCoordinates = Array(3) { null }
    }

    fun onMortarSelected(view: View) {
        if (view is RadioButton) {
            if (view.isChecked) {
                when (view.getId()) {
                    R.id.but2b11 ->
                        if (view.isChecked) {
                            selectMortar("2b11")
                        }
                    R.id.but2b14 ->
                        if (view.isChecked) {
                            selectMortar("2b14")
                        }
                    R.id.butd30 ->
                        if (view.isChecked) {
                            selectMortar("d30")
                        }
                    R.id.butm252 ->
                        if (view.isChecked) {
                            selectMortar("m252")
                        }
                }
                minMax.visibility = View.VISIBLE
                minMax.text = "   -   %.1fm".format(findRange(mortarCharges!!.last() * muzzleVelocity!!, 45.0))
            }
        }
    }
}
