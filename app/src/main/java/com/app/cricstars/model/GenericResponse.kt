package com.app.cricstars.model

import java.io.Serializable

data class GenericResponse(
    var `data`: ArrayList<String>?=null,
    var success: Boolean,
    var msg:String,
    var match_id:String?=null
):Serializable {


}