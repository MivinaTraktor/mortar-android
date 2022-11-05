package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.first_test.databinding.ActivityDirectBinding

class DirectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDirectBinding
    private lateinit var range: EditText
    private lateinit var altDif: EditText
    private lateinit var azimuth: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        range = binding.range
        altDif = binding.altDif
        azimuth = binding.range
    }

    fun onClickCalculate(view: View) {
        if (binding.range.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "No range!", Toast.LENGTH_SHORT).show()
            return
        }
        calcCoordinates(mCoordinates.requireNoNulls(),
            range.text.toString().toDouble(),
            azimuth.text.toString().toDoubleOrNull() ?: 0.0,
            altDif.text.toString().toDoubleOrNull() ?: 0.0)
        val firingData = FiringData(mCoordinates.requireNoNulls(), tCoordinates.requireNoNulls())
        if (chargesList(firingData.range, firingData.altDif).isEmpty()) {
            Toast.makeText(applicationContext, "Invalid range!", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, TargetActivity::class.java)
        startActivity(intent)
    }

    fun onClickMortar(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickIndirect(view: View) {
        val intent = Intent(this, IndirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickFillIn(view: View) {
        if (range.text.isNotEmpty() && azimuth.text.isNotEmpty()) {
            calcCoordinates(mCoordinates.requireNoNulls(),
                range.text.toString().toDouble(),
                azimuth.text.toString().toDoubleOrNull() ?: 0.0,
                0.0)
            altDif.setText(
                    heightInterpolation(
                            x = tCoordinates[0]!!,
                            y = tCoordinates[1]!!
                    ).toInt() - mCoordinates[2]!!
            )
        }

    }
}
