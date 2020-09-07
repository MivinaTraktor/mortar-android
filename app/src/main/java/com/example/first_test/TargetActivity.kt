package com.example.first_test

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_target.*

class TargetActivity : AppCompatActivity() {

    private lateinit var enterLeftRight: TextInputEditText
    private lateinit var enterFwdBack: TextInputEditText
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
        enterLeftRight.clearFocus()
        enterFwdBack.clearFocus()
        hideKeyboard()
    }

    fun onClickCorrectOk(view: View) {
        fwdBack = enterFwdBack.text.toString().toIntOrNull() ?: 0
        leftRight = enterLeftRight.text.toString().toIntOrNull() ?: 0
        elNew = elevation - (elCor * fwdBack)
        azNew = azimuth + (azCor * leftRight).toInt()
        if (azNew < 0) azNew += 6400
        else if (azNew > 6400) azNew -= 6400
        textViewEl.text = "%.1f".format(elNew)
        textViewAz.text = azNew.toString()
        enterLeftRight.clearFocus()
        enterFwdBack.clearFocus()
        hideKeyboard()
    }

    fun onClickAnchor(view: View) {
        elevation = elNew
        azimuth = azNew
        clearFields()
        enterLeftRight.clearFocus()
        enterFwdBack.clearFocus()
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
        enterLeftRight = findViewById(R.id.fieldLeftRight)
        enterFwdBack = findViewById(R.id.fieldFwdBack)
        solNumber = intent.getIntExtra("Number of solutions", 0)
        for (i in 0 until solNumber) {
            val solution = intent.getFloatArrayExtra("sol$i")!!
            solutionList.add(Display(solution[0], solution[1], solution[2], solution[3], solution[4]))
        }
        currentSolution = solutionList[0]
        updateValues()
        azimuth = intent.getFloatExtra("azimuth", 0F).toInt()
        azCor = intent.getFloatExtra("azimuthCor", 0F)
        altDifLabel.text = currentSolution.altDif.toInt().toString()
        rngLabel.text = currentSolution.range.toInt().toString()
        displaySolution()

        textViewAz.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val curAz = value.toString().toInt()
                val disp = fieldDisp.text.toString().toFloatOrNull() ?: 0F
                val curEl = textViewEl.text.toString().replace(',', '.').toFloat()
                setDispersion(disp, curAz, curEl)
            }
        })

        textViewEl.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val curEl = value.toString().replace(',', '.').toFloat()
                val curAz = textViewAz.text.toString().toInt()
                val disp = fieldDisp.text.toString().toFloatOrNull() ?: 0F
                setDispersion(disp, curAz, curEl)
            }
        })

        fieldDisp.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val disp = value.toString().toFloatOrNull() ?: 0F
                val curAz = textViewAz.text.toString().toInt()
                val curEl = textViewEl.text.toString().replace(',', '.').toFloat()
                setDispersion(disp, curAz, curEl)
            }
        })
        fieldDisp.setText(stdDispersion.toString())
    }
    private fun clearFields() {
        enterFwdBack.text?.clear()
        enterLeftRight.text?.clear()
        enterLeftRight.clearFocus()
        enterFwdBack.clearFocus()
    }
    private fun displaySolution() {
        textViewCh.text = currentSolution.charge.toInt().toString()
        textViewAz.text = azimuth.toString()
        textViewEl.text = "%.1f".format(elevation)
    }
    private fun updateValues() {
        elevation = currentSolution.elevation
        elCor = currentSolution.plusCorrect
    }

    private fun setDispersion(disp: Float, curAz: Int, curEl: Float) {
        dispersionLR.text = curAz.toString()
        val dispAz = (azCor * disp).toInt()
        if (curAz + dispAz > 6400)
            dispersionRight.text = (curAz + dispAz - 6400).toString()
        else
            dispersionRight.text = (curAz + dispAz).toString()
        if (curAz - dispAz < 0)
            dispersionLeft.text = (curAz - dispAz + 6400).toString()
        else
            dispersionLeft.text = (curAz - dispAz).toString()
        dispersionFB.text = "%.1f".format(curEl)
        val dispEl = elCor * disp
        dispersionForward.text = "%.1f".format((curEl - dispEl))
        dispersionBack.text = "%.1f".format((curEl + dispEl))
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
