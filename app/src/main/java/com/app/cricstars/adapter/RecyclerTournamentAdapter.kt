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
import com.app.cricstars.TournamentDetailsActivty
import com.app.cricstars.model.Tournament
import com.squareup.picasso.Picasso

class RecyclerTournamentAdapter(
    var context: Activity,
    var ARG_SECTION_NUMBER: Int,
   var DATA:List<Tournament.Data>
) :
    RecyclerView.Adapter<RecyclerTournamentAdapter.MyViewHolder?>() {
    //declare interface
    private var onClick: OnItemClicked? = null

    //make interface like this
    interface OnItemClicked {
        fun onItemClick(selectedFilter: String?)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView? = null
        var place: TextView? = null
        var ongoing: TextView? = null
        var img: ImageView? = null
        var linear: RelativeLayout

        init {
            linear = itemView.findViewById(R.id.relative)
            img = itemView.findViewById(R.id.img)
            tvTitle = itemView.findViewById(R.id.tv_title)
            place = itemView.findViewById(R.id.place)
            ongoing = itemView.findViewById(R.id.ongoing)
        }
    }

  override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_tournament_tab, parent, false)
        return MyViewHolder(itemView)
    }

   override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val data=DATA.get(position)
       holder.tvTitle?.setText(data.tournament_name)
       holder.place?.setText(data.ground+","+data.city)
        holder.linear.setOnClickListener {
            val intent= Intent(
                context,
                TournamentDetailsActivty::class.java)
            intent.putExtra("tournament_id",data.tournament_id)
            context.startActivity(
               intent
                )

        }
       if(ARG_SECTION_NUMBER==2)
           holder.ongoing?.setText("Upcomming")
        Picasso.get().load("https://t-10.in/cricstars/uploads/"+data.tournament_banner).placeholder(R.drawable.tmp_tournaments).error(R.drawable.tmp_tournaments).fit().into(holder.img)
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

    override fun getItemCount(): Int {
        return DATA.size
    }

}