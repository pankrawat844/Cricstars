package com.app.cricstars.adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.ManualScoringActivity
import com.app.cricstars.R
import com.app.cricstars.model.RunsModel
import java.util.*

class RecyclerRunsAdapter(
    var context: ManualScoringActivity,
    var arrayRun: ArrayList<RunsModel>
) :RecyclerView.Adapter<RecyclerRunsAdapter.MyViewHolder>() {
    //declare interface
    private var onClick: OnItemClicked? = null

    //make interface like this
    interface OnItemClicked {
        //        void onItemClick(int overRun);
        fun ballByBall(wicket: Int, run: Int, extra: Int)
    }


    var teamOrSquad = 0


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvRun: TextView
        var tvRunWords: TextView
        var img: ImageView? = null
        var linear: LinearLayout? = null

        init {
            //            linear = itemView.findViewById(R.id.linear);
            tvRun = itemView.findViewById(R.id.tv_run)
            tvRunWords = itemView.findViewById(R.id.tv_run_words)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_runs_manual, parent, false)
        return MyViewHolder(itemView)
    }

    private var overCount = 0
    private var totalRun = 0
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvRun.setText(arrayRun!![position].run)
        holder.tvRunWords.setText(arrayRun!![position].runWords)
        if (arrayRun!![position].runWords.equals("FOUR")) {
            holder.tvRun.setTextColor(Color.RED)
            //            holder.tvRunWords.setTextColor(Color.RED);
        }
        if (arrayRun!![position].runWords.equals("SIX")) {
            holder.tvRun.setTextColor(Color.RED)
            //            holder.tvRunWords.setTextColor(Color.RED);
        }
        if (arrayRun!![position].runWords.equals("OUT")) {
            holder.tvRun.setTextColor(Color.RED)
            //            holder.tvRunWords.setTextColor(Color.RED);

        }
        if (arrayRun!![position].runWords.equals("wd") || arrayRun!![position].runWords
                .equals("Nb") ||
            arrayRun!![position].runWords.equals("lb") ||
            arrayRun!![position].runWords.equals("b")
        ) {
            holder.tvRun.setTextColor(Color.BLACK)
            //            overCount -=1;
        }
        overCount += 1
        totalRun += arrayRun!![position].run!!.toInt()
        if (overCount > 5) {
//            Toast.makeText(context, "over complete"+totalRun, Toast.LENGTH_SHORT).show();
//            this.onClick.onItemClick(totalRun);
            return
        }

    }

    fun changeColor(holder: MyViewHolder) {
        holder.tvRun.setTextColor(Color.RED)
        holder.tvRunWords.text = "OUT"
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

    override fun getItemCount(): Int {
        return arrayRun!!.size
    }

}