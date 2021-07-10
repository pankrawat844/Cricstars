package com.app.cricstars.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.app.cricstars.R
import com.app.cricstars.SelectTeamActivity
import com.app.cricstars.adapter.RecyclerTeamAdapter
import com.app.cricstars.model.Team
import com.app.cricstars.retrofit.OnRecylerclerClickListener
import kotlinx.android.synthetic.main.fragment_team.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TeamFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyTeamFragment : Fragment(),OnRecylerclerClickListener {
    // TODO: Rename and change types of parameters
    private var teamData:  ArrayList<Team.Data>? = null
    private var param2: Int? = null
    private var list: List<Team.Data>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            teamData = it.getSerializable(ARG_PARAM1) as ArrayList<Team.Data>
            param2 = it.getInt(ARG_PARAM2,0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var adapter= RecyclerTeamAdapter(
            activity!!,param2!!, teamData!!,this
        )
        recylerView?.layoutManager=GridLayoutManager(activity,3)
        recylerView?.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(recylerView)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TeamFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: ArrayList<Team.Data>, param2: Int) =
            MyTeamFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(teamNo: Int, data: Team.Data?) {
            val intent= Intent()
            intent.putExtra("data", data)
            activity!!.setResult(0, intent)
            activity!!.finish()

    }
}