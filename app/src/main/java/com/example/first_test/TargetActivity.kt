package com.example.first_test

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.first_test.databinding.ActivityTargetBinding
import kotlin.math.*

class TargetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTargetBinding

    private var firingData = FiringData(mCoordinates.requireNoNulls(), tCoordinates.requireNoNulls())
    private var elevation = 0.0
    private var deflection = 0.0
    private var curNumber = 0
    private var azimuth = firingData.azimuth
    private var range = firingData.range
    private var altDif = firingData.altDif
    private var azCor = 0.0
    private lateinit var solutions: List<ChargePair>
    private lateinit var curSolution: ChargePair
    private var hiSelected = true
    private var timer: CountDownTimer? = null
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTargetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rngLabel.text = range.roundToInt().toString()
        binding.altDifLabel.text = altDif.toInt().toString()
        if (useDeflection) binding.textLabelAz.text = "Deflection"
        binding.fieldDisp.setText(stdDispersion.toString())
        applyData()

        binding.fieldDisp.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setDispersion(s.toString().toDoubleOrNull() ?: 0.0)
            }
        })
    }

    fun applyData() {
        solutions = chargesList(range, altDif)
        curSolution = solutions[curNumber]
        azCor = 2 * PI * range / mortarData.artDegree
        if (hiSelected) onClickHi(binding.root)
        else onClickLo(binding.root)
    }

    fun onClickOkCorrection(view: View) {
        val x = binding.eastWest.text.toString().toIntOrNull() ?: 0
        val y = binding.northSouth.text.toString().toIntOrNull() ?: 0
        val newData = FiringData(mCoordinates.requireNoNulls(), listOf(tCoordinates[0]!! + x, tCoordinates[1]!! + y, tCoordinates[2]!!))
        if (chargesList(newData.range, newData.altDif).isEmpty()) {
            Toast.makeText(applicationContext, "Invalid range!", Toast.LENGTH_SHORT).show()
            return
        }
        range = newData.range
        azimuth = newData.azimuth
        binding.rngLabel.text = range.roundToInt().toString()
        applyData()
        binding.eastWest.clearFocus()
        binding.northSouth.clearFocus()
        hideKeyboard()
    }

    fun onClickAnchor(view: View) {
        val x = tCoordinates[0]!! + (binding.eastWest.text.toString().toIntOrNull() ?: 0)
        val y = tCoordinates[1]!! + (binding.northSouth.text.toString().toIntOrNull() ?: 0)
        val firingDataNew = FiringData(mCoordinates.requireNoNulls(), listOf(x, y, tCoordinates[2]!!))
        if (chargesList(firingDataNew.range, firingDataNew.altDif).isEmpty()) {
            Toast.makeText(applicationContext, "Invalid range!", Toast.LENGTH_SHORT).show()
            return
        }
        tCoordinates[0] = x
        tCoordinates[1] = y
        firingData = firingDataNew
        range = firingData.range
        azimuth = firingData.azimuth
        applyData()
        clearCorrFields()
    }

    fun onClickResetCorrection(view: View) {
        clearCorrFields()
        range = firingData.range
        azimuth = firingData.azimuth
        binding.rngLabel.text = range.roundToInt().toString()
        applyData()
    }

    fun onClickPlus(view: View) {
        if (curNumber < solutions.size - 1) {
            curNumber++
            curSolution = solutions[curNumber]
            updateValues()
        }
    }

    fun onClickMinus(view: View) {
        if (curNumber != 0) {
            curNumber--
            curSolution = solutions[curNumber]
            updateValues()
        }
    }

    private fun updateValues() {
        if (hiSelected) {
            elevation = curSolution.hi
            time = curSolution.hiTime
        }
        else {
            elevation = curSolution.lo
            time = curSolution.loTime
        }
        binding.textViewCh.text = if (!mortarData.specialCharges.isNullOrEmpty())
            mortarData.specialCharges[curSolution.charge]
        else
            curSolution.charge.toString()
        if (useDeflection) {
            deflection = calcDeflection(deflectionArray[0]!!, deflectionArray[1]!!, azimuth)
            binding.textViewAz.text = deflection.roundToInt().toString()
        }
        else
            binding.textViewAz.text = azimuth.roundToInt().toString()
        binding.textViewEl.text = elevation.roundToInt().toString()
        setDispersion(binding.fieldDisp.text.toString().toDoubleOrNull() ?: 0.0)
        binding.textViewTof.text = "%.1f".format(time)
    }

    private fun setDispersion(disp: Double) {
        val az = if (useDeflection) deflection else azimuth
        binding.dispersionLR.text = az.roundToInt().toString()
        val dispAz = disp / azCor
        binding.dispersionAz.text = "%.1f".format(dispAz)
        binding.dispersionRight.text = turn(az, dispAz, mortarData.artDegree).roundToInt().toString()
        binding.dispersionLeft.text = turn(az, -dispAz, mortarData.artDegree).roundToInt().toString()
        binding.dispersionFB.text = elevation.roundToInt().toString()
        val plus: Double
        val minus: Double
        if (hiSelected) {
            plus = atanHi(curSolution.v, range + disp, altDif).toArtDegrees()
            minus = atanHi(curSolution.v, range - disp, altDif).toArtDegrees()
        }
        else {
            plus = atanLo(curSolution.v, range + disp, altDif).toArtDegrees()
            minus = atanLo(curSolution.v, range - disp, altDif).toArtDegrees()
        }
        binding.dispersionFwd.text = round(plus).toInt().toString()
        binding.dispersionBack.text = round(minus).toInt().toString()
        binding.dispersionEl.text =  when {
            plus.isNaN() -> "%.1f".format(abs(elevation - minus))
            minus.isNaN() -> "%.1f".format(abs(elevation - plus))
            else -> "%.1f".format(abs(plus - minus) / 2)
        }
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

    fun onClickShot(view: View) {
        timer?.cancel()
        timer = object: CountDownTimer((time * 1000.0).toLong(), 100) {
            override fun onTick(millisUntilFinished: Long) {
                binding.textViewTof.text = "%.1f".format(millisUntilFinished.toInt().toFloat() / 1000L)
            }

            override fun onFinish() {
                Thread.sleep(2000)
                binding.textViewTof.text = "%.1f".format(time)
            }
        }.start()
    }

    fun onClickTofReset(view: View) {
        timer?.cancel()
        binding.textViewTof.text = "%.1f".format(time)
    }

    fun clearCorrFields() {
        binding.eastWest.text.clear()
        binding.northSouth.text.clear()
        binding.eastWest.clearFocus()
        binding.northSouth.clearFocus()
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
