package com.example.first_test

fun selectMortar(name: String) {
    when (name) {
        "2b11" -> {
            mortarCharges = arrayOf(0.4432, 0.583, 0.6672, 0.7656, 0.8377, 0.9014, 1.0001)
            muzzleVelocity = 265.409
        }
        "2b14" -> {
            mortarCharges = arrayOf(0.35, 0.7, 1.0)
            muzzleVelocity = 211.0
        }
        "d30" -> {
            mortarCharges = arrayOf(0.19, 0.3, 0.48)
            muzzleVelocity = 810.0
        }
        "m252" -> {
            mortarCharges = arrayOf(0.35, 0.7, 1.0)
            muzzleVelocity = 202.5
        }
        else -> {
            mortarCharges = null
            muzzleVelocity = null
        }
    }
}