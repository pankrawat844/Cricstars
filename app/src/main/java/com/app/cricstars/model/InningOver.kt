package com.app.cricstars.model

import java.io.Serializable

data class InningOver(
    val name: String,
    val overs: Double,
    val runs: Int,
    val wickets: Int
): Serializable