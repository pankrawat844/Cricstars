package com.app.cricstars.model

import java.io.Serializable

data class Tournament(
    val `data`: ArrayList<Data>,
    val success: Boolean
):Serializable {
    data class Data(
        val about: String,
        val ball_type: String,
        val city: String,
        val end_date: String,
        val ground: String,
        val organiser_name: String,
        val organiser_phone: String,
        val overs: String,
        val start_date: String,
        val tournament_banner: String,
        val tournament_id: String,
        val tournament_logo: String,
        val tournament_name: String
    ) : Serializable
}