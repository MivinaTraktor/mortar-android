package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.first_test.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var minMax: TextView
    private lateinit var binding: ActivityMainBinding
    private lateinit var mortarX: EditText
    private lateinit var mortarY: EditText
    private lateinit var mortarAlt: EditText
    private lateinit var azOfFire: EditText
    private lateinit var initDef: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        minMax = binding.minMaxDist
        mortarX = binding.mortarX
        mortarY = binding.mortarY
        mortarAlt = binding.mortarAlt
        azOfFire = binding.azOfFire
        initDef = binding.initDef
        val spinner: Spinner = binding.digitsSpinner
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            gunArray
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.setSelection(spinnerPos)
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectMortar("")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                spinnerPos = pos
                selectMortar(parent!!.getItemAtPosition(pos).toString())
                var d = findRange(mortarData.chargeSpeeds.last(), 45.0).roundToInt()
                minMax.text = "Max: ${d}m"
                }
            }

        val checkDeflection: CheckBox = binding.checkDeflection
        checkDeflection.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.deflectionFields.visibility = View.VISIBLE
                useDeflection = true
            }
            else {
                binding.deflectionFields.visibility = View.GONE
                useDeflection = false
            }
        }

        listOf(mortarX, mortarY, mortarAlt).forEachIndexed { i, editText ->
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
                Toast.makeText(applicationContext,"Select artillery type!", Toast.LENGTH_SHORT).show()
                return
            }
        }
        listOf(binding.mortarX.text.toString(), binding.mortarY.text.toString()).forEachIndexed { i, field->
            mCoordinates[i] = if (field.isNotEmpty()) formatCoordinates(field) else null
        }
        val alt = binding.mortarAlt.text.toString()
        mCoordinates[2] = alt.toIntOrNull()
        if (useDeflection) {
            listOf(azOfFire.text.toString(), initDef.text.toString()).forEachIndexed { i, field ->
                if (field.isNotEmpty())
                    deflectionArray[i] = field.toInt()
                else {
                    Toast.makeText(
                        applicationContext,
                        "Fill in both deflection values!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
            }
        }
        val intent = Intent(this, IndirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickDirect(view: View) {
        if (mortarData.chargeSpeeds.isNullOrEmpty()) {
            Toast.makeText(applicationContext,"Select artillery type!", Toast.LENGTH_SHORT).show()
            return
        }
        listOf(mortarX.text.toString(), mortarY.text.toString(),
            mortarAlt.text.toString()).forEachIndexed { i, field->
            if (field.isNotEmpty())
                mCoordinates[i] = formatCoordinates(field) else mCoordinates[i] = null
        }
        val intent = Intent(this, DirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickClear(view: View) {
        listOf(mortarX, mortarY, mortarAlt,
            binding.azOfFire, binding.initDef).forEach {
            it.text?.clear()
        }
    }
}
