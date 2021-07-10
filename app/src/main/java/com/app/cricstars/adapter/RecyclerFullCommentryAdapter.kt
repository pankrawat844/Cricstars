package com.app.cricstars.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.R
import com.app.cricstars.model.Commentary
import java.text.DecimalFormat

class RecyclerFullCommentryAdapter(var context: Activity,var data: Commentary.Data) :
    RecyclerView.Adapter<RecyclerFullCommentryAdapter.MyViewHolder?>() {
    //declare interface
    private var onClick: OnItemClicked? = null

    //make interface like this
    interface OnItemClicked {
        fun onItemClick(selectedFilter: String?)
    }



   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_commentry_full, parent, false)
        return MyViewHolder(itemView)
    }

   override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

val commentary=data.commentary!!.get(position)
        holder.strikeBatsman!!.text=commentary.batsman1_name
        holder.nonstrikeBatsman!!.text=commentary.batsman2_name
        holder.runsNballs!!.text=commentary.batsman1_run.toString()+"("+commentary.batsman2_ball+")"
        holder.runsNballs2!!.text=commentary.batsman2_run.toString()+"("+commentary.batsman2_ball+")"

       holder.endOfover!!.text="End of Over:"+(commentary.totalBowls.div(6)).toString()+" Score "+commentary.totalRuns+"/"+commentary.totalWicket

       holder.bowlerName!!.text=commentary.bowler_name
       holder.oversDetail!!.text=(commentary.bowlerover).toString()+"-0-"+commentary.bowlerruns.toString()+"-"+commentary.bowlerwicket.toString()
       holder.totalOver!!.text=commentary.totalOvers.toString()
      var runs=commentary.runs.toInt()+commentary.extras_run.toInt()
       if(commentary.extras.isNullOrEmpty())
       holder.run!!.text=runs.toString()
       else {
           if(commentary.extras=="WD" || commentary.extras=="NB")
           {
               if(!commentary.totalOvers.toString().contains(".1") && !commentary.totalOvers.toString().contains(".6")) {
                   val df = DecimalFormat("#.#")
                   Log.e("fd",(commentary.totalOvers.plus(0.1)).toString())

                   holder.totalOver!!.text = (df.format(commentary.totalOvers.plus(0.1))).toString()
               }else if(commentary.totalOvers.toString().contains(".6")){
                   val df = DecimalFormat("#.#")

                   holder.totalOver!!.text = (df.format(commentary.totalOvers.toInt().plus(1.0))).toString()
               }
           }
           holder.run!!.text = commentary.extras
       }

       holder.commantary!!.text= commentary.bowler_name+" to "+commentary.batsman1_name+", $runs runs"
       if(commentary.totalOvers.toString().contains(".6") && commentary.bowlerover.toString().contains(".6"))
           holder.header!!.visibility=View.VISIBLE
   }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return data.commentary!!.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var header: LinearLayout? = null
        var ballbyLayout: RelativeLayout? = null
        var strikeBatsman: TextView? = null
        var nonstrikeBatsman: TextView? = null
        var endOfover: TextView? = null
        var runsNballs: TextView? = null
        var runsNballs2: TextView? = null
        var bowlerName: TextView? = null
        var oversDetail: TextView? = null
        var totalOver: TextView? = null
        var run: TextView? = null
        var commantary: TextView? = null
        init {
            header = itemView.findViewById(R.id.headerLayout)
            ballbyLayout = itemView.findViewById(R.id.ballByBallLayout)
            strikeBatsman=itemView.findViewById(R.id.strikeBatsman)
            nonstrikeBatsman=itemView.findViewById(R.id.nonstrikeBatsman)
            endOfover=itemView.findViewById(R.id.endofOver)
            runsNballs=itemView.findViewById(R.id.runsNballs)
            runsNballs2=itemView.findViewById(R.id.runsNballs2)
            bowlerName=itemView.findViewById(R.id.bowlerName)
            oversDetail=itemView.findViewById(R.id.oversDetail)

            totalOver=itemView.findViewById(R.id.tv_over)
            run=itemView.findViewById(R.id.run)
            commantary=itemView.findViewById(R.id.commantary)
            this.setIsRecyclable(false)
        }
    }



}
