package com.app.cricstars

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.cricstars.model.Team
import com.app.cricstars.retrofit.OnRecylerclerClickListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_select_team.*

class SelectTeamActivity : AppCompatActivity(),OnRecylerclerClickListener {
    var team1:Team.Data?=null
    var team2:Team.Data?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_team)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
       //supportActionBar!!.title = "Select teams"

        (findViewById(R.id.tv_next) as TextView).setOnClickListener {
            if(team1Name.text.toString().equals("Team 1") || team2Name.text.toString().equals("Team 2"))
                Toast.makeText(this@SelectTeamActivity, "Please select team 1 or team 2", Toast.LENGTH_SHORT).show()
          else

                Intent(
                    this@SelectTeamActivity,
                    OvernLocationActivity::class.java
                ).also {
                    it.putExtra("team1",team1)
                    it.putExtra("team2",team2)
                    startActivity(it)
                }

        }

        (findViewById<CircleImageView>(R.id.cv_team1)).setOnClickListener {

                Intent(
                    this@SelectTeamActivity,
                    AllTeamActivity::class.java
                ).also {
                    it.putExtra("team",0)
                    startActivityForResult(it,0)
                }

        }
        (findViewById<CircleImageView>(R.id.cv_team2)).setOnClickListener {
            Intent(
                this@SelectTeamActivity,
                AllTeamActivity::class.java
            ).also {
                it.putExtra("team",1)
                startActivityForResult(it,1)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val team = data?.getSerializableExtra("data")!! as Team.Data
            if (requestCode == 0 && resultCode == 0) {
                 team1=team
                team1Name.text = team.t10_team_name
            }else if (requestCode == 1 && resultCode == 0) {
                team2Name.text = team.t10_team_name
                team2=team
            }
        }
    }

    override fun onClick(teamNo:Int,data: Team.Data?) {
        if(teamNo==0){
            team1=data
            team1Name.setText(team1!!.t10_team_name)
        }else{
            team2=data
            team2Name.setText(team2!!.t10_team_name)

        }
    }
}