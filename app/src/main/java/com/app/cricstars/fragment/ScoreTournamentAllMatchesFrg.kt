package com.app.cricstars.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.app.cricstars.R
import com.app.cricstars.adapter.RecyclerMatchesAdapter
import com.app.cricstars.adapter.RecyclerTournamentMatchesAdapter
import com.app.cricstars.model.MatchDetail
import com.app.cricstars.model.Tournament
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.app.cricstars.utils.MyUtils.LIGHTSCORECARD
import com.tuyenmonkey.mkloader.MKLoader
import kotlinx.android.synthetic.main.fr_score_all_matches.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [com.app.t10_scoring.fragments.ScoreAllMatchesFrg.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [com.app.t10_scoring.fragments.ScoreAllMatchesFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoreTournamentAllMatchesFrg : Fragment() {
    private val ARG_SECTION_NUMBER: String? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mParam1 = getArguments()?.getString(ARG_PARAM1)
        }
    }

    var recyclerMatches: RecyclerView? = null
    var loader: MKLoader? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fr_score_all_matches, container, false)
        recyclerMatches = rootView.findViewById(R.id.recycler_matches)
        loader = rootView.findViewById(R.id.loader)

        getData()
        return rootView
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri?) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    fun newInstance(sectionNumber: MatchDetail): ScoreTournamentAllMatchesFrg {
        val fragment = ScoreTournamentAllMatchesFrg()
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
        private const val ARG_PARAM1 = "tournament_id"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScoreAllMatchesFrg.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(tournament_id: String): ScoreTournamentAllMatchesFrg {
            val fragment = ScoreTournamentAllMatchesFrg()
            val args = Bundle()
            args.putString(ARG_PARAM1, tournament_id)
            fragment.setArguments(args)
            return fragment
        }
    }


    private fun getData() {
        loader?.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(activity!!)?.create(
                RetrofitMethods::class.java
            )

            retrofitMethod?.getTournamentMatches(mParam1!!)?.enqueue(
                object : Callback<MatchDetail?> {
                    override fun onResponse(call: Call<MatchDetail?>, response: Response<MatchDetail?>) {
                        loader?.visibility= View.GONE
                        if (response.isSuccessful) {
                            recyclerMatches?.setLayoutManager(LinearLayoutManager(getActivity()))
                            recyclerMatches?.setAdapter(RecyclerTournamentMatchesAdapter(activity!!, LIGHTSCORECARD,response.body()))

                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getJSONObject("data").getString("msg")
                            Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<MatchDetail?>, t: Throwable) {
                        loader?.visibility= View.GONE
                        Toast.makeText(activity!!, t.message, Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

}