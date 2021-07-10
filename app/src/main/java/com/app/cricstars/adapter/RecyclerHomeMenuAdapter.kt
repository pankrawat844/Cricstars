package com.app.cricstars.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.R
import com.app.cricstars.TournamentTabActivity
import com.app.cricstars.matchtab.MatchesTabActivty

class RecyclerHomeMenuAdapter(var context: Activity) :
    RecyclerView.Adapter<RecyclerHomeMenuAdapter.MyViewHolder?>() {
    //declare interface
    private var onClick: OnItemClicked? = null

    //make interface like this
    interface OnItemClicked {
        fun onItemClick(selectedFilter: String?)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var img: ImageView
        var relative: RelativeLayout

        init {
            tvTitle = itemView.findViewById(R.id.tv_title)
            img = itemView.findViewById(R.id.img)
            relative = itemView.findViewById(R.id.relative)
        }
    }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_home_menu, parent, false)
        return MyViewHolder(itemView)
    }

    var strs = arrayOf("Matches", "Tournaments", "My Team", "My Matches", "My Profile")
    var svgImgs = intArrayOf(
        R.drawable.matches,
        R.drawable.tournament,
        R.drawable.myteam,
        R.drawable.matches,
        R.drawable.profile
    )

  override  fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvTitle.text = strs[position]
        holder.img.setImageResource(svgImgs[position])
        holder.relative.setOnClickListener {
            when (position) {
                0 -> context.startActivity(Intent(context, MatchesTabActivty::class.java))
                1 -> context.startActivity(Intent(context, TournamentTabActivity::class.java))
//                2 -> context.startActivity(Intent(context, MyTeamTabActivity::class.java))
//                3 -> context.startActivity(Intent(context, SelectTeamActivity::class.java))
//                4 -> context.startActivity(Intent(context, ProfileActivity::class.java))
                else -> {
                }
            }
        }
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

    override fun getItemCount(): Int {
        return strs.size
    }
}