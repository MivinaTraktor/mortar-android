package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.first_test.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var minMax: TextView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        minMax = binding.minMaxDist
        val spinner: Spinner = binding.digitsSpinner
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
                binding.mortarX.hint = zeros
                binding.mortarY.hint = zeros
            }

        }

        val checkDeflection: CheckBox = binding.checkDeflection
        checkDeflection.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.deflectionFields.visibility = View.VISIBLE
                useDeflection = true
            }
            else {
                binding.deflectionFields.visibility = View.GONE
                useDeflection = false
            }
        }

        listOf(100, 10, 1).forEachIndexed { i, item ->
            if (rangeMultiplier == item) spinner.setSelection(i)
        }
        listOf(binding.mortarX, binding.mortarY, binding.mortarAlt).forEachIndexed { i, editText ->
            if (mCoordinates[i] != null)
                editText.setText(mCoordinates[i].toString())
            else
                editText.setText("")
        }
        listOf(binding.azOfFire, binding.initDef).forEachIndexed { i, editText ->
            if (deflectionArray[i] != null)
                editText.setText(deflectionArray[i].toString())
            else
                editText.setText("")
        }
    }

    fun onClickIndirect(view: View) {
        when { // ошибки
            mortarData.chargeSpeeds.isNullOrEmpty() -> {
                Toast.makeText(applicationContext,"Select a mortar!", Toast.LENGTH_SHORT).show()
                return
            }
            binding.mortarX.text.toString().length > zeros.length || binding.mortarY.text.toString().length > zeros.length -> {
                Toast.makeText(applicationContext,"Incorrect coordinate format!", Toast.LENGTH_SHORT).show()
                return
            }
        }
        listOf(binding.mortarX.text.toString(), binding.mortarY.text.toString(), binding.mortarAlt.text.toString()).forEachIndexed { i, field->
            mCoordinates[i] = if (field.isNotEmpty()) field.toInt() else null
        }
        listOf(binding.azOfFire.text.toString(), binding.initDef.text.toString()).forEachIndexed { i, field ->
            deflectionArray[i] = if (field.isNotEmpty()) field.toInt() else null
        }
        val intent = Intent(this, IndirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickDirect(view: View) {
        if (mortarData.chargeSpeeds.isNullOrEmpty()) {
            Toast.makeText(applicationContext,"Select a mortar!", Toast.LENGTH_SHORT).show()
            return
        }
        listOf(binding.mortarX.text.toString(), binding.mortarY.text.toString(), binding.mortarAlt.text.toString()).forEachIndexed { i, field->
            if (field.isNotEmpty()) mCoordinates[i] = field.toInt() else mCoordinates[i] = null
        }
        val intent = Intent(this, DirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickClear(view: View) {
        binding.mortarX.text?.clear()
        binding.mortarY.text?.clear()
        binding.mortarAlt.text?.clear()
        binding.azOfFire.text?.clear()
        binding.initDef.text?.clear()
        mCoordinates = MutableList(3) { null }
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
                    R.id.butm224 ->
                        if (view.isChecked) {
                            selectMortar("m224")
                        }
                }
                var d = findRange(mortarData.chargeSpeeds.last(), 45.0).roundToInt()
                minMax.text = "Max: ${d}m"
            }
        }
    }
}
