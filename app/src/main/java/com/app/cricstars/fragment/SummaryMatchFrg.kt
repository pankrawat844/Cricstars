package com.app.cricstars.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.cricstars.R
import com.app.cricstars.model.MatchScoreDetail
import com.truecaller.android.sdk.TruecallerSDK.init
import kotlinx.android.synthetic.main.fr_summary_match.*

class SummaryMatchFrg : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: MatchScoreDetail? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    var view1: View? = null
    var teamName:TextView?=null
    var total:TextView?=null
    var totalOvers:TextView?=null
    var currentRunrate:TextView?=null
    var requireRunrate:TextView?=null
    var matchStatus:TextView?=null
    var batsman1:TextView?=null
    var batsman2:TextView?=null
    var runs1:TextView?=null
    var balls1:TextView?=null
    var fours1:TextView?=null
    var sixes1:TextView?=null
    var strikeRate1:TextView?=null
    var runs2:TextView?=null
    var balls2:TextView?=null
    var fours2:TextView?=null
    var sixes2:TextView?=null
    var strikeRate2:TextView?=null
    var patnership:TextView?=null
    var target:TextView?=null
    var bowlerName:TextView?=null
    var bowlerOver:TextView?=null
    var bowlerMaiden:TextView?=null
    var bowlerRuns:TextView?=null
    var bowlerWickets:TextView?=null
    var bowleEco:TextView?=null
    var matchResult:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
                mParam1 = arguments?.getSerializable(ARG_PARAM1) as MatchScoreDetail?
                      mParam2 = getArguments()?.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_summary_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view1=view
        init()
        var data=mParam1!!.data
        if(data!!.inningOneCompleted)
        teamName!!.text=data!!.bowling_team_name
        else
            teamName!!.text=data!!.bating_team_name

        if(data!!.inningOneCompleted)
            total!!.text=data!!.inning2Teamscore.toString()+"/"+data!!.inning2wicket.toString()
        else
            total!!.text=data!!.inning1Teamscore.toString()+"/"+data!!.inning1wicket.toString()

        if(data!!.inningOneCompleted==true)
            totalOvers!!.text="("+data!!.inning2OversBowled.toString()+" OV)"
        else
            totalOvers!!.text="("+data!!.inning1OversBowled.toString()+" OV)"

        if(data!!.inningOneCompleted){
            currentRunrate!!.text=(data!!.inning2Teamscore/data!!.inning2OversBowled).toString()
            requireRunrate!!.text=(data!!.inning1Teamscore/data!!.inning1OversBowled).toString()
        }
        else {
            currentRunrate!!.text="CRR "+(data!!.inning1Teamscore/data!!.inning1OversBowled).toString()
            requireRunrate!!.text="REQ 0.0"

        }

        if(data!!.inningTwoCompleted){
            matchResult!!.text=data!!.matchResult
        }

        if(data!!.inningOneCompleted)
            matchStatus!!.text=data!!.toss_string
        else
            matchStatus!!.text=data!!.toss_string

        if(data!!.inningOneCompleted) {
            if (data!!.inning1Teamscore!=null)
                target!!.text = "Target " + data!!.inning1Teamscore.plus(1)
        }
        else
            matchStatus!!.text=data!!.toss_string
            batsman1!!.text=data!!.strikeBatsman
            runs1!!.text=data!!.strikeBatsmanRun.toString()
            balls1!!.text=data!!.strikeBatsmanBall.toString()
            fours1!!.text=data!!.strikeBatsmanFour.toString()
            sixes1!!.text=data!!.strikeBatsmanSix.toString()
        if(data!!.strikeBatsmanRun!=0 && data.strikeBatsmanBall!=0)
            strikeRate1!!.text=((data!!.strikeBatsmanRun/data!!.strikeBatsmanBall)*100).toString()

        batsman2!!.text=data!!.nonstrikeBatsman
        runs2!!.text=data!!.nonstrikeBatsmanRun.toString()
        balls2!!.text=data!!.nonstrikeBatsmanBall.toString()
        fours2!!.text=data!!.nonstrikeBatsmanFour.toString()
        sixes2!!.text=data!!.nonstrikeBatsmanSix.toString()
        if(data!!.nonstrikeBatsmanRun!=0 && data.nonstrikeBatsmanBall!=0)
        strikeRate2!!.text=((data!!.nonstrikeBatsmanRun/data!!.nonstrikeBatsmanBall)*100).toString()

        bowlerName!!.text=data!!.currentBowler
        bowlerOver!!.text=data!!.currentBowlerOver.toString()
        bowlerMaiden!!.text="0"
        bowlerRuns!!.text=data!!.currentBowlerRuns.toString()
        bowlerWickets!!.text=data!!.currentBowlerWicket.toString()
        val overWithDec:Double=(data.currentBowlerRuns.div(data.currentBowlerBalls.toDouble()))

        bowleEco!!.text=(overWithDec*6).toString()

    }

    private fun init() {
        teamName=view1!!.findViewById(R.id.teamName)
        total=view1!!.findViewById(R.id.total)
        totalOvers=view1!!.findViewById(R.id.totalOvers)
        totalOvers=view1!!.findViewById(R.id.totalOvers)
        currentRunrate=view1!!.findViewById(R.id.currentRunrate)
        requireRunrate=view1!!.findViewById(R.id.reqRunrate)
        matchStatus=view1!!.findViewById(R.id.matchStatus)
        batsman1=view1!!.findViewById(R.id.batsman1)
        runs1=view1!!.findViewById(R.id.runs1)
        balls1=view1!!.findViewById(R.id.balls1)
        fours1=view1!!.findViewById(R.id.fours1)
        sixes1=view1!!.findViewById(R.id.sixes1)
        strikeRate1=view1!!.findViewById(R.id.strike_rate1)
        batsman2=view1!!.findViewById(R.id.batsman2)
        runs2=view1!!.findViewById(R.id.runs2)
        balls2=view1!!.findViewById(R.id.balls2)
        fours2=view1!!.findViewById(R.id.fours2)
        sixes2=view1!!.findViewById(R.id.sixes2)
        strikeRate2=view1!!.findViewById(R.id.strike_rate2)
        bowlerName=view1!!.findViewById(R.id.bowlerName)
        bowlerOver=view1!!.findViewById(R.id.overs)
        bowlerMaiden=view1!!.findViewById(R.id.maiden)
        bowlerRuns=view1!!.findViewById(R.id.runs)
        bowlerWickets=view1!!.findViewById(R.id.wickets)
        bowleEco=view1!!.findViewById(R.id.eco)
        target=view1!!.findViewById(R.id.target)
        matchResult=view1!!.findViewById(R.id.matchResult)

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
         * @return A new instance of fragment SummaryMatchFrg.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: MatchScoreDetail?, param2: String?): SummaryMatchFrg {
            val fragment = SummaryMatchFrg()
            val args = Bundle()
            args.putSerializable(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.setArguments(args)
            return fragment
        }
    }
}
