package com.app.cricstars.model

data class State(
    val `data`: List<Data>,
    val success: Boolean
) {
    data class Data(
        val t10_state_created_at: String,
        val t10_state_id: String,
        val t10_state_name: String,
        val t10_status_id: String
    )
}