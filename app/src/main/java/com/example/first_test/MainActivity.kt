package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                rangeMultiplier = when (pos) {
                    0 -> 100
                    1 -> 10
                    else -> 1
                }
            }

        }
    }

    fun onClickIndirect(view: View) {
        if (mortarName == "") { //если не выбран миномет
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
        if (mortarName == "") { //если не выбран миномет
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

    fun onMortarSelected(view: View) {
        if (view is RadioButton) {
            if (view.isChecked)
            when (view.getId()) {
                R.id.dvab11Button ->
                    if (view.isChecked) {
                        mortarName = "2b11.txt"
                    }
                R.id.podnosButton ->
                    if (view.isChecked) {
                        mortarName = "podnos.txt"
                    }
                R.id.d30Button ->
                    if (view.isChecked) {
                        mortarName = "d30.txt"
                    }
                R.id.m252Button ->
                    if (view.isChecked) {
                        mortarName = "m252.txt"
                    }
            }
        }
    }
}
