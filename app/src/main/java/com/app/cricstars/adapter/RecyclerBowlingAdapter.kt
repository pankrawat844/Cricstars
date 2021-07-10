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
import androidx.core.util.rangeTo
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.MatchDetailsActivity
import com.app.cricstars.R
import com.app.cricstars.model.InningScore
import com.app.cricstars.model.MatchScoreDetail
import com.app.cricstars.utils.ProfileDialog
import java.math.RoundingMode
import java.text.DecimalFormat


class RecyclerBowlingAdapter(var context: Activity, var bowlOrBat: Int, var data:List<MatchScoreDetail.Data.Inning1Over>) :
    RecyclerView.Adapter<RecyclerBowlingAdapter.MyViewHolder?>() {
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
        var strikerate: TextView
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
            strikerate=itemView.findViewById(R.id.strike_rate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_batting_score, parent, false)
        return MyViewHolder(itemView)
    }

   override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            holder.tvOutBy.visibility = View.GONE

       val model=data!!.get(position)!!
       holder.tvName.text=model.name
       if(model.overs.toString().contains(".6"))
       holder.runs.text=(model.overs.toInt()+1).toString()
       else
           holder.runs.text=model.overs.toString()
       holder.balls.text= model.medians.toString()
       holder.fours.text= model.runs.toString()
       holder.sixes.text=model.wickets.toString()
       val overWithDec:Double=(model.runs.div(model.bowls.toDouble()))
       val df = DecimalFormat("#.##")
       df.roundingMode = RoundingMode.CEILING
       holder.strikerate.text=(df.format(overWithDec*6)).toString()
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
