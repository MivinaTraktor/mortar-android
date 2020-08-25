package com.example.first_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_target.*

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

    fun onClickReset(view: View) {
        enterFwdBack.text?.clear()
        enterLeftRight.text?.clear()
        elNew = elevation
        azNew = azimuth
        fieldEl.text = "%.1f".format(elNew)
        fieldAz.text = azNew.toString()
        enterLeftRight.clearFocus()
        enterFwdBack.clearFocus()
    }

    fun onClickCorrectOk(view: View) {
        fwdBack = enterFwdBack.text.toString().toIntOrNull() ?: 0
        leftRight = enterLeftRight.text.toString().toIntOrNull() ?: 0
        elNew = elevation + (elCor * fwdBack)
        azNew = azimuth + (azCor * leftRight).toInt()
        fieldEl.text = "%.1f".format(elNew)
        fieldAz.text = azNew.toString()
        enterLeftRight.clearFocus()
        enterFwdBack.clearFocus()
    }

    fun onClickAnchor(view: View) {
        elevation = elNew
        azimuth = azNew
        enterFwdBack.text?.clear()
        enterLeftRight.text?.clear()
        enterLeftRight.clearFocus()
        enterFwdBack.clearFocus()
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
        val solutionArr = intent.getFloatArrayExtra("solution")!!
        fieldRng.text = solutionArr[5].toInt().toString()
        fieldAltDif.text = solutionArr[6].toInt().toString()
        elevation = solutionArr[4]
        fieldEl.text = "%.1f".format(elevation)
        azimuth = solutionArr[0].toInt()
        fieldCh.text = solutionArr[2].toInt().toString()
        fieldAz.text = azimuth.toString()
        azCor = solutionArr[1]
        elCor = solutionArr[3]
    }
}
