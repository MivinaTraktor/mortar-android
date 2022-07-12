package com.example.first_test

class MortarData(artCharges: List<Double> = emptyList(), val artDegree: Double = 6400.0, initSpeed: Double = 1.0, val specialCharges: List<String> = emptyList()) {
    val chargeSpeeds = artCharges.map { it * initSpeed }
}

fun selectMortar(name: String) {
    mortarData = when (name) {
        "m224" -> {
            MortarData(listOf(0.58, 0.72, 0.85, 1.0), initSpeed = 186.0)
        }
        "2b11" -> {
            MortarData(listOf(0.4432, 0.583, 0.6672, 0.7656, 0.8377, 0.9014, 1.0001), initSpeed = 265.409)
        }
        "2b14" -> {
            MortarData(listOf(0.35, 0.7, 1.0), initSpeed = 211.0)
        }
        "d30" -> {
            MortarData(listOf(158.0, 224.0, 274.0, 316.0, 353.0, 387.0), 6000.0, specialCharges = listOf("4", "3", "2", "1", "R", "F"))
        }
        "m252" -> {
            MortarData(listOf(0.35, 0.7, 1.0), initSpeed = 200.0)
        }
        else -> {
            MortarData()
        }
    }
}