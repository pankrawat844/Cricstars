package com.app.cricstars.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.cricstars.R
import com.app.cricstars.model.AddTeam
import com.app.cricstars.model.AllCities
import com.app.cricstars.model.State
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.app.cricstars.utils.UserData
import kotlinx.android.synthetic.main.activity_add_team.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTeamFragment:Fragment(){


var citiesList= arrayListOf<AllCities.Data>()
var stateList= arrayListOf<State.Data>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_add_team,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getStates()
        btnSave.setOnClickListener {
            if(memberType.selectedItem.toString()=="Member Type"){
                Toast.makeText(activity!!, "Please select member type", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if(teamName.text.toString().isNullOrEmpty()){
                tname.error = "Enter team name"
                tname.isErrorEnabled = true
            }else if(captainName.text.toString().isNullOrEmpty()){
                captainNameLayout.error = "Enter captain name"
                captainNameLayout.isErrorEnabled = true
            }else if(captainAge.text.toString().isNullOrEmpty()){
                captainAgeLayout.error = "Enter captain age"
                captainAgeLayout.isErrorEnabled = true
            }else if(captainOccupation.text.toString().isNullOrEmpty()){
                captainOccupationLayout.error = "Enter captain occupation"
                captainOccupationLayout.isErrorEnabled = true
            }else if(state.selectedItem.toString()==""){
                Toast.makeText(activity!!, "Please select state", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if(city.selectedItem.toString()==""){
                Toast.makeText(activity!!, "Please select city", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                addTeam()
            }
        }
        state.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                stateList.forEach {
                    if(it.t10_state_name==state.selectedItem.toString())
                        getcities(it.t10_state_id)
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }
private fun addTeam() {
    var member_type=""
    if(memberType.selectedItem.toString()=="Batsman")
        member_type="1"

    if(memberType.selectedItem.toString()=="Bowler")
        member_type="2"
    if(memberType.selectedItem.toString()=="All-rounder")
        member_type="3"
    if(memberType.selectedItem.toString()=="Wicket-keeper")
        member_type="4"
    loader.visibility= View.VISIBLE
    CoroutineScope(Dispatchers.Main).launch{
        val retrofitMethod = ApiBuilder.getRetrofitInstance(activity!!)?.create(
            RetrofitMethods::class.java
        )
        var userdat= UserData(activity!!)
        var stateId=""
        var cityId=""
        stateList.forEach {
            if(it.t10_state_name==state.selectedItem.toString())
                stateId=it.t10_state_id
        }
        citiesList.forEach {
            if(it.t10_city_id==city.selectedItem.toString())
                cityId=it.t10_city_id
        }
        retrofitMethod?.addTeam(stateId,cityId,teamName.text.toString(),captainName.text.toString(),userdat.email!!,userdat.password!!,userdat.mobile!!,captainOccupation.text.toString(),captainAge.text.toString(),member_type)?.enqueue(
            object : Callback<AddTeam?> {
                override fun onResponse(call: Call<AddTeam?>, response: Response<AddTeam?>) {
                    loader.visibility = View.INVISIBLE
                    if (response.isSuccessful) {
                       val data= response.body()?.data!!
                        Toast.makeText(activity!!, "Team added successfully", Toast.LENGTH_SHORT).show()
                        val intent= Intent()
                        intent.putExtra("data", data)
                        activity!!.setResult(0, intent)
                        activity!!.finish()
                    } else {
                        val msg =
                            JSONObject(response.errorBody()?.string()).getString("msg")
                        Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddTeam?>, t: Throwable) {
                    loader.visibility = View.INVISIBLE
                    Toast.makeText(activity!!, t.message, Toast.LENGTH_SHORT).show()

                }

            })
    }
}


private fun getcities(stateId:String) {
    CoroutineScope(Dispatchers.Main).launch {
        val retrofitMethod = ApiBuilder.getRetrofitInstance(activity!!)?.create(
            RetrofitMethods::class.java
        )
        val response = retrofitMethod?.getcities(stateId)?.enqueue(
            object : Callback<AllCities?> {
                override fun onResponse(call: Call<AllCities?>, response: Response<AllCities?>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data?.success!!) {
                            citiesList.clear()
                            citiesList.addAll(data.data!!)
                            var arraylist= arrayListOf<String>()
                            arraylist.clear()
                            citiesList.forEach {
                                arraylist.add(it.t10_city_name)
                            }
                            arraylist.add(0,"Select city")
                            city.adapter = ArrayAdapter(
                                activity!!,
                                android.R.layout.simple_dropdown_item_1line,
                                arraylist!!
                            )


                        }
//                                Toast.makeText(
//                                    this@AddTournamentActivity,
//                                    data?.data?.msg,
//                                    Toast.LENGTH_SHORT
//                                ).show()
                    } else {
                        val msg =
                            JSONObject(response.errorBody()?.string()).getString("msg")
                        Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<AllCities?>, t: Throwable) {
                    Toast.makeText(activity!!, t.message, Toast.LENGTH_SHORT)
                        .show()

                }

            })
    }


}


private fun getStates() {
    CoroutineScope(Dispatchers.Main).launch {
        val retrofitMethod = ApiBuilder.getRetrofitInstance(activity!!)?.create(
            RetrofitMethods::class.java
        )
        val response = retrofitMethod?.getstate()?.enqueue(
            object : Callback<State?> {
                override fun onResponse(call: Call<State?>, response: Response<State?>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data?.success!!) {
                            stateList.addAll(data.data!!)
                            var arraylist= arrayListOf<String>()
                            stateList.forEach {
                                arraylist.add(it.t10_state_name)
                            }
                            arraylist.add(0,"Select state")
                            state.setAdapter(
                                ArrayAdapter(
                                    activity!!,
                                    android.R.layout.simple_dropdown_item_1line,
                                    arraylist!!
                                )
                            )

                        }
//                                Toast.makeText(
//                                    this@AddTournamentActivity,
//                                    data?.data?.msg,
//                                    Toast.LENGTH_SHORT
//                                ).show()
                    } else {
                        val msg =
                            JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                .getString("msg")
                        Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<State?>, t: Throwable) {
                    Toast.makeText(activity!!, t.message, Toast.LENGTH_SHORT)
                        .show()

                }

            })
    }


}
}