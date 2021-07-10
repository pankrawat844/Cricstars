package com.app.cricstars

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.app.cricstars.model.GenericResponse
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import kotlinx.android.synthetic.main.activity_verify.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class VerifyOtpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)
        changePassword.setOnClickListener {
            if(otp.text.toString().isNullOrEmpty()){
                Toast.makeText(this, "Please enter otp.", Toast.LENGTH_SHORT).show()
            }else if(newpassword.text.toString().isNullOrEmpty()){
                Toast.makeText(this, "Please enter new password.", Toast.LENGTH_SHORT).show()
            }else if(!otp.text.toString().equals(intent.getStringExtra("otp"))){
                Toast.makeText(this, "Please enter correct otp", Toast.LENGTH_SHORT).show()
            }else
                changePassword()
        }
    }

    private fun changePassword() {

//        println(randomNumber)
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@VerifyOtpActivity)?.create(
                RetrofitMethods::class.java
            )
          retrofitMethod?.changePassword(
                intent.getStringExtra("phone"),
                newpassword.text.toString()
            )?.enqueue(
                object : Callback<GenericResponse?> {
                    override fun onResponse(call: Call<GenericResponse?>, response: Response<GenericResponse?>) {
                        loader.visibility = View.GONE
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data?.success!!) {
                                Toast.makeText(
                                    this@VerifyOtpActivity,
                                    "Change password successfully",
                                    Toast.LENGTH_SHORT
                                ).show()


                                Intent(
                                    this@VerifyOtpActivity,
                                    LoginActivity::class.java
                                ).also {
                                    it.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(it)
                                }


                            } else
                                Toast.makeText(
                                    this@VerifyOtpActivity,
                                    data?.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getString("msg")
                            Toast.makeText(this@VerifyOtpActivity, msg, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse?>, t: Throwable) {
                        loader.visibility = View.GONE
                        Toast.makeText(this@VerifyOtpActivity, t.message, Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }
    }

}