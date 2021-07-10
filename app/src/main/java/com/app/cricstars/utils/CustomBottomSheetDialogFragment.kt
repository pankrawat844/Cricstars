package com.app.cricstars.utils

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import com.app.cricstars.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomBottomSheetDialogFragment:BottomSheetDialogFragment(),View.OnClickListener {
    fun CustomBottomSheetDialogFragment() {}
    var ids = intArrayOf(
        R.id.run0,
        R.id.run1,
        R.id.run2,
        R.id.run3,
        R.id.run4,
        R.id.run5,
        R.id.run6,
        R.id.run7
    )
    var tv0: TextView? = null
    var tv1:TextView? = null
    var tv2:TextView? = null
    var tv3:TextView? = null
    var tv4:TextView? = null
    var tv5:TextView? = null
    var tv6:TextView? = null
    var tv7:TextView? = null
    var tvs = arrayOf(tv0, tv1, tv2, tv3, tv4, tv5, tv6, tv7)
    var runs = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
    var j = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.custom_extra_run_sheet, container, false)
        val tvSubmit = v.findViewById<TextView>(R.id.tv_submit)
        for (i in ids.indices) {
            tvs[i] = v.findViewById(ids[i])
            j = i
            tvs[i]!!.setOnClickListener(this)
        }
        val gv = v.findViewById<GridView>(R.id.gv)
        val adapter = ArrayAdapter(
            v.context, android.R.layout.simple_list_item_1,
            v.context.resources.getStringArray(R.array.extra_run)
        )
        gv.adapter = adapter
        gv.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l -> }
        return v
    }

    var extraDialog: ExtraDialog? = null
    var run = 0
    override fun onClick(view: View) {
        for (i in tvs.indices) {
            if (tvs[i] === view) {
                extraDialog!!.extraRun(runs[i])
                run = runs[i]
            }
        }
    }

    interface ExtraDialog {
        fun extraRun( run: Int)
    }

    fun setRuns(extraDialog: ExtraDialog?) {
        this.extraDialog = extraDialog
    }

    // make sure the Activity implemented it
   override fun onAttach(activity: Activity) {
        super.onAttach(activity!!)
    }
}