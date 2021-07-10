package com.app.cricstars.model

import java.io.Serializable

data class TeamMember(
    val `data`: List<Data>,
    val success: Boolean
):Serializable {
    data class Data(
        val t10_city_id: String,
        val t10_coupon_id: String,
        val t10_member_type_id: String,
        val t10_state_id: String,
        val t10_status_id: String,
        val t10_team_id: String,
        val t10_team_member_access_token: Any,
        val t10_team_member_age: String,
        val t10_team_member_contact_no: String,
        val t10_team_member_email_id: String,
        val t10_team_member_id: String,
        val t10_team_member_image: Any,
        val t10_team_member_is_active: String,
        val t10_team_member_name: String,
        val t10_team_member_occupation: String,
        val t10_team_member_occupation_json: Any,
        val t10_team_member_otp_code: String,
        val t10_team_member_password: String
    ):Serializable
}