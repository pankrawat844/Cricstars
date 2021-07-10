package com.app.cricstars.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.AddPlayerActivity
import com.app.cricstars.R
import com.app.cricstars.TeamMemberActivity
import com.app.cricstars.fragment.TeamMembersFrg
import com.app.cricstars.model.Team
import com.app.cricstars.model.TeamMember
import com.app.cricstars.utils.MyUtils.PAGERFOR2SQUAD
import com.app.cricstars.utils.MyUtils.PAGERFORTEAMS

class RecyclerSquadAdapter(var context: Activity, var teamOrSquad: Int,var list:List<Team.Data>?,var list2:List<TeamMember.Data>?) :
    RecyclerView.Adapter<RecyclerSquadAdapter.MyViewHolder?>() {
    //declare interface
    private var onClick: OnItemClicked? = null

    //make interface like this
    interface OnItemClicked {
        fun onItemClick(selectedFilter: String?)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var img: ImageView
        var imgBatOrBall: ImageView
        var linear: LinearLayout? = null

        init {
                        linear = itemView.findViewById(R.id.linear);
            tvTitle = itemView.findViewById(R.id.tv_name)
            img = itemView.findViewById(R.id.img)
            imgBatOrBall = itemView.findViewById(R.id.img_ball_or_bat)
        }
    }

  override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_squad_details, parent, false)
        return MyViewHolder(itemView)
    }

  override  fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (teamOrSquad == PAGERFORTEAMS) {
            holder.tvTitle.text = list!!.get(position).t10_team_name
            holder.img.setImageResource(R.drawable.tmp)
            holder.imgBatOrBall.visibility = View.GONE
            holder.linear!!.setOnClickListener {
                val intent =Intent(context,TeamMemberActivity::class.java)
                intent.putExtra("team_id",list!!.get(position).t10_team_id)
                context.startActivity(intent)
            }
        } else {
            val data=list2!!.get(position)
            holder.tvTitle.text = data.t10_team_member_name
            holder.img.setImageResource(R.drawable.helmet)

            if (data.t10_member_type_id=="1") {
                 holder.imgBatOrBall.setImageResource(R.drawable.bat)
            }
            else if(data.t10_member_type_id=="2") {
                holder.imgBatOrBall.setImageResource(R.drawable.matches)
            }
            else if(data.t10_member_type_id=="3") {
                holder.imgBatOrBall.setImageResource(R.drawable.allrounder)
            }else {
                holder.imgBatOrBall.setImageResource(R.drawable.keeper)

            }
            holder.linear!!.setOnClickListener {
                val intent =Intent(context,AddPlayerActivity::class.java)
                intent.putExtra("player_data",data)
                context.startActivity(intent)
            }
        }
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

    override fun getItemCount(): Int {
        if(teamOrSquad==PAGERFORTEAMS)
        return list!!.size!!
        else if(teamOrSquad== PAGERFOR2SQUAD)
            return list2!!.size!!
        else
          return 0
    }

}