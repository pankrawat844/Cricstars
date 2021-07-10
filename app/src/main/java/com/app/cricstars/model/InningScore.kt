package com.app.cricstars.model

import java.io.Serializable

data class InningScore(
    val ballsFaced: Int,
    val bowlerName: String,
    val fours: Int,
    val name: String,
    val runs: Int,
    val sixes: Int,
    val wicketHelperName: String,
    val wicket_type: String
): Serializable