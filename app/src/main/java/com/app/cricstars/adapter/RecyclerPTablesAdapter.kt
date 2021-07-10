package com.app.cricstars.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.R

class RecyclerPTablesAdapter(var context: Activity) :
    RecyclerView.Adapter<RecyclerPTablesAdapter.MyViewHolder?>() {
    //declare interface
    private var onClick: OnItemClicked? = null

    //make interface like this
    interface OnItemClicked {
        fun onItemClick(selectedFilter: String?)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView? = null
        var img: ImageView? = null
        var linearHide: LinearLayout
        var tvTeamGroupHide: TextView

        init {
            linearHide = itemView.findViewById(R.id.hide)
            tvTeamGroupHide = itemView.findViewById(R.id.tv_title)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_points_table, parent, false)
        return MyViewHolder(itemView)
    }

   override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == 0) {
            holder.linearHide.visibility = View.VISIBLE
            holder.tvTeamGroupHide.visibility = View.VISIBLE
        } else {
            holder.linearHide.visibility = View.GONE
            holder.tvTeamGroupHide.visibility = View.GONE
        }
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

    override fun getItemCount(): Int {
        return 5
    }

}
