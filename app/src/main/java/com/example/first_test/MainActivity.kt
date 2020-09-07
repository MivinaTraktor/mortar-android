package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var xLabel: EditText
    private lateinit var yLabel: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        xLabel = findViewById(R.id.mortarX)
        yLabel = findViewById(R.id.mortarY)
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
                xLabel.hint = zeros
                yLabel.hint = zeros
            }

        }
    }

    fun onClickIndirect(view: View) {
        if (mortarX.text.toString().isEmpty() || mortarY.text.toString().isEmpty() || mortarAlt.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        if (mortarArray == null) { //если не выбран миномет
            Toast.makeText(applicationContext,"Select a mortar!", Toast.LENGTH_SHORT).show()
            return
        }
        val fieldsList = listOf(mortarX.text.toString(), mortarY.text.toString(), mortarAlt.text.toString())
        fieldsList.forEachIndexed { i, field->
            if (field.isNotEmpty()) mCoordinates[i] = field.toInt() else mCoordinates[i] = 0
        }
        val intent = Intent(this, IndirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickDirect(view: View) {
        if (mortarArray == null) { //если не выбран миномет
            Toast.makeText(applicationContext,"Select a mortar!", Toast.LENGTH_SHORT).show()
            return
        }
        val fieldsList = listOf(mortarX.text.toString(), mortarY.text.toString(), mortarAlt.text.toString())
        fieldsList.forEachIndexed { i, field->
            if (field.isNotEmpty()) mCoordinates[i] = field.toInt() else mCoordinates[i] = 0
        }
        val intent = Intent(this, DirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickClear(view: View) {
        mortarX.text?.clear()
        mortarY.text?.clear()
        mortarAlt.text?.clear()
    }

    fun onMortarSelected(view: View) {
        if (view is RadioButton) {
            if (view.isChecked)
            when (view.getId()) {
                R.id.dvab11Button ->
                    if (view.isChecked) {
                        selectMortar("2b11")
                    }
                R.id.podnosButton ->
                    if (view.isChecked) {
                        selectMortar("2b14")
                    }
                R.id.d30Button ->
                    if (view.isChecked) {
                        selectMortar("d30")
                    }
                R.id.m252Button ->
                    if (view.isChecked) {
                        selectMortar("m252")
                    }
            }
        }
    }
}
