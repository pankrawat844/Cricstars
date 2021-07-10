package com.app.cricstars.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.R

class RecyclerMVPAdapter(var context: Activity) : RecyclerView.Adapter<RecyclerMVPAdapter.MyViewHolder?>() {
    //declare interface
    private var onClick: OnItemClicked? = null

    //make interface like this
    interface OnItemClicked {
        fun onItemClick(selectedFilter: String?)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView? = null
        var tvRank: TextView
        var img: ImageView? = null
        var ivShare: ImageView

        init {
            tvRank = itemView.findViewById(R.id.tv_rank)
            ivShare = itemView.findViewById(R.id.iv_share)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.custom_mvp_matches, parent, false)
        return MyViewHolder(itemView)
    }

   override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvRank.text = "" + (position + 1)
        holder.ivShare.setOnClickListener {
            val imageUri = Uri.parse("android.resource://" + context.packageName
                    + "/drawable/" + "player_default")
            Toast.makeText(context, "under development", Toast.LENGTH_SHORT).show()
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, "MVP -Rank 1 - Surendar kumar - 1.6")
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.type = "image/*"
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(Intent.createChooser(shareIntent, "send"))
        }
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

    override fun getItemCount(): Int {
        return 10
    }

}
