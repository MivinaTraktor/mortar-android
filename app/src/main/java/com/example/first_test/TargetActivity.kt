package com.example.first_test

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_target.*
import kotlin.math.*

class TargetActivity : AppCompatActivity() {

    private var elevation = 0.0
    private var elevation360 = 0.0
    private var azimuth = 0.0
    private var azimuth360 = 0.0
    private var currentNumber = 0
    private var tValues = target(mCoordinates.requireNoNulls(), tCoordinates.requireNoNulls())
    private var solutionList = chargesList(tValues)
    private var currentSolution = solutionList.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
        updateValues()
        altDifLabel.text = tValues[1].toInt().toString()
        rngLabel.text = tValues[0].roundToInt().toString()
        displaySolution()
    }

    fun onClickPlus(view: View) {
        if (currentNumber < solutionList.size - 1) {
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

    private fun updateValues() {
        elevation = currentSolution.elevation
        elevation360 = currentSolution.elevation360
        azimuth = tValues[2]
        azimuth360 = tValues[3]
    }

    private fun displaySolution() {
        textViewCh.text = currentSolution.charge.toString()
        textViewAz.text = azimuth.roundToInt().toString()
        textViewEl.text = "%.1f".format(elevation)
        textViewAz360.text = "%.1f°".format(azimuth360)
        textViewEl360.text = "%.2f°".format(elevation360)
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
