package com.app.cricstars.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class UserData(var context: Activity) {
    var shared: SharedPreferences
    fun setAllInfo(
        uid: String?,
        name: String?,
        email: String?,
        mobile: String?,
        isLogin: Boolean,
        pass: String?
    ): Boolean {
        shared.edit().clear()
        return shared.edit()
            .putString("user_id", uid)
            .putString("name", name)
            .putString("email", email)
            .putString("mobile", mobile) //                .putString("wallet",wallet)
            //                .putString("token",token)
            .putBoolean("isLogin", isLogin)
            .putString("password", pass)
            .commit()
    }

    fun updateInfo(
        name: String?,
        email: String?,
        mobile: String?,
        isLogin: Boolean,
        pass: String?,
        player_style: String?,
        batting_style: String?,
        bowling_style: String?,
        dob:String
    ): Boolean {
        return shared.edit()
            .putString("name", name)
            .putString("email", email)
            .putString("player_style", player_style)
            .putString("batting_style", batting_style)
            .putString("bowling_style", bowling_style)
            .putString("dob", dob)
            .putString("mobile", mobile) //                .putString("wallet",wallet)
            //                .putString("token",token)
            .putBoolean("isLogin", isLogin)
            .putString("password", pass)
            .commit()
    }

    val uid: String?
        get() = shared.getString("user_id", "na")
    val token: String?
        get() = shared.getString("token", "na")
    var wallet: String?
        get() = shared.getString("wallet", "na")
        set(amount) {
            shared.edit().putString("wallet", amount).commit()
        }
    val name: String?
        get() = shared.getString("name", "na")
    val mobile: String?
        get() = shared.getString("mobile", "na")
    val email: String?
        get() = shared.getString("email", "")
    val isLogin: Boolean
        get() = shared.getBoolean("isLogin", false)

    val player_style: String?
        get() = shared.getString("player_style", "")

    val batting_style: String?
        get() = shared.getString("batting_style", "")
    val bowling_style: String?
        get() = shared.getString("bowling_style", "")

    val dob: String?
        get() = shared.getString("dob", "")
    fun setLogout() {
        shared.edit().putBoolean("isLogin", false).commit()
    }

    val password: String?
        get() = shared.getString("password", "na")

    companion object {
        const val DB = "userdb"
    }

    init {
        shared = context.getSharedPreferences(DB, Context.MODE_PRIVATE)
    }
}