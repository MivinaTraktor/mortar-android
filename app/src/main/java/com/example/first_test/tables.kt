package com.example.first_test

fun selectMortar(name: String) {
    when (name) {
        "2b11" -> {
            mortarCharges = arrayOf(0.4432, 0.583, 0.6672, 0.7656, 0.8377, 0.9014, 1.0001)
            artDegree = 6400.0
            specialCharges = null
        }
        "2b14" -> {
            mortarCharges = arrayOf(0.35, 0.7, 1.0)
            artDegree = 6400.0
            specialCharges = null
        }
        "d30" -> {
            mortarCharges = arrayOf(158.0, 224.0, 274.0, 316.0, 353.0, 387.0)
            artDegree = 6000.0
            specialCharges = mapOf(1 to "4", 2 to "3", 3 to "2", 4 to "1", 5 to "R", 6 to "F")
        }
        "m252" -> {
            mortarCharges = arrayOf(0.35, 0.7, 1.0)
            artDegree = 6400.0
            specialCharges = null
        }
        else -> {
            mortarCharges = null
        }
    }
}