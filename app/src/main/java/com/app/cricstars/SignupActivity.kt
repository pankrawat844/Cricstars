package com.app.cricstars

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.cricstars.model.Signup
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.app.cricstars.utils.CheckInternetConnection
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signup.setOnClickListener {

        if(name.text.toString().isEmpty())
            Toast.makeText(this@SignupActivity, "Please enter name.", Toast.LENGTH_SHORT).show()
            else if(password.text.toString().isEmpty())
            Toast.makeText(this@SignupActivity, "Please enter password.", Toast.LENGTH_SHORT).show()
            else if(email.text.toString().isEmpty())
            Toast.makeText(this@SignupActivity, "Please enter email.", Toast.LENGTH_SHORT).show()
            else if(phone.text.toString().isEmpty())
            Toast.makeText(this@SignupActivity, "Please enter phone no.", Toast.LENGTH_SHORT).show()
            else if(!CheckInternetConnection.isNetworkAvailable(this))
            Toast.makeText(this@SignupActivity, "Please check internet connection.", Toast.LENGTH_SHORT).show()
            else
            signup()
        }
    }

        private fun signup() {
            loader.visibility= View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch{
                val retrofitMethod = ApiBuilder.getRetrofitInstance(this@SignupActivity)?.create(
                    RetrofitMethods::class.java
                )
                val response=retrofitMethod?.signup(email.text.toString(),password.text.toString(),name.text.toString(),phone.text.toString())?.enqueue(
                    object : Callback<Signup?> {
                        override fun onResponse(call: Call<Signup?>, response: Response<Signup?>) {
                            loader.visibility= View.GONE
                            if (response.isSuccessful) {
                                val data = response.body()
                                if (data?.data?.success!!) {
                                    finish()
                                    Toast.makeText(
                                        this@SignupActivity,
                                        data?.data?.msg,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else
                                    Toast.makeText(
                                        this@SignupActivity,
                                        data?.data?.msg,
                                        Toast.LENGTH_SHORT
                                    ).show()
                            } else {
                                val msg =
                                    JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                        .getJSONObject("data").getString("msg")
                                Toast.makeText(this@SignupActivity, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
    
                        override fun onFailure(call: Call<Signup?>, t: Throwable) {
                            loader.visibility= View.GONE
                            Toast.makeText(this@SignupActivity, t.message, Toast.LENGTH_SHORT).show()
                        }
    
                    })
            }
        }
}