package com.app.cricstars.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.R
import com.app.cricstars.SelectTeamActivity
import com.app.cricstars.model.Team
import com.app.cricstars.retrofit.OnRecylerclerClickListener


class RecyclerTeamAdapter(var context: Activity, var teamOne: Int, var team: List<Team.Data>,var listner:OnRecylerclerClickListener) :
    RecyclerView.Adapter<RecyclerTeamAdapter.MyViewHolder?>() {
    //declare interface
    private var onClick: OnItemClicked? = null

    //make interface like this
    interface OnItemClicked {
        fun onItemClick(selectedFilter: String?)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var firstTxt: TextView
        var tvName: TextView
        var img: ImageView? = null
        var linear: LinearLayout

        init {
            linear = itemView.findViewById(R.id.linear)
            firstTxt = itemView.findViewById(R.id.firstTxt)
            tvName = itemView.findViewById(R.id.tv_name)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_team, parent, false)
        return MyViewHolder(itemView)
    }

           override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
                    val team=team[position]
                holder.linear.setOnClickListener {
//                    val intent=Intent(context,SelectTeamActivity::class.java)
//                    intent.putExtra("data", team)
//                     context ?.setResult(teamOne, intent)
//                    ( context as SelectTeamActivity)?.finish()
                    listner.onClick(teamOne,team)
                }

                holder.tvName.setText(team.t10_team_name)
                holder.firstTxt.setText(team.t10_team_name.first().toString().capitalize())
            }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    fun filterList(filterdNames: List<Team.Data>) {
        this.team = filterdNames
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return team.size
    }
}
