package com.app.cricstars

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.cricstars.model.Login
import com.app.cricstars.model.Signup
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.app.cricstars.utils.CheckInternetConnection
import com.app.cricstars.utils.UserData
import com.truecaller.android.sdk.*
import com.truecaller.android.sdk.clients.VerificationCallback
import com.truecaller.android.sdk.clients.VerificationDataBundle
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.phone
import kotlinx.android.synthetic.main.activity_login.signup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity(),ITrueCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        initSDK()
        login.setOnClickListener {
//            TruecallerSDK.getInstance().requestVerification(
//                "IN",
//                phone.text.toString(),
//                object : VerificationCallback {
//                    override fun onRequestSuccess(
//                        requestCode: Int,
//                        extras: VerificationDataBundle?
//                    ) {
//                        if (requestCode == VerificationCallback.TYPE_MISSED_CALL_INITIATED) {
//                            if (extras != null)
//                                extras.getString(VerificationDataBundle.KEY_TTL)
//                        }
//
//                        if (requestCode == VerificationCallback.TYPE_MISSED_CALL_RECEIVED) {
//                        }
//
//                        if (requestCode == VerificationCallback.TYPE_OTP_INITIATED) {
//                            if (extras != null)
//                                extras.getString(VerificationDataBundle.KEY_TTL)
//                        }
//
//                        if (requestCode == VerificationCallback.TYPE_OTP_RECEIVED) {
//                            Log.e("TAG", "onRequestSuccess: ", )
//                        }
//
//                        if (requestCode == VerificationCallback.TYPE_VERIFICATION_COMPLETE) {
//                            Log.e("TAG", "onRequestSuccess: ",)
//                        }
//
//                        if (requestCode == VerificationCallback.TYPE_PROFILE_VERIFIED_BEFORE) {
//                            Log.e("TAG", "onRequestSuccess: ",)
//                        }
//
//                    }
//
//                    override fun onRequestFailure(p0: Int, p1: TrueException) {
//                        Log.e("TAG", "onRequestFailure: " + p1.exceptionMessage)
//                    }
//
//                },
//                this@LoginActivity
//            );
            if(phone.text.toString().isEmpty())
                Toast.makeText(this@LoginActivity, "Please enter email or phone no.", Toast.LENGTH_SHORT).show()
            else if(password.text.toString().isEmpty())
                Toast.makeText(this@LoginActivity, "Please enter password.", Toast.LENGTH_SHORT).show()
             else if(!CheckInternetConnection.isNetworkAvailable(this))
                Toast.makeText(this@LoginActivity, "Please check internet connection.", Toast.LENGTH_SHORT).show()
            else
                login()
        }
        signup.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))

        }
        forgotpassword.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))

        }
    }

    fun initSDK(){
        val trueScope: TruecallerSdkScope = TruecallerSdkScope.Builder(this, this)
            .consentMode(TruecallerSdkScope.CONSENT_MODE_BOTTOMSHEET)

            .loginTextPrefix(TruecallerSdkScope.LOGIN_TEXT_PREFIX_TO_GET_STARTED)
            .loginTextSuffix(TruecallerSdkScope.LOGIN_TEXT_SUFFIX_PLEASE_VERIFY_MOBILE_NO)
            .ctaTextPrefix(TruecallerSdkScope.CTA_TEXT_PREFIX_USE)
            .buttonShapeOptions(TruecallerSdkScope.BUTTON_SHAPE_ROUNDED)
            .privacyPolicyUrl("<<YOUR_PRIVACY_POLICY_LINK>>")
            .termsOfServiceUrl("<<YOUR_PRIVACY_POLICY_LINK>>")
            .footerType(TruecallerSdkScope.FOOTER_TYPE_MANUALLY)
            .consentTitleOption(TruecallerSdkScope.SDK_CONSENT_TITLE_LOG_IN)
            .sdkOptions(TruecallerSdkScope.SDK_OPTION_WITH_OTP)
            .build()

        TruecallerSDK.init(trueScope)
        if (TruecallerSDK.getInstance().isUsable()) {
            TruecallerSDK.getInstance().getUserProfile(this);
        }else{

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TruecallerSDK.SHARE_PROFILE_REQUEST_CODE) {
            TruecallerSDK.getInstance().onActivityResultObtained(
                this,
                requestCode,
                resultCode,
                data
            );
//            if (TruecallerSDK.getInstance().isUsable()) {
//                TruecallerSDK.getInstance().getUserProfile(this);
//            }
        }
    }
    override fun onSuccessProfileShared(p0: TrueProfile) {
        Toast.makeText(this, p0.phoneNumber, Toast.LENGTH_SHORT).show()
        phone.setText(p0.phoneNumber!!)
    }

    override fun onFailureProfileShared(p0: TrueError) {
        //Toast.makeText(this, p0.errorType , Toast.LENGTH_SHORT).show()
        Log.e("asdfadsf", p0.toString())
    }

    override fun onVerificationRequired(p0: TrueError?) {
       // Toast.makeText(this, p0?.errorType!!, Toast.LENGTH_SHORT).show()


    }

    private fun login() {
    loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@LoginActivity)?.create(
                RetrofitMethods::class.java
            )
            val response=retrofitMethod?.login(phone.text.toString(),password.text.toString())?.enqueue(
                object : Callback<Login?> {
                    override fun onResponse(call: Call<Login?>, response: Response<Login?>) {
                        loader.visibility= View.GONE
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data?.data?.success!!) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Login Successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                UserData(this@LoginActivity).setAllInfo(data.data.response?.t10_app_user_id,data.data.response?.t10_app_user_fullname,data.data.response?.t10_app_user_emailid,data.data.response?.t10_app_user_contact_no,true,"")
                                val sharedpref= getSharedPreferences("app", MODE_PRIVATE)
                                val editor=sharedpref.edit()
                                editor.putString("id",data.data.response?.t10_app_user_id)
                                editor.putString("name",data.data.response?.t10_app_user_fullname)
                                editor.putString("email",data.data.response?.t10_app_user_emailid)
                                editor.putString("phone",data.data.response?.t10_app_user_contact_no)
                                editor.apply()
                                startActivity(Intent(this@LoginActivity,BottomNavigationActivity::class.java))
                                finish()
                            } else
                                Toast.makeText(
                                    this@LoginActivity,
                                    data?.data?.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getJSONObject("data").getString("msg")
                            Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Login?>, t: Throwable) {
                        loader.visibility= View.GONE
                        Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

}