package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_indirect.*

class IndirectActivity : AppCompatActivity() {

    private var mortarArr = Array(3) { 0 }
    private var targetArr = Array(3) { 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indirect)

        val intentMortar = intent
        mortarArr = intentMortar.getIntArrayExtra("mCoordinates")?.toTypedArray() ?: Array(3) { 0 }

        targetX.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                targetArr[0] = s.toString().toInt()
            }
        })

        targetY.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                targetArr[1] = s.toString().toInt()
            }
        })

        targetAlt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                targetArr[1] = s.toString().toInt()
            }
        })
    }

    fun onClickMortar(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onClickDirect(view: View) {
        val intent = Intent(this, DirectActivity::class.java)
        intent.putExtra("mCoordinates", targetArr)
        startActivity(intent)
    }
}
