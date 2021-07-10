package com.app.cricstars.model

data class Signup(
    val `data`: Data
) {
    data class Data(
        val msg: String,
        val success: Boolean
    )
}