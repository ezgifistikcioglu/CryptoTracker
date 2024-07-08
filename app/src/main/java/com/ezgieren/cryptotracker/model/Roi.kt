package com.ezgieren.cryptotracker.model

data class Roi(
    val currency: String,
    val percentage: Double,
    val times: Double
)