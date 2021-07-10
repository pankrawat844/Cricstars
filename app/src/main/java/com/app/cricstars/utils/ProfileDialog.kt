package com.app.cricstars.utils

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import com.app.cricstars.R

class ProfileDialog     // TODO Auto-generated constructor stub
    (var c: Activity) : Dialog(c) {
    var d: Dialog? = null
    var tvClose: TextView? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_profile_dialog)
        tvClose = findViewById(R.id.tv_close)
        tvClose?.setOnClickListener(View.OnClickListener { dismiss() })
    }
}