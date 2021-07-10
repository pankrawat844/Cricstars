package com.app.cricstars.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.R
import com.app.cricstars.adapter.RecyclerMatchesAdapter
import com.app.cricstars.model.MatchDetail
import com.app.cricstars.utils.MyUtils.LIGHTSCORECARD


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [com.app.t10_scoring.fragments.ScoreAllMatchesFrg.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [com.app.t10_scoring.fragments.ScoreAllMatchesFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoreAllMatchesFrg : Fragment() {
    private val ARG_SECTION_NUMBER: String? = null

    // TODO: Rename and change types of parameters
    private var mParam1: MatchDetail? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mParam1 = getArguments()?.get(ARG_PARAM1) as MatchDetail
            mParam2 = getArguments()?.getString(ARG_PARAM2)
        }
    }

    var recyclerMatches: RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fr_score_all_matches, container, false)
        recyclerMatches = rootView.findViewById(R.id.recycler_matches)
        recyclerMatches?.setLayoutManager(LinearLayoutManager(getActivity()))
        recyclerMatches?.setAdapter(RecyclerMatchesAdapter(activity!!, LIGHTSCORECARD,mParam1,mParam2))
        return rootView
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri?) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    fun newInstance(sectionNumber: MatchDetail): ScoreAllMatchesFrg {
        val fragment = ScoreAllMatchesFrg()
        val args = Bundle()
        args.putSerializable("data", sectionNumber)
        fragment.setArguments(args)
        return fragment
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
        private const val ARG_PARAM1 = "data"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScoreAllMatchesFrg.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: MatchDetail?, param2: String?): ScoreAllMatchesFrg {
            val fragment = ScoreAllMatchesFrg()
            val args = Bundle()
            args.putSerializable(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.setArguments(args)
            return fragment
        }
    }
}