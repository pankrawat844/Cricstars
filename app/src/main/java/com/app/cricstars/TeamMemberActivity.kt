package com.app.cricstars

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.app.cricstars.adapter.RecyclerSquadAdapter
import com.app.cricstars.model.Team
import com.app.cricstars.model.TeamMember
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.app.cricstars.utils.MyUtils
import com.app.cricstars.utils.UserData
import kotlinx.android.synthetic.main.activity_team_member.*
import kotlinx.android.synthetic.main.fragment_recent_matches.*
import kotlinx.android.synthetic.main.fragment_recent_matches.loader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamMemberActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_member)
       fab.setOnClickListener {
           Intent(this@TeamMemberActivity,AddPlayerActivity::class.java).also {
               it.putExtra("team_id",intent.getStringExtra("team_id"))
               startActivity(it)
           }
       }
    }

    override fun onResume() {
        super.onResume()
        geteamPlayers()
    }

    private fun geteamPlayers() {
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@TeamMemberActivity)?.create(
                RetrofitMethods::class.java
            )
            var userdat= UserData(this@TeamMemberActivity)


            retrofitMethod?.getTeamMember(intent.getStringExtra("team_id")!!)?.enqueue(
                object : Callback<TeamMember?> {
                    override fun onResponse(call: Call<TeamMember?>, response: Response<TeamMember?>) {
                        loader.visibility = View.GONE
                        if (response.isSuccessful) {
                            response.body()!!
                            recycler_teams?.layoutManager = GridLayoutManager(this@TeamMemberActivity,2)
                            recycler_teams?.adapter = RecyclerSquadAdapter(this@TeamMemberActivity, MyUtils.PAGERFOR2SQUAD,null,response.body()!!.data)

                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getJSONObject("data").getString("msg")
                            Toast.makeText(this@TeamMemberActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<TeamMember?>, t: Throwable) {
                        loader.visibility = View.GONE
                        Toast.makeText(this@TeamMemberActivity, t.message, Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

}