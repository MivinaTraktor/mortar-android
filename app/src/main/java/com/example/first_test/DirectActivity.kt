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
    private var range = 0.0
    private var altDif = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClickCalculate(view: View) {
        if (binding.range.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "No range!", Toast.LENGTH_SHORT).show()
            return
        }
        range = binding.range.text.toString().toDouble()
        altDif = binding.altDif.text.toString().toDoubleOrNull() ?: 0.0
        calcCoordinates(mCoordinates.requireNoNulls(), range, 0.0, altDif)
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
}
