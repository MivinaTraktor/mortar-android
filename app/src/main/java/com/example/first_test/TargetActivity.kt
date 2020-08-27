package com.example.first_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

class TargetActivity : AppCompatActivity() {

    private lateinit var fieldAz: TextView
    private lateinit var fieldEl: TextView
    private lateinit var fieldCh: TextView
    private lateinit var enterLeftRight: TextInputEditText
    private lateinit var enterFwdBack: TextInputEditText
    private lateinit var fieldRng: TextView
    private lateinit var fieldAltDif: TextView
    private var elevation = 0F
    private var elNew = 0F
    private var azimuth = 0
    private var azNew = 0
    private var fwdBack = 0
    private var leftRight = 0
    private var azCor = 0F
    private var elCor = 0F
    private var solNumber = 0
    private var currentNumber = 0
    private lateinit var currentSolution: Display
    private val solutionList: MutableList<Display> = mutableListOf()

    fun onClickReset(view: View) {
        clearFields()
        elNew = elevation
        azNew = azimuth
        displaySolution()
    }

    fun onClickCorrectOk(view: View) {
        fwdBack = enterFwdBack.text.toString().toIntOrNull() ?: 0
        leftRight = enterLeftRight.text.toString().toIntOrNull() ?: 0
        elNew = elevation - (elCor * fwdBack)
        azNew = azimuth + (azCor * leftRight).toInt()
        if (azNew < 0) azNew += 6400
        else if (azNew > 6400) azNew -= 6400
        fieldEl.text = "%.1f".format(elNew)
        fieldAz.text = azNew.toString()
        enterLeftRight.clearFocus()
        enterFwdBack.clearFocus()
    }

    fun onClickAnchor(view: View) {
        elevation = elNew
        azimuth = azNew
        clearFields()
    }

    fun onClickPlus(view: View) {
        if (currentNumber != solNumber - 1) {
            currentNumber++
            currentSolution = solutionList[currentNumber]
            updateValues()
            displaySolution()
        }
    }

    fun onClickMinus(view: View) {
        if (currentNumber != 0) {
            currentNumber--
            currentSolution = solutionList[currentNumber]
            updateValues()
            displaySolution()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
        fieldAz = findViewById(R.id.textViewAz)
        fieldEl = findViewById(R.id.textViewEl)
        fieldCh = findViewById(R.id.textViewCh)
        enterLeftRight = findViewById(R.id.fieldLeftRight)
        enterFwdBack = findViewById(R.id.fieldFwdBack)
        fieldRng = findViewById(R.id.rngLabel)
        fieldAltDif = findViewById(R.id.altDifLabel)
        solNumber = intent.getIntExtra("Number of solutions", 0)
        for (i in 0 until solNumber) {
            val solution = intent.getFloatArrayExtra("sol$i")!!
            solutionList.add(Display(solution[0], solution[1], solution[2], solution[3], solution[4]))
        }
        currentSolution = solutionList[0]
        updateValues()
        azimuth = intent.getFloatExtra("azimuth", 0F).toInt()
        azCor = intent.getFloatExtra("azimuthCor", 0F)
        fieldAltDif.text = currentSolution.altDif.toInt().toString()
        fieldRng.text = currentSolution.range.toInt().toString()
        displaySolution()
    }
    fun clearFields() {
        enterFwdBack.text?.clear()
        enterLeftRight.text?.clear()
        enterLeftRight.clearFocus()
        enterFwdBack.clearFocus()
    }
    fun displaySolution() {
        fieldCh.text = currentSolution.charge.toInt().toString()
        fieldAz.text = azimuth.toString()
        fieldEl.text = "%.1f".format(elevation)
    }
    fun updateValues() {
        elevation = currentSolution.elevation
        elCor = currentSolution.plusCorrect
    }
}
