package com.app.cricstars.model

import java.io.Serializable

data class MatchDetail(
    var `data`: ArrayList<Data>?,
    var success: Boolean
):Serializable {
    data class Data(
        val match_details: MatchDetails,
        val t10_match_description: String,
        val t10_match_id: String,
        val t10_match_scheduled_at: String,
        val t10_match_title: String,
        val t10_status_id: String,
        val t10_team_A_id: String,
        val t10_team_B_id: String,
        val t10_venue_id: String,
        val teamAname: String,
        val teamBname: String,
        val venue_details: VenueDetails
    ):Serializable {
        data class MatchDetails(
            val t10_2ndinn_batsman1: String,
            val t10_2ndinn_batsman2: String,
            val t10_2ndinn_bowler: String,
            val t10_bat_bowl: String?=null,
            val t10_match_date: String,
            val t10_match_detail_id: String,
            val t10_match_id: String,
            val t10_organizer_id: String,
            val t10_overs: String,
            val t10_player_id: String,
            val t10_teamA_id: String,
            val t10_teamB_id: String,
            val t10_team_batsman1_id: String,
            val t10_team_batsman2_id: String,
            val t10_team_bowler_id: String,
            val t10_toss_won: String
        ):Serializable

        data class VenueDetails(
            val t10_city_id: String,
            val t10_state_id: String,
            val t10_status_id: String,
            val t10_venue_address: String,
            val t10_venue_created_at: String,
            val t10_venue_id: String,
            val t10_venue_name: String,
            val t10_venue_pincode: String
        ):Serializable
    }
}