package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_direct.*

class DirectActivity : AppCompatActivity() {

    private var range = 0.0
    private var altitude = 0
    private lateinit var solution: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct)
        solution = findViewById(R.id.solDir)
    }

    fun onClickCalculate(view: View) {
        range = targetRange.text.toString().toDouble()
        altitude = targetAltDirect.text.toString().toIntOrNull() ?: 0
        solution = findViewById(R.id.solDir)
        val altDif = altitude - mCoordinates[2]
        solution.text = find(range, altDif)
    }

    fun onClickMortar(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickIndirect(view: View) {
        val intent = Intent(this, IndirectActivity::class.java)
        startActivity(intent)
    }

    private fun find(range: Double, altDif: Int): String {
        var string = ""
        var valuesMinus: List<Int>
        var valuesPlus: List<Int>
        var charge = 0
        applicationContext.assets.open(mortarName).bufferedReader().use { reader ->
            var line = reader.readLine()
            var minMax = line.split(" ").map { it.toInt() }
            val maxTotal = minMax[1]
            if (range.toInt() !in minMax[0]..minMax[1])
                return "Unable to fire at this range!"
            line = reader.readLine()
            mainLoop@ while (line.isNotEmpty()) {
                minMax = line.split(" ").map { it.toInt() }
                if (range.toInt() in minMax[0]..minMax[1]) {
                    line = reader.readLine()
                    valuesPlus = line.split(" ").map { it.toInt() }
                    valuesMinus = valuesPlus
                    while (valuesPlus[0] <= range.toInt()) {
                        line = reader.readLine()
                        valuesMinus = valuesPlus
                        valuesPlus = line.split(" ").map { it.toInt() }
                    }
                    val sol = Solution(valuesMinus[1], valuesPlus[1], valuesMinus[2], valuesPlus[2])
                    string += "Charge $charge:\n"
                    string += "${calc(sol, altDif, range)}\n"
                } else if (range.toInt() < minMax[0]) return string
                charge++
                while (line.isNotEmpty()) {
                    val rangeTemp = line.split(" ").map { it.toInt() }
                    if (rangeTemp[0] == maxTotal)
                        break@mainLoop
                    line = reader.readLine()
                }
                line = reader.readLine()
            }
            return string
        }
    }
}
