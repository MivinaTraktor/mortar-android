package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.first_test.databinding.ActivityIndirectBinding

class IndirectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndirectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndirectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listOf(binding.targetX, binding.targetY, binding.targetAltIndirect).forEachIndexed { i, editText ->
            if (tCoordinates[i] != null)
                editText.setText(tCoordinates[i].toString())
            else
                editText.setText("")
        }
    }

    fun onClickMortar(view: View) {
        listOf(binding.targetX.text.toString(), binding.targetY.text.toString(), binding.targetAltIndirect.text.toString()).forEachIndexed { i, field ->
            tCoordinates[i] = if (field.isNotEmpty()) formatCoordinates(field) else null
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickDirect(view: View) {
        listOf(binding.targetX.text.toString(), binding.targetY.text.toString()).forEachIndexed { i, field ->
            tCoordinates[i] = if (field.isNotEmpty()) formatCoordinates(field) else null
        }
        val alt = binding.targetAltIndirect.text.toString()
        tCoordinates[2] = if (alt.isNotEmpty()) alt.toInt() else null
        val intent = Intent(this, DirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickCalculate(view: View) {
        listOf(binding.targetX.text.toString(), binding.targetY.text.toString(), binding.targetAltIndirect.text.toString()).forEachIndexed { i, field->
            if (field.isNotEmpty())
                tCoordinates[i] = field.toInt()
            else {
                Toast.makeText(applicationContext, "Fill in all target coordinates!", Toast.LENGTH_SHORT).show()
                return
            }
        }
        when {
            mCoordinates.contains(null) -> {
                Toast.makeText(applicationContext, "Fill in all mortar coordinates!", Toast.LENGTH_SHORT).show()
                return
            }
            useDeflection && deflectionArray.contains(null) -> {
                Toast.makeText(applicationContext, "Fill in deflection values!", Toast.LENGTH_SHORT).show()
                return
            }
        }
        val firingData = FiringData(mCoordinates.requireNoNulls(), tCoordinates.requireNoNulls())
        if (chargesList(firingData.range, firingData.altDif).isEmpty()) {
            Toast.makeText(applicationContext, "Unable to fire at this range!", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, TargetActivity::class.java)
        startActivity(intent)
    }

    fun onClickClear(view: View) {
        binding.targetX.text?.clear()
        binding.targetY.text?.clear()
        binding.targetAltIndirect.text?.clear()
        tCoordinates = MutableList(3) { null }
    }
}


