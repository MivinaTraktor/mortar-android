package com.example.first_test

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.first_test.databinding.ActivityTargetBinding
import kotlin.math.*

class TargetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTargetBinding
    private var elevation = 0.0
    private var azimuth = 0.0
    private var currentNumber = 0
    private var tValues = target(mCoordinates.requireNoNulls(), tCoordinates.requireNoNulls())
    private var solutionList = chargesList(tValues)
    private var currentSolution = solutionList.first()
    private var hiSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTargetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.altDifLabel.text = tValues[1].toInt().toString()
        binding.rngLabel.text = tValues[0].roundToInt().toString()
        if (useDeflection) binding.textLabelAz.text = "Deflection"
        onClickHi(binding.root)
        updateValues()
    }

    fun onClickPlus(view: View) {
        if (currentNumber < solutionList.size - 1) {
            currentNumber++
            currentSolution = solutionList[currentNumber]
            updateValues()
        }
    }

    fun onClickMinus(view: View) {
        if (currentNumber != 0) {
            currentNumber--
            currentSolution = solutionList[currentNumber]
            updateValues()
        }
    }

    private fun updateValues() {
        elevation = if (hiSelected)
            currentSolution.hi
        else
            currentSolution.lo
        azimuth = tValues[2]
        binding.textViewCh.text = if (specialCharges != null)
            specialCharges!![currentSolution.charge]
        else
            currentSolution.charge.toString()
        binding.textViewAz.text = azimuth.roundToInt().toString()
        binding.textViewEl.text = elevation.roundToInt().toString()
    }

    fun onClickHi(view: View) {
        binding.buttonHi.backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
        binding.buttonLo.backgroundTintList = ColorStateList.valueOf(Color.LTGRAY)
        hiSelected = true
        updateValues()
    }

    fun onClickLo(view: View) {
        binding.buttonLo.backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
        binding.buttonHi.backgroundTintList = ColorStateList.valueOf(Color.LTGRAY)
        hiSelected = false
        updateValues()
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
