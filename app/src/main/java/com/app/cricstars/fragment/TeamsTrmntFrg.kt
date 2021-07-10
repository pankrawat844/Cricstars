package com.app.cricstars.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.AddTeamActivity
import com.app.cricstars.R
import com.app.cricstars.adapter.RecyclerSquadAdapter
import com.app.cricstars.model.Team
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.app.cricstars.utils.MyUtils.PAGERFORTEAMS
import com.app.cricstars.utils.UserData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_recent_matches.loader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamsTrmntFrg : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mParam1 = getArguments()?.getString(ARG_PARAM1)
            mParam2 = getArguments()?.getString(ARG_PARAM2)
        }
    }

    var recycler: RecyclerView? = null
   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fr_teams_trmnt, container, false)
        recycler = view.findViewById(R.id.recycler_teams)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(activity,AddTeamActivity::class.java))
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri?) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

   override fun onAttach(context: Context) {
        super.onAttach(context)
//        mListener = if (context is OnFragmentInteractionListener) {
//            context
//        } else {
//            throw RuntimeException(
//                context.toString()
//                        + " must implement OnFragmentInteractionListener"
//            )
//        }
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
         * @return A new instance of fragment TeamsTrmntFrg.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): TeamsTrmntFrg {
            val fragment = TeamsTrmntFrg()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        geteams()
    }
    private fun geteams() {
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(activity!!)?.create(
                RetrofitMethods::class.java
            )
            var userdat= UserData(activity!!)


            retrofitMethod?.getUserTeam(userdat.email!!)?.enqueue(
                object : Callback<Team?> {
                    override fun onResponse(call: Call<Team?>, response: Response<Team?>) {
                        loader.visibility = View.GONE
                        if (response.isSuccessful) {
                            response.body()!!
                            recycler?.layoutManager = GridLayoutManager(activity,2)
                            recycler?.adapter = RecyclerSquadAdapter(activity!!, PAGERFORTEAMS,response.body()!!.data,null)

                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getJSONObject("data").getString("msg")
                            Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Team?>, t: Throwable) {
                        loader.visibility = View.GONE
                        Toast.makeText(activity!!, t.message, Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }
}
