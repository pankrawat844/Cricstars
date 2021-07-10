package com.app.cricstars.model

import java.io.Serializable

data class MatchScoreDetail(
    val `data`: Data,
    val success: Boolean
):Serializable {
    data class Data(
        val bating_team_name: String,
        val bowling_team_name: String,
        val inning1Overs: List<Inning1Over>?=null,
        val inning1OversBowled: Double,
        val inning1Score: List<Inning1Score>?=null,
        val inning1Teamscore: Int,
        val inning1fallofwicket:String,
        val inning1wicket: Int,
        val inning1wide:Int,
        val inning1noball:Int,
        val inning1bye:Int,
        val inning1legbye:Int,
        val inning1extras:Int,
        val inning2Overs: List<Inning2Over>?=null,
        val inning2OversBowled: Double,
        val inning2Score: List<Inning2Score>?=null,
        val inning2Teamscore: Int,
        val inning2wicket: Int,
        val inning2wide: Int,
        val inning2noball: Int,
        val inning2bye: Int,
        val inning2legbye: Int,
        val inning2extras: Int,
        val inning2fallofwicket:String,
        val maximumOvers: String,
        val teamAname: String,
        val teamBname: String,
        val toss_selection: String,
        val toss_string: String,
        val toss_won: String,
        val venue_id: String,
        val venue_name: String,
        val currentBowler: String,
        val currentBowlerRuns: Int,
        val currentBowlerWicket: Int,
        val currentBowlerBalls: Int,
        val currentBowlerOver: Double,
        val currentBowlerExtra: Int,
        val currentBowlerMedian: Int,
        val strikeBatsman: String,
        val strikeBatsmanRun: Int,
        val strikeBatsmanBall: Int,
        val strikeBatsmanFour: Int,
        val strikeBatsmanSix: Int,
        val nonstrikeBatsman: String,
        val nonstrikeBatsmanRun: Int,
        val nonstrikeBatsmanBall: Int,
        val nonstrikeBatsmanFour: Int,
        val nonstrikeBatsmanSix: Int,
        val inningOneCompleted: Boolean,
        val inningTwoCompleted: Boolean,
        val matchResult: String=""
    ):Serializable {
        data class Inning1Over(
            val name: String,
            val overs: Double,
            val runs: Int,
            val medians: Int,
            val wickets: Int,
            val bowls: Int,
        ):Serializable

        data class Inning1Score(
            val ballsFaced: Int,
            val bowlerName: String,
            val fours: Int,
            val name: String,
            val runs: Int,
            val sixes: Int,
            val wicketHelperName: String,
            val wicket_type: String
        ):Serializable

        data class Inning2Over(
            val name: String,
            val overs: Double,
            val runs: Int,
            val medians: Int,
            val wickets: Int,
            val bowls: Int
        ):Serializable

        data class Inning2Score(
            val ballsFaced: Int,
            val bowlerName: String,
            val fours: Int,
            val name: String,
            val runs: Int,
            val sixes: Int,
            val wicketHelperName: String,
            val wicket_type: String
        ):Serializable
    }
}