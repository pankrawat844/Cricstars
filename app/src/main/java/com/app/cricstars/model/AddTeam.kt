package com.app.cricstars.model

import java.io.Serializable

data class AddTeam(
    val `data`: Team.Data?,
    val msg: String?,
    val success: Boolean?
):Serializable {

}