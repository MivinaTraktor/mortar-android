package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.first_test.databinding.ActivityIndirectBinding

class IndirectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndirectBinding
    private lateinit var targetX: EditText
    private lateinit var targetY: EditText
    private lateinit var targetAlt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndirectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        targetX = binding.targetX
        targetY = binding.targetY
        targetAlt = binding.targetAlt
        listOf(targetX, targetY, targetAlt).forEachIndexed { i, editText ->
            if (tCoordinates[i] != null)
                editText.setText(tCoordinates[i].toString())
            else
                editText.setText("")
        }
    }

    fun onClickMortar(view: View) {
        listOf(targetX.text.toString(), targetY.text.toString(), targetAlt.text.toString()).forEachIndexed { i, field ->
            tCoordinates[i] = if (field.isNotEmpty()) formatCoordinates(field) else null
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickDirect(view: View) {
        listOf(targetX.text.toString(), targetY.text.toString()).forEachIndexed { i, field ->
            tCoordinates[i] = if (field.isNotEmpty()) formatCoordinates(field) else null
        }
        val alt = targetAlt.text.toString()
        tCoordinates[2] = alt.toIntOrNull()
        val intent = Intent(this, DirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickCalculate(view: View) {
        listOf(targetX.text.toString(), targetY.text.toString()).forEachIndexed { i, field->
            tCoordinates[i] = if (field.isNotEmpty()) formatCoordinates(field) else null
        }
        val alt = targetAlt.text.toString()
        tCoordinates[2] = alt.toIntOrNull()
        when {
            mCoordinates.contains(null) -> {
                Toast.makeText(applicationContext, "Fill in all mortar coordinates!", Toast.LENGTH_SHORT).show()
                return
            }
            tCoordinates.contains(null) -> {
                Toast.makeText(applicationContext, "Fill in all target coordinates!", Toast.LENGTH_SHORT).show()
                return
            }
        }
        val firingData = FiringData(mCoordinates.requireNoNulls(), tCoordinates.requireNoNulls())
        if (chargesList(firingData.range, firingData.altDif).isEmpty()) {
            Toast.makeText(applicationContext, "Invalid range!", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, TargetActivity::class.java)
        startActivity(intent)
    }

    fun onClickClear(view: View) {
        targetX.text?.clear()
        targetY.text?.clear()
        targetAlt.text?.clear()
    }
}


