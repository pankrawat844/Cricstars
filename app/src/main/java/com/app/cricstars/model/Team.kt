package com.app.cricstars.model

import java.io.Serializable

data class Team(
    val `data`: ArrayList<Data>,
    val success: Boolean,
    val msg:String?=null
):Serializable {
    data class Data(
        val t10_city_id: String,
        val t10_coscoteam: String,
        val t10_coupon_id: Any,
        val t10_member_type_id: String,
        val t10_state_id: String,
        val t10_status_id: String,
        val t10_team_captain_contact_no: String,
        val t10_team_captain_email_id: String,
        val t10_team_captain_name: String,
        val t10_team_captain_occupation: String,
        val t10_team_captain_password: String,
        val t10_team_created_at: String,
        val t10_team_id: String,
        val t10_team_is_verified: String,
        val t10_team_member_age: String,
        val t10_team_member_id: Any,
        val t10_team_name: String,
        val t10_team_otp_code: String,
        val t10_team_payment_status: String,
        val t10_team_total_no_member: String
    ):Serializable
}