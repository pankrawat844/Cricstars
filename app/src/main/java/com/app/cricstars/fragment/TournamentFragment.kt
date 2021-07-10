package com.app.cricstars.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.R
import com.app.cricstars.adapter.RecyclerTournamentAdapter
import com.app.cricstars.model.MatchDetail
import com.app.cricstars.model.Tournament
import java.text.SimpleDateFormat
import java.util.*

class TournamentFragment : Fragment() {
    var recyclerMatches: RecyclerView? = null
    private var mParam1: Int? = null
    private var mParam2: Tournament? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fr_tournament_tab, container, false)
        recyclerMatches = rootView.findViewById(R.id.recycler_matches)
        recyclerMatches!!.layoutManager = LinearLayoutManager(getActivity())
        var newdata:ArrayList<Tournament.Data>?= ArrayList()
        if(mParam1==1){
        for (value in mParam2?.data!!){
            val dateStr = value?.start_date//2020-02-07
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val matchDate = dateFormat.parse(dateStr)
            val currentDateStr = dateFormat.format(Calendar.getInstance().time)
            val currentDate = dateFormat.parse(currentDateStr)

            if (matchDate.equals(currentDate)) {
                newdata?.add(value)
            }
        }
        recyclerMatches!!.adapter = RecyclerTournamentAdapter(getActivity()!!, mParam1!!,
            newdata!!)
        }
        if(mParam1==2){
            for (value in mParam2?.data!!){
                val dateStr = value?.start_date//2020-02-07
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val matchDate = dateFormat.parse(dateStr)
                val currentDateStr = dateFormat.format(Calendar.getInstance().time)
                val currentDate = dateFormat.parse(currentDateStr)

                if (matchDate.after(currentDate)) {
                    newdata?.add(value)
                }
            }
            recyclerMatches!!.adapter = RecyclerTournamentAdapter(getActivity()!!, mParam1!!,
                newdata!!)
        }
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mParam1 = getArguments()?.getInt(ARG_SECTION_NUMBER)
            mParam2 = getArguments()?.getSerializable(DATA) as Tournament
        }
    }
    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val DATA = "data"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int, data: Tournament?): TournamentFragment {
            val fragment = TournamentFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            args.putSerializable(DATA, data)
            fragment.setArguments(args)
            return fragment
        }
    }
}