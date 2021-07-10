package com.app.cricstars

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.PagerSnapHelper
import com.app.cricstars.adapter.RecyclerTeamAdapter
import com.app.cricstars.model.Team
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import kotlinx.android.synthetic.main.activity_search_team.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchTeamActivity : AppCompatActivity() {
    private var list: List<Team.Data>?=null
    private var adapter: RecyclerTeamAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_team)
        getData()
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

        })
    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        val filterdNames: ArrayList<Team.Data> = ArrayList()

        //looping through existing elements
        for (s in list!!) {
            //if the existing elements contains the search input
            if (s.t10_team_name.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter?.filterList(filterdNames)
    }
    private fun getData() {
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@SearchTeamActivity)?.create(
                RetrofitMethods::class.java
            )

            retrofitMethod?.getTeams()?.enqueue(
                object : Callback<Team?> {
                    override fun onResponse(call: Call<Team?>, response: Response<Team?>) {
                        loader.visibility = View.GONE
                        if (response.isSuccessful) {
                            response.body()!!
                            recylerView?.setNestedScrollingEnabled(false)
                            list=response.body()!!.data!!
//                            adapter= RecyclerTeamAdapter(
//                                this@SearchTeamActivity, intent.getIntExtra(
//                                    "team",
//                                    0
//                                ), list!!
//                            ,null)
//                            recylerView?.adapter = adapter
//                            PagerSnapHelper().attachToRecyclerView(recylerView)
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data").getString("msg")
                            Toast.makeText(this@SearchTeamActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Team?>, t: Throwable) {
                        loader.visibility = View.GONE
                        Toast.makeText(this@SearchTeamActivity, t.message, Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }
    }

}