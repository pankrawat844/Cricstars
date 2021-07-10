package com.app.cricstars.model

data class CurrentBowler(
    val `data`: Data?,
    val success: Boolean?
) {
    data class Data(
        val currentBowler: String?,
        val currentBowlerBalls: Int?,
        val currentBowlerExtra: Int?,
        val currentBowlerOver: Double?,
        val currentBowlerRuns: Int?,
        val currentBowlerWicket: Int?,
        val currentBowlerMedian: Int?
    )
}