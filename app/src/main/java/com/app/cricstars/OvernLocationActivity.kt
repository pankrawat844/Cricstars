package com.app.cricstars

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.app.cricstars.model.GenericResponse
import com.app.cricstars.model.Players
import com.app.cricstars.model.Team
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import kotlinx.android.synthetic.main.activity_overn_location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OvernLocationActivity : AppCompatActivity() {
    private var bowlerList: List<Players.Data>?=null
    private var batsmanList: List<Players.Data>?=null
    private var team1List=arrayListOf<Players.Data>()
    private var team2List=arrayListOf<Players.Data>()
    var batsmanListName = arrayListOf<String>()
    var batsmanListName2 = arrayListOf<String>()
    var bowlerListName = arrayListOf<String>()
    lateinit var batsmanAdapter:ArrayAdapter<String>
    lateinit var batsmanAdapter2:ArrayAdapter<String>
    lateinit var bowlerAdapter:ArrayAdapter<String>
    lateinit var team1:Team.Data
    lateinit var team2:Team.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overn_location)
         team1=intent.getSerializableExtra("team1") as Team.Data
         team2=intent.getSerializableExtra("team2") as Team.Data
        team1Name.setText(team1.t10_team_name)
        team2Name.setText(team2.t10_team_name)
        val teamList= arrayOf("Toss Won",team1.t10_team_name,team2.t10_team_name)
        toss_won.adapter=ArrayAdapter(this@OvernLocationActivity,android.R.layout.simple_dropdown_item_1line ,teamList)
         batsmanAdapter=ArrayAdapter(
            this@OvernLocationActivity,
            android.R.layout.simple_dropdown_item_1line,
            batsmanListName
        )
        batsman1.adapter=batsmanAdapter
        batsmanAdapter2=ArrayAdapter(
            this@OvernLocationActivity,
            android.R.layout.simple_dropdown_item_1line,
            batsmanListName2
        )
        batsman2.adapter=batsmanAdapter2
        bowlerAdapter=ArrayAdapter(
            this@OvernLocationActivity,
            android.R.layout.simple_dropdown_item_1line,
            bowlerListName
        )
        bowler.adapter=bowlerAdapter


        choose_bat_bowl.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(toss_won.selectedItem.toString().equals(team1Name.text.toString())){
                    team1List.clear()
                    team2List.clear()
                    if(choose_bat_bowl.selectedItem.toString().equals("Bat")) {

                        getBatsmen(team1.t10_team_id)
                        getBowler(team2.t10_team_id)
                    }else
                    {
                        getBatsmen(team2.t10_team_id)
                        getBowler(team1.t10_team_id)
                    }
                }else{
                    if(choose_bat_bowl.selectedItem.toString().equals("Bat")) {
                        getBatsmen(team2.t10_team_id)
                        getBowler(team1.t10_team_id)

                    }else
                    {
                        getBatsmen(team1.t10_team_id)
                        getBowler(team2.t10_team_id)

                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
toss_won.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        team1List.clear()
        team2List.clear()
        choose_bat_bowl.setSelection(0)

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}

        tv_next.setOnClickListener {

            if (matchTite.text.toString().isNullOrEmpty())
                Toast.makeText(
                    this@OvernLocationActivity,
                    "Please enter match title",
                    Toast.LENGTH_SHORT
                ).show()
            else if (toss_won.selectedItem.toString() == "Toss Won")
                Toast.makeText(
                    this@OvernLocationActivity,
                    "Please select toss won",
                    Toast.LENGTH_SHORT
                ).show()
            else if (choose_bat_bowl.selectedItem.toString() == "Choose First")
                Toast.makeText(
                    this@OvernLocationActivity,
                    "Choose batting or bowling",
                    Toast.LENGTH_SHORT
                ).show()
            else if (overs.text.toString().isNullOrEmpty())
                Toast.makeText(this@OvernLocationActivity, "please enter overs", Toast.LENGTH_SHORT)
                    .show()
            else if (batsman1.selectedItem.toString() == "Select Batsman 1")
                Toast.makeText(
                    this@OvernLocationActivity,
                    "please select batsman 1",
                    Toast.LENGTH_SHORT
                ).show()
            else if (batsman2.selectedItem.toString() == "Select Batsman 2")
                Toast.makeText(
                    this@OvernLocationActivity,
                    "please select batsman 2",
                    Toast.LENGTH_SHORT
                ).show()
            else if (bowler.selectedItem.toString() == "Select Bowler")
                Toast.makeText(
                    this@OvernLocationActivity,
                    "please select bowler",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                addMatch()


            }
        }
    }

    private fun getBatsmen(teamId:String) {
        batsmanListName.clear()
        batsmanListName2.clear()
        batsmanListName.add(0,"Select Batsman 1")
        batsmanListName2.add(0,"Select Batsman 2")

//        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@OvernLocationActivity)?.create(
                RetrofitMethods::class.java
            )

            retrofitMethod?.getPlayers(teamId)?.enqueue(
                object : Callback<Players?> {
                    override fun onResponse(call: Call<Players?>, response: Response<Players?>) {
//                        loader.visibility = View.GONE
                        if (response.isSuccessful) {
                            if (response.body()?.success!!) {
                                 batsmanList = response.body()!!.data!!
                                for (name in batsmanList!!) {
//                                    if (name.t10_member_type_id == "1" || name.t10_member_type_id == "3" || name.t10_member_type_id == "4") {

                                        batsmanListName.add(name.t10_team_member_name)
                                        batsmanListName2.add(name.t10_team_member_name)
                                        team1List?.add(name)
                                    //}
                                }
                                batsmanAdapter.notifyDataSetChanged()
                                batsmanAdapter2.notifyDataSetChanged()
                            }else{

                                Toast.makeText(this@OvernLocationActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()

                            }
                        }else {
                            val msg =
                                    JSONObject(response.errorBody()?.string()).getString("msg")
                            Toast.makeText(this@OvernLocationActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Players?>, t: Throwable) {
//                        loader.visibility = View.GONE
                        Toast.makeText(this@OvernLocationActivity, t.message, Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }
    }

    private fun getBowler(teamId:String) {
        bowlerListName.clear()
        bowlerListName.add(0,"Select Bowler")
//        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@OvernLocationActivity)?.create(
                RetrofitMethods::class.java
            )

            retrofitMethod?.getPlayers(teamId)?.enqueue(
                object : Callback<Players?> {
                    override fun onResponse(call: Call<Players?>, response: Response<Players?>) {
//                        loader.visibility = View.GONE
                        if (response.isSuccessful) {
                            if (response.body()?.success!!) {
                                 bowlerList = response.body()!!.data!!
                                for (name in bowlerList!!) {
                                    team2List?.add(name)
                                    if(name.t10_member_type_id=="2" || name.t10_member_type_id=="3" ) {
                                        bowlerListName.add(name.t10_team_member_name)
                                    }
                                }
                                bowlerAdapter.notifyDataSetChanged()
                            }else{

                                Toast.makeText(this@OvernLocationActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()

                            }
                        }else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getString("msg")
                            Toast.makeText(this@OvernLocationActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Players?>, t: Throwable) {
//                        loader.visibility = View.GONE
                        Toast.makeText(this@OvernLocationActivity, t.message, Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }
    }

    private fun addMatch() {
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@OvernLocationActivity)?.create(
                RetrofitMethods::class.java
            )
            var toss=""
            var batOrbowl=""
            var batsman_1=""
            var batsman_2=""
            var bowlerid=""
            if(toss_won.selectedItem.toString() == team1.t10_team_name)
                toss=team1.t10_team_id
            else
                toss=team2.t10_team_id

            if(choose_bat_bowl.selectedItem.toString()=="Bat")
                batOrbowl="1"
            else
                batOrbowl="2"
           for(batsman in batsmanList!!){
               if(batsman.t10_team_member_name==batsman1.selectedItem.toString())
                   batsman_1=batsman.t10_team_member_id
               if(batsman.t10_team_member_name==batsman2.selectedItem.toString())
                   batsman_2=batsman.t10_team_member_id
           }

            for(bow in bowlerList!!){
                if(bow.t10_team_member_name==bowler.selectedItem.toString())
                    bowlerid=bow.t10_team_member_id
            }
            retrofitMethod?.addMatch(getSharedPreferences("app", MODE_PRIVATE).getString("id","")!!,matchTite.text.toString(),team1.t10_team_id,team2.t10_team_id,toss,overs.text.toString(),batOrbowl,batsman_1,batsman_2,bowlerid)?.enqueue(
                object : Callback<GenericResponse?> {
                    override fun onResponse(call: Call<GenericResponse?>, response: Response<GenericResponse?>) {
                        loader.visibility = View.GONE
                        if (response.isSuccessful) {
                            if (response.body()?.success!!) {

                                Toast.makeText(this@OvernLocationActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()

                                var toss = ""
                                var batsman_1: Players.Data? = null
                                var batsman_2: Players.Data? = null
                                var bowlerid: Players.Data? = null
                                var bat_team_id:String = ""
                                var bat_team_name:String = ""
                                var bowl_team_id:String = ""
                                var bowl_team_name:String = ""
                                if (toss_won.selectedItem.toString().equals(team1.t10_team_name))
                                    toss = team1.t10_team_name
                                else
                                    toss = team2.t10_team_name

                                for (batsman in batsmanList!!) {
                                    if (batsman.t10_team_member_name == batsman1.selectedItem.toString())
                                        batsman_1 = batsman
                                    if (batsman.t10_team_member_name == batsman2.selectedItem.toString())
                                        batsman_2 = batsman
                                }

                                for (bow in bowlerList!!) {
                                    if (bow.t10_team_member_name == bowler.selectedItem.toString())
                                        bowlerid = bow
                                }

                                if(toss_won.selectedItem.toString().equals(team1Name.text.toString())){
                                    if(choose_bat_bowl.selectedItem.toString().equals("Bat")) {
                                        bat_team_id=team1.t10_team_id
                                        bowl_team_id=team2.t10_team_id
                                        bat_team_name=team1.t10_team_name
                                        bowl_team_name=team2.t10_team_name
                                    }else
                                    {
                                        bat_team_id=team2.t10_team_id
                                        bowl_team_id=team1.t10_team_id
                                        bat_team_name=team2.t10_team_name
                                        bowl_team_name=team1.t10_team_name
                                    }
                                }else{
                                    if(choose_bat_bowl.selectedItem.toString().equals("Bat")) {
                                        bat_team_id=team2.t10_team_id
                                        bowl_team_id=team1.t10_team_id
                                        bat_team_name=team2.t10_team_name
                                        bowl_team_name=team1.t10_team_name
                                    }else
                                    {
                                        bat_team_id=team1.t10_team_id
                                        bowl_team_id=team2.t10_team_id
                                        bat_team_name=team1.t10_team_name
                                        bowl_team_name=team2.t10_team_name
                                    }
                                }
                                val intent = Intent(this@OvernLocationActivity, ManualScoringActivity::class.java)
                                intent.putExtra("team1", team1)
                                intent.putExtra("team2", team2)
                                intent.putExtra("batsman1", batsman_1)
                                intent.putExtra("batsman2", batsman_2)
                                intent.putExtra("bowler", bowlerid)
                                intent.putExtra("toss_won", toss)
                                intent.putExtra("bat_team_id",bat_team_id)
                                intent.putExtra("bat_team_name",bat_team_name)
                                intent.putExtra("bowl_team_id",bowl_team_id)
                                intent.putExtra("bowl_team_name",bowl_team_name)
                                intent.putExtra("team1List",team1List)
                                intent.putExtra("team2List",team2List)
                                intent.putExtra("match_id",response.body()?.match_id!!)
                                intent.putExtra("choose", choose_bat_bowl.selectedItem.toString())
                                intent.putExtra("inningNo",1)
                                startActivity(intent)
                                finish()
                            }else{

                                Toast.makeText(this@OvernLocationActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()

                            }
                        }else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getString("msg")
                            Toast.makeText(this@OvernLocationActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse?>, t: Throwable) {
                        loader.visibility = View.GONE
                        Toast.makeText(this@OvernLocationActivity, t.message, Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }
    }

}
