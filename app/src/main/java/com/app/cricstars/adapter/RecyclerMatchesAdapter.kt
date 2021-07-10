package com.app.cricstars.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.MatchDetailsActivity
import com.app.cricstars.R
import com.app.cricstars.model.MatchDetail
import com.app.cricstars.utils.MyUtils
import com.app.cricstars.utils.MyUtils.LIGHTSCORECARD
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RecyclerMatchesAdapter(var context: Activity, var darkOrLight: Int,var data:MatchDetail?,var fragmentName:String?) :
    RecyclerView.Adapter<RecyclerMatchesAdapter.MyViewHolder?>() {
    //declare interface
    private var onClick: OnItemClicked? = null
    private val tvleaguename: TextView? = null
    private val tvovers1: TextView? = null
    private val tvovers2: TextView? = null
    private val linear: LinearLayout? = null
    private val tvmatchstatus: TextView? = null
    private val tvmatchinfo: TextView? = null
    private val tvmatchtype: TextView? = null
    private val tvteam1name: TextView? = null
    private val tvteam1score: TextView? = null
    private val tvteam1overs: TextView? = null
    private val tvteam2name: TextView? = null
    private val tvteam2score: TextView? = null
    private val tvteam2overs: TextView? = null
    private val tvtoss: TextView? = null

    //make interface like this
    interface OnItemClicked {
        fun onItemClick(selectedFilter: String?)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView? = null
        var tv_match_info: TextView? = null
        var tv_league_name: TextView? = null
        var tv_team1_name: TextView? = null
        var tv_team1_score: TextView? = null
        var tv_team2_name: TextView? = null
        var tv_status: TextView? = null
        var tv_team1_overs: TextView? = null
        var tv_team2_score: TextView? = null
        var img: ImageView? = null
        var linear: LinearLayout

        init {
            tvTitle=itemView.findViewById(R.id.tv_match_type)
            tv_match_info=itemView.findViewById(R.id.tv_match_info)
            tv_league_name=itemView.findViewById(R.id.tv_league_name)
            tv_team1_name=itemView.findViewById(R.id.tv_team1_name)
            tv_team1_score=itemView.findViewById(R.id.tv_team1_score)
            tv_team1_overs=itemView.findViewById(R.id.tv_team1_overs)
            tv_team2_name=itemView.findViewById(R.id.tv_team2_name)
            tv_team2_score=itemView.findViewById(R.id.tv_team2_score)
            tv_status=itemView.findViewById(R.id.tv_status)
            linear = itemView.findViewById(R.id.linear)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView: View? = null
//        if (darkOrLight == LIGHTSCORECARD) {
//            itemView = LayoutInflater.from(parent.context)
//                .inflate(R.layout.custom_matches_scores, parent, false)
//        } else {
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_matches_scores_dark, parent, false)
//        }
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

//        val dateStr = data?.data?.get(position)?.match_details?.t10_match_date //2020-02-07
//
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
//        val matchDate = dateFormat.parse(dateStr)
//        val currentDateStr = dateFormat.format(Calendar.getInstance().time)
//        val currentDate = dateFormat.parse(currentDateStr)
//        if (fragmentName == "live") {
//
//            if (matchDate.equals(currentDate)) {


                holder.tvTitle?.setText(data?.data?.get(position)?.t10_match_title)
                holder.tv_league_name?.setText(data?.data?.get(position)?.venue_details?.t10_venue_name)
                holder.tv_match_info?.setText(data?.data?.get(position)?.match_details?.t10_match_date)
                holder.tv_team1_name?.setText(data?.data?.get(position)?.teamAname)
//         holder.tv_team1_overs?.setText(data?.data?.get(position)?.match_details.)
                holder.tv_team2_name?.setText(data?.data?.get(position)?.teamBname)
                if (data?.data?.get(position)?.match_details?.t10_toss_won == data?.data?.get(position)?.t10_team_A_id) {
                    if (data?.data?.get(position)?.teamAname != null) {
                        if(data?.data?.get(position)?.match_details!!.t10_bat_bowl=="1")
                        holder.tv_status?.setText(data?.data?.get(position)?.teamAname + " won the toss and elected to bat")
                        else
                            holder.tv_status?.setText(data?.data?.get(position)?.teamAname + " won the toss and elected to bowl")

                    }
                    else
                        holder.tv_status?.setText("")
                } else {
                    if (data?.data?.get(position)?.teamBname != null) {
                        Log.e("TAG", "onBindViewHolder: "+data?.data?.get(position)?.match_details  )
                        if(data?.data?.get(position)?.match_details!!.t10_bat_bowl=="1")
                        holder.tv_status?.setText(data?.data?.get(position)?.teamBname + " won the toss and elected to bat")
                        else
                            holder.tv_status?.setText(data?.data?.get(position)?.teamBname + " won the toss and elected to bowl")


                    }
                    else
                        holder.tv_status?.setText("")

                }
                holder.linear.setOnClickListener {

                Intent(
                    context,
                    MatchDetailsActivity::class.java
                ).also {
                    it.putExtra("match_id",data?.data?.get(position)?.t10_match_id)
                    context.startActivity(it)
                }

                }

            }



    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

    override fun getItemCount(): Int {
        if(data!=null)
            if(data?.data!=null)
            return data?.data!!.size
        else
            return 0
        else
        return 0

    }


}