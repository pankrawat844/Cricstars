package com.app.cricstars.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.MatchDetailsActivity
import com.app.cricstars.R
import com.app.cricstars.model.InningScore
import com.app.cricstars.model.MatchScoreDetail
import com.app.cricstars.utils.ProfileDialog


class Recycler2BattingAdapter(var context: Activity, var bowlOrBat: Int, var data:List<MatchScoreDetail.Data.Inning2Score>) :
    RecyclerView.Adapter<Recycler2BattingAdapter.MyViewHolder?>() {
    //declare interface
    private var onClick: OnItemClicked? = null

    //make interface like this
    interface OnItemClicked {
        fun onItemClick(selectedFilter: String?)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvOutBy: TextView
        var tvName: TextView
        var runs: TextView
        var balls: TextView
        var fours: TextView
        var sixes: TextView
        var img: ImageView? = null
        var linear: LinearLayout

        init {
            linear = itemView.findViewById(R.id.linear)
            tvOutBy = itemView.findViewById(R.id.tv_out_by)
            tvName = itemView.findViewById(R.id.tv_name)
            runs = itemView.findViewById(R.id.runs)
            balls = itemView.findViewById(R.id.balls)
            fours = itemView.findViewById(R.id.fours)
            sixes=itemView.findViewById(R.id.sixes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_batting_score, parent, false)
        return MyViewHolder(itemView)
    }

   override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (bowlOrBat == 0) {
            holder.tvOutBy.visibility = View.VISIBLE
        } else {
            holder.tvOutBy.visibility = View.GONE
        }
       val model=data!!.get(position)!!
       holder.tvName.text=model.name
       holder.runs.text=model.runs.toString()
       holder.balls.text=model.ballsFaced.toString()
       holder.fours.text=model.fours.toString()
       holder.sixes.text=model.sixes.toString()
       if(model.bowlerName.isEmpty())
           holder.tvOutBy.text="not out"
       else
       holder.tvOutBy.text=model.wicket_type+" "+model.wicketHelperName+" b "+model.bowlerName
        holder.linear.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    MatchDetailsActivity::class.java
                )
            )
        }
        holder.tvName.setOnClickListener {
//            val cdd = ProfileDialog(context)
//            cdd.getWindow()
//                ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            cdd.show()
        }
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }



    override fun getItemCount(): Int {
        if (bowlOrBat == 0) {
          return  data.size
        } else {
            return data.size
        }
    }
}
