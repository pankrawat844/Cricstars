package com.app.cricstars.model

data class Commentary(
    val `data`: Data,
    val success: Boolean
) {
    data class Data(
        val bating_team_name: String,
        val bowling_team_name: String,
        var commentary: List<Commentary>?=null,
        val teamAname: String,
        val teamBname: String,
        val toss_selection: String,
        val toss_string: String,
        val toss_won: String
    ) {
        data class Commentary(
            val ball_number: String,
            val bat_member_id: String,
            val bat_team_id: String,
            val batsman1_ball: Int,
            val batsman1_name: String,
            val batsman1_run: Int,
            val batsman2_ball: Int,
            val batsman2_name: String,
            val batsman2_run: Int,
            val bowl_member_id: String,
            val bowl_team_id: String,
            val bowl_to_count: String,
            val bowler_name: String,
            val bowlerover: Double,
            val bowlerruns: Int,
            val bowlerwicket: Int,
            val create_DateTime: String,
            val extras: String,
            val extras_run: String,
            val four_count: String,
            val new_batsman_id: String,
            val next_bowler: String,
            val non_strike_batsman: String,
            val over: String,
            val run_type: String,
            val runs: String,
            val six_count: String,
            val strike1: String,
            val strike2: String,
            val t10_match_id: String,
            val t10_member_id: String,
            val t10_score_id: String,
            val totalBowls: Int,
            val totalOvers: Double,
            val totalRuns: Int,
            val totalWicket: Int,
            val tournament_id: String,
            val wicket: String,
            val wicket_help_member_id: String,
            val wkt_type: String
        )
    }
}