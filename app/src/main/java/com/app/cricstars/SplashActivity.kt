package com.app.cricstars

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.app.cricstars.utils.UserData

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(Runnable {
            if(UserData(this@SplashActivity).isLogin)
            startActivity(Intent(this@SplashActivity,BottomNavigationActivity::class.java))
            else
            startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
            finish()
        },3000)
    }
}