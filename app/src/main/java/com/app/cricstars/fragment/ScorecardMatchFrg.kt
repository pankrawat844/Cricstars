package com.app.cricstars.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.R
import com.app.cricstars.adapter.Recycler2BattingAdapter
import com.app.cricstars.adapter.Recycler2BowlingAdapter
import com.app.cricstars.adapter.RecyclerBattingAdapter
import com.app.cricstars.adapter.RecyclerBowlingAdapter
import com.app.cricstars.model.InningScore
import com.app.cricstars.model.MatchScoreDetail
import kotlinx.android.synthetic.main.fr_scorecard_match.*
import kotlinx.android.synthetic.main.fr_scorecard_match.view.*

class ScorecardMatchFrg : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: MatchScoreDetail? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mParam1 = arguments?.getSerializable(ARG_PARAM1) as MatchScoreDetail?
        }
    }

    var batting1: RecyclerView? = null
    var batting2: RecyclerView? = null
    var bowling1: RecyclerView? = null
    var bowling2: RecyclerView? = null
    var view1: View? = null
    var relTeam1: RelativeLayout? = null
    var relTeam2: RelativeLayout? = null
    var hideTeam1: LinearLayout? = null
    var hideTeam2: LinearLayout? = null
    var ivDropdown1: ImageView? = null
    var ivDropdown2: ImageView? = null
    var linearTeam1: LinearLayout? = null
    var tvTeam1Name: TextView? = null
    var tvTeam2Name: TextView? = null
    var inning1fallOfwickets: TextView? = null
    var inning2fallOfwickets: TextView? = null
   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fr_scorecard_match, container, false)
        init()
       tvTeam1Name!!.text=mParam1?.data?.bating_team_name
       tvTeam2Name!!.text=mParam1?.data?.bowling_team_name

           if(mParam1!!.data!!.inning1fallofwicket.isNotEmpty()) {
               inning1fallOfwickets!!.text=  mParam1?.data?.inning1fallofwicket!!.dropLast(1)


           }

       if(mParam1!!.data!!.inning2fallofwicket.isNotEmpty()) {
           inning2fallOfwickets!!.text=  mParam1?.data?.inning2fallofwicket!!.dropLast(1)

       }
        hideTeam1!!.visibility = View.GONE
        hideTeam2!!.visibility = View.VISIBLE
if(mParam1!!.data!!.inning1Score!=null)
    settingRecyclerView(batting1, 0, mParam1!!.data!!.inning1Score?.asReversed())

       mParam1!!.data!!.inning2Score?.let { settingRecyclerView2(batting2, 0, it.reversed())}
       if(mParam1!!.data!!.inning1Overs!=null)
        settingRecyclerViewBowling(bowling1, 1,mParam1!!.data!!.inning1Overs!!.asReversed()!!)
       if(mParam1!!.data!!.inning2Overs!=null)
        settingRecyclerViewBowling2(bowling2, 1,mParam1!!.data!!.inning2Overs!!.asReversed()!!)
//        settingRecyclerView(bowling2, 1)
        relTeam1!!.setOnClickListener { hidingOnClick(hideTeam1, ivDropdown1) }
        relTeam2!!.setOnClickListener { hidingOnClick(hideTeam2, ivDropdown2) }
        return view1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tv_score.text = "${mParam1?.data?.inning1Teamscore!!} /${mParam1!!.data!!.inning1wicket}"
        view.tv_score2.text = "${mParam1?.data?.inning2Teamscore!!} /${mParam1!!.data!!.inning2wicket}"
        if(mParam1?.data?.inning1OversBowled!!.toString().contains(".6"))
        view.tv_overs.text = "(${mParam1?.data?.inning1OversBowled!!.toInt()+1} Ov)"
        else
        view.tv_overs.text = "(${mParam1?.data?.inning1OversBowled!!} Ov)"
        if(mParam1?.data?.inning2OversBowled!!.toString().contains(".6"))
        view.tv_overs2.text = "(${mParam1?.data?.inning2OversBowled!!.toInt()+1} Ov)"
        else
        view.tv_overs2.text = "(${mParam1?.data?.inning2OversBowled!!} Ov)"
        view.extras.text = "${mParam1?.data?.inning1extras!!} (b ${mParam1!!.data!!.inning1bye},lb ${mParam1!!.data!!.inning1legbye},nb ${mParam1!!.data!!.inning1noball},wb ${mParam1!!.data!!.inning1wide})"
        view.extras2.text = "${mParam1?.data?.inning2extras!!} (b ${mParam1!!.data!!.inning2bye},lb ${mParam1!!.data!!.inning2legbye},nb ${mParam1!!.data!!.inning2noball},wb ${mParam1!!.data!!.inning2wide})"
    }

    fun hidingOnClick(rTeam: LinearLayout?, ivDrop: ImageView?) {
        if (rTeam!!.visibility == View.GONE) {
            rTeam.visibility = View.VISIBLE
            ivDrop!!.rotation = 270f
            //            linearTeam1 = new LinearLayout(new ContextThemeWrapper(getActivity(), R.style.ThemeOverlay_AppCompat_Dark), null, 0);
        } else {
            rTeam.visibility = View.GONE
            ivDrop!!.rotation = 90f
        }
    }

    fun settingRecyclerView(recycler: RecyclerView?, bowlOrBat: Int,data:List<MatchScoreDetail.Data.Inning1Score>?) {
        recycler?.isNestedScrollingEnabled = false
        recycler?.layoutManager = LinearLayoutManager(getActivity())
        recycler?.setAdapter(RecyclerBattingAdapter(getActivity()!!, bowlOrBat,data!!))
    }
fun settingRecyclerView2(recycler: RecyclerView?, bowlOrBat: Int,data:List<MatchScoreDetail.Data.Inning2Score>) {
        recycler?.isNestedScrollingEnabled = false
        recycler?.layoutManager = LinearLayoutManager(getActivity())
        recycler?.setAdapter(Recycler2BattingAdapter(getActivity()!!, bowlOrBat,data))
    }
    fun settingRecyclerViewBowling(recycler: RecyclerView?, bowlOrBat: Int,data:List<MatchScoreDetail.Data.Inning1Over>) {
        recycler?.isNestedScrollingEnabled = false
        recycler?.layoutManager = LinearLayoutManager(getActivity())
        recycler?.setAdapter(RecyclerBowlingAdapter(getActivity()!!, bowlOrBat,data))
    }
    fun settingRecyclerViewBowling2(recycler: RecyclerView?, bowlOrBat: Int,data:List<MatchScoreDetail.Data.Inning2Over>) {
        recycler?.isNestedScrollingEnabled = false
        recycler?.layoutManager = LinearLayoutManager(getActivity())
        recycler?.setAdapter(Recycler2BowlingAdapter(getActivity()!!, bowlOrBat,data))
    }
    fun init() {
        tvTeam1Name=view1!!.findViewById(R.id.tv_team1_name)
        tvTeam2Name=view1!!.findViewById(R.id.tv_team2_name)
        batting1 = view1!!.findViewById(R.id.recycler_batting_team1)
        bowling1 = view1!!.findViewById(R.id.recycler_bowling_team1)
        batting2 = view1!!.findViewById(R.id.recycler_batting_team2)
        bowling2 = view1!!.findViewById(R.id.recycler_bowling_team2)
        relTeam1 = view1!!.findViewById(R.id.relative_team1)
        relTeam2 = view1!!.findViewById(R.id.relative_team2)
        hideTeam1 = view1!!.findViewById(R.id.hide_team1)
        hideTeam2 = view1!!.findViewById(R.id.hide_team2)
        ivDropdown1 = view1!!.findViewById(R.id.iv_dropdown)
        ivDropdown2 = view1!!.findViewById(R.id.iv_dropdown2)
        linearTeam1 = view1!!.findViewById(R.id.linear_team1)
        inning1fallOfwickets = view1!!.findViewById(R.id.inning1fallOfwickets)
        inning2fallOfwickets = view1!!.findViewById(R.id.inning2fallOfwickets)
    }

    // TODO: Rename method, update argument and hook method into UI event
     fun onButtonPressed(uri: Uri?) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

   override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri?)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScorecardMatchFrg.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: MatchScoreDetail?): ScorecardMatchFrg {
            val fragment = ScorecardMatchFrg()
            val args = Bundle()
            args.putSerializable(ARG_PARAM1, param1)
            fragment.setArguments(args)
            return fragment
        }
    }
}