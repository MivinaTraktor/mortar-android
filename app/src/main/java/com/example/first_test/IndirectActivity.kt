package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_indirect.*
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.hypot
import kotlin.math.roundToInt

class IndirectActivity : AppCompatActivity() {

    private var targetArr = Array(3) { 0 }
    private lateinit var solution: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indirect)
        solution = findViewById(R.id.solIndir)
    }

    fun onClickMortar(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickDirect(view: View) {
        val intent = Intent(this, DirectActivity::class.java)
        startActivity(intent)
    }

    fun onClickCalculate(view: View) {
        targetArr[0] = targetX.text.toString().toIntOrNull() ?: 0
        targetArr[1] = targetY.text.toString().toIntOrNull() ?: 0
        targetArr[2] = targetAltIndirect.text.toString().toIntOrNull() ?: 0
        solution.text = target(mCoordinates, targetArr)
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
                    if (valuesPlus[0] == maxTotal)
                        break@mainLoop
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

    private fun target (mCoordinates: Array<Int>, tCoordinates: Array<Int>): String {
        var string = ""
        val x = mCoordinates[0]
        val y = mCoordinates[1]
        val alt = mCoordinates[2]
        val tgtX = tCoordinates[0]
        val tgtY = tCoordinates[1]
        val tgtAlt = tCoordinates[2]
        val altDif = tgtAlt - alt
        val range = hypot(((tgtX - x).toDouble()), ((tgtY - y).toDouble())) * 10
        val angle: Float
        if ((tgtX - x) == 0) {
            if ((tgtY - y) > 0)
                angle = 0F
            else
                angle = 180F
        }
        else angle = (atan(((tgtY - y).toDouble() / (tgtX - x).toDouble())) * 180 / PI).toFloat()
        val azimuth: Float
        if ((tgtX - x) <= 0)
            azimuth = 270 - angle
        else
            azimuth = 90 - angle
        val azimuth_mil = (azimuth * MIL).toInt()
        val azimuth_correct = atan(1 / range) * 180 / PI * MIL
        lastAz = azimuth_mil.toDouble()
        azCor = azimuth_correct * HI
        string += "Range = ${range.roundToInt()}\n"
        string += "Azimuth = ${"%.1f".format(azimuth)} / $azimuth_mil\n"
        //print("${LOW}m-> = +${((azimuth_correct * LOW).toInt())}\t")
        //print("${MED}m-> = +${((azimuth_correct * MED).toInt())}\t")
        //println("${HI}m-> = +${((azimuth_correct * HI).toInt())}")
        string += "Altitude difference = $altDif\n\n"
        string += find(range, altDif)
        return string
    }
}
