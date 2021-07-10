package com.app.cricstars.retrofit

import com.app.cricstars.model.Team

interface OnRecylerclerClickListener {
    fun onClick(teamNo:Int,data: Team.Data?)
}