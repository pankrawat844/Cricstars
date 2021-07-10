package com.app.cricstars.model

data class Login(
    val `data`: Data
) {
    data class Data(
        val response: Response?=null,
        val msg: String?=null,
        val success: Boolean
    ) {
        data class Response(
            val t10_app_user_contact_no: String,
            val t10_app_user_created_at: String,
            val t10_app_user_emailid: String,
            val t10_app_user_fullname: String,
            val t10_app_user_id: String,
            val t10_app_user_image: String,
            val t10_app_user_otp_code: Any,
            val t10_app_user_password: String,
            val t10_status_id: String
        )
    }
}