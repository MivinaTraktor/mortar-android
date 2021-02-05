package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_direct.*
import kotlin.math.PI
import kotlin.math.atan

class DirectActivity : AppCompatActivity() {

    private var range = 0F
    private var altDif = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct)
    }

    fun onClickCalculate(view: View) {
        range = targetRange.text.toString().toFloat()
        altDif = altDifDirect.text.toString().toFloatOrNull() ?: 0F
        val azimuth_correct = (atan(1 / range) * 180 / PI * MIL).toFloat()
        if (range.toInt() !in mortarArray!!.first().min..mortarArray!!.last().max) {
            Toast.makeText(applicationContext, "Unable to fire at this range!", Toast.LENGTH_SHORT).show()
            return
        }
        val solutions = find(range)
        val intent = Intent(this, TargetActivity::class.java)
        solutions.forEachIndexed { index, values ->
            val solution = calc(values, altDif, range)
            intent.putExtra("sol$index", solution)
        }
        intent.putExtra("azimuth", 0F)
        intent.putExtra("azimuthCor", azimuth_correct)
        intent.putExtra("Number of solutions", solutions.size)
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
