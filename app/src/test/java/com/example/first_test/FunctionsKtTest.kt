package com.example.first_test

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class FunctionsKtTest {

    @Test
    fun findRange() {
        assertEquals(findRange(200.0, 39.41), 4000F)
    }
}