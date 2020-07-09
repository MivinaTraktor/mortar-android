package com.example.first_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mCoordinates = Array(3) { 0 }
    var editList = List(3) { "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editList = listOf(mortarX.text.toString(), mortarY.text.toString(), mortarAlt.text.toString())
    }

    fun onClickIndirect(view: View) {
        if (mortarFolder == "") { //если не выбран миномет
            Toast.makeText(applicationContext,"Select a mortar!", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, IndirectActivity::class.java)
        for (i in mCoordinates.indices) { //0, если поле пустое
            if (editList[i] == "")
                continue
            mCoordinates[i] = editList[i].toInt()
        }
        intent.putExtra("mCoordinates", mCoordinates)
        startActivity(intent)
    }

    fun onClickDirect(view: View) {
        if (mortarFolder == "") { //если не выбран миномет
            Toast.makeText(applicationContext,"Select a mortar!", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, DirectActivity::class.java)
        mCoordinates[2] = mortarAlt.text.toString().toInt() //обнуление координат миномета, сохранение высоты
        intent.putExtra("mCoordinates", mCoordinates)
        startActivity(intent)
    }

    fun onMortarSelected(view: View) {
        if (view is RadioButton) {
            if (view.isChecked)
            when (view.getId()) {
                R.id.dvab11Button ->
                    if (view.isChecked) {
                        mortarFolder = "\\2b11\\"
                    }
                R.id.podnosButton ->
                    if (view.isChecked) {
                        mortarFolder = "\\podnos\\"
                    }
                R.id.d30Button ->
                    if (view.isChecked) {
                        mortarFolder = "\\d30\\"
                    }
                R.id.m252Button ->
                    if (view.isChecked) {
                        mortarFolder = "\\m252\\"
                    }
            }
        }
    }
}
