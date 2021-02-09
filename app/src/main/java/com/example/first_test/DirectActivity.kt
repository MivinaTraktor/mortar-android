package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_direct.*

class DirectActivity : AppCompatActivity() {

    private var range = 0.0
    private var altDif = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct)
    }

    fun onClickCalculate(view: View) {
        range = targetRange.text.toString().toDouble()
        altDif = altDifDirect.text.toString().toDoubleOrNull() ?: 0.0
        calcCoordinates(mCoordinates.requireNoNulls(), range, 0.0, altDif)
        val targetCalculated = target(mCoordinates.requireNoNulls(), tCoordinates.requireNoNulls())
        if (chargesList(targetCalculated).isEmpty()) {
            Toast.makeText(applicationContext, "Unable to fire at this range!", Toast.LENGTH_SHORT).show()
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
