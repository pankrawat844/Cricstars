package com.app.cricstars

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.app.cricstars.model.GenericResponse
import com.app.cricstars.model.TeamMember
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.app.cricstars.utils.UserData
import kotlinx.android.synthetic.main.activity_add_player.*
import kotlinx.android.synthetic.main.activity_add_player.loader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPlayerActivity : AppCompatActivity() {
    var memberId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_player)
            if(intent.hasExtra("player_data")){
                val data=intent.getSerializableExtra("player_data") as TeamMember.Data
                memberId=data.t10_team_member_id
                name.setText(data.t10_team_member_name)
                phone.setText(data.t10_team_member_contact_no)
                email.setText(data.t10_team_member_email_id)
                age.setText(data.t10_team_member_age)
                if(data.t10_member_type_id=="1")
                   member_type.setSelection(1)
                if(data.t10_member_type_id=="2")
                    member_type.setSelection(2)
                if(data.t10_member_type_id=="3")
                    member_type.setSelection(3)
                if(data.t10_member_type_id=="4")
                    member_type.setSelection(4)
            }

        update.setOnClickListener {
            if(name.text.toString().isNullOrEmpty())
                Toast.makeText(this@AddPlayerActivity, "Please enter player name", Toast.LENGTH_SHORT).show()
            else if(phone.text.toString().isNullOrEmpty())
                Toast.makeText(this@AddPlayerActivity, "Please enter player phone no", Toast.LENGTH_SHORT).show()
            else if(email.text.toString().isNullOrEmpty())
                   Toast.makeText(this@AddPlayerActivity, "Please enter player email", Toast.LENGTH_SHORT).show()
            else if(age.text.toString().isNullOrEmpty())
                Toast.makeText(this@AddPlayerActivity, "Please enter player age", Toast.LENGTH_SHORT).show()
            else if(member_type.selectedItem.toString()=="Member Type")
                Toast.makeText(this@AddPlayerActivity, "Please select member type", Toast.LENGTH_SHORT).show()
            else {
                if(intent.hasExtra("player_data"))
                    editPlayer()
                else
                addPlayer()
            }
        }

    }

    private fun addPlayer() {
        var membertype=""
        if(member_type.selectedItem.toString()=="Batsman")
            membertype="1"

        if(member_type.selectedItem.toString()=="Bowler")
            membertype="2"
        if(member_type.selectedItem.toString()=="All-rounder")
            membertype="3"
        if(member_type.selectedItem.toString()=="Wicket-keeper")
            membertype="4"
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@AddPlayerActivity)?.create(
                RetrofitMethods::class.java
            )
            var userdat= UserData(this@AddPlayerActivity)
            var stateId=""
            var cityId=""
//            stateList.forEach {
//                if(it.t10_state_name==state.selectedItem.toString())
//                    stateId=it.t10_state_id
//            }
//            citiesList.forEach {
//                if(it.t10_city_id==city.selectedItem.toString())
//                    cityId=it.t10_city_id
//            }
            retrofitMethod?.addPlayer("0","0",intent.getStringExtra("team_id"),name.text.toString(),email.text.toString(),age.text.toString(),phone.text.toString(),membertype)?.enqueue(
                object : Callback<GenericResponse?> {
                    override fun onResponse(call: Call<GenericResponse?>, response: Response<GenericResponse?>) {
                        loader.visibility = View.INVISIBLE
                        if (response.isSuccessful) {
                            //response.body()!!
                            finish()
                            Toast.makeText(this@AddPlayerActivity, "Player added successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getString("msg")
                            Toast.makeText(this@AddPlayerActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse?>, t: Throwable) {
                        loader.visibility = View.INVISIBLE
                        Toast.makeText(this@AddPlayerActivity, t.message, Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

    private fun editPlayer() {
        var membertype=""
        if(member_type.selectedItem.toString()=="Batsman")
            membertype="1"

        if(member_type.selectedItem.toString()=="Bowler")
            membertype="2"
        if(member_type.selectedItem.toString()=="All-rounder")
            membertype="3"
        if(member_type.selectedItem.toString()=="Wicket-keeper")
            membertype="4"
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@AddPlayerActivity)?.create(
                RetrofitMethods::class.java
            )
            var userdat= UserData(this@AddPlayerActivity)
            var stateId=""
            var cityId=""
//            stateList.forEach {
//                if(it.t10_state_name==state.selectedItem.toString())
//                    stateId=it.t10_state_id
//            }
//            citiesList.forEach {
//                if(it.t10_city_id==city.selectedItem.toString())
//                    cityId=it.t10_city_id
//            }
            retrofitMethod?.editPlayer("0","0",name.text.toString(),email.text.toString(),age.text.toString(),phone.text.toString(),membertype,memberId)?.enqueue(
                object : Callback<GenericResponse?> {
                    override fun onResponse(call: Call<GenericResponse?>, response: Response<GenericResponse?>) {
                        loader.visibility = View.INVISIBLE
                        if (response.isSuccessful) {
                            //response.body()!!
                            finish()
                            Toast.makeText(this@AddPlayerActivity, "Player added successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getString("msg")
                            Toast.makeText(this@AddPlayerActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse?>, t: Throwable) {
                        loader.visibility = View.INVISIBLE
                        Toast.makeText(this@AddPlayerActivity, t.message, Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

}