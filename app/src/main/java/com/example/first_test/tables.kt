package com.example.first_test

fun selectMortar(name: String) {
    when (name) {
        "2b11" -> {
            mortarCharges = arrayOf(0.4432, 0.583, 0.6672, 0.7656, 0.8377, 0.9014, 1.0001)
            muzzleVelocity = 265.409
            artDegree = 6400.0
        }
        "2b14" -> {
            mortarCharges = arrayOf(0.35, 0.7, 1.0)
            muzzleVelocity = 211.0
            artDegree = 6400.0
        }
        "d30" -> {
            mortarCharges = arrayOf(0.4082, 0.5788, 0.708, 0.8165, 0.9121, 1.0)
            muzzleVelocity = 387.0
            artDegree = 6000.0
        }
        "m252" -> {
            mortarCharges = arrayOf(0.35, 0.7, 1.0)
            muzzleVelocity = 202.5
            artDegree = 6400.0
        }
        else -> {
            mortarCharges = null
            muzzleVelocity = null
        }
    }
}