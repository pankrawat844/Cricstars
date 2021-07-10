package com.app.cricstars

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.cricstars.model.GenericResponse
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.String
import java.util.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        sendOtp.setOnClickListener { 
            if(phone.text.toString().isNullOrEmpty())
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Please enter registered mobile no.",
                    Toast.LENGTH_SHORT
                ).show()
                else
                    sendOtp()
        }
    }



    private fun sendOtp() {
        val r = Random()
        val randomNumber = String.format("%04d", r.nextInt(1001))
//        println(randomNumber)
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@ForgotPasswordActivity)?.create(
                RetrofitMethods::class.java
            )
            val response=retrofitMethod?.forgotPassword(
                phone.text.toString(),
                randomNumber
            )?.enqueue(
                object : Callback<GenericResponse?> {
                    override fun onResponse(call: Call<GenericResponse?>, response: Response<GenericResponse?>) {
                        loader.visibility = View.GONE
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data?.success!!) {
                                Toast.makeText(
                                    this@ForgotPasswordActivity,
                                    "Otp send successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()


                                    Intent(
                                        this@ForgotPasswordActivity,
                                        VerifyOtpActivity::class.java
                                    ).also {
                                        it.putExtra("phone",phone.text.toString())
                                        it.putExtra("otp",randomNumber)
                                        startActivity(it)
                                    }


                            } else
                                Toast.makeText(
                                    this@ForgotPasswordActivity,
                                    data?.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getString("msg")
                            Toast.makeText(this@ForgotPasswordActivity, msg, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse?>, t: Throwable) {
                        loader.visibility = View.GONE
                        Toast.makeText(this@ForgotPasswordActivity, t.message, Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }
    }

}