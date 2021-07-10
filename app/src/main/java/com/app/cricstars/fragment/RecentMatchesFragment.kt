package com.app.cricstars.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.R
import com.app.cricstars.adapter.RecyclerHomeMenuAdapter
import com.app.cricstars.adapter.RecyclerMatchesAdapter
import com.app.cricstars.model.MatchDetail
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.app.cricstars.utils.MyUtils
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_recent_matches.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecentMatchesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

var recyclerScore:RecyclerView? = null

class RecentMatchesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_matches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.pagetite).setText(param1)
//        recyclerMenu = view.findViewById<RecyclerView>(R.id.recycler_home_menu)
//        recyclerMenu?.setLayoutManager(
//            LinearLayoutManager(
//                this,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//        )
        recyclerScore = view.findViewById(R.id.recycler_home_score)
        recyclerScore?.setLayoutManager(
            LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        getData()
    }


    private fun getData() {
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(activity!!)?.create(
                RetrofitMethods::class.java
            )

            retrofitMethod?.recentmatches()?.enqueue(
                object : Callback<MatchDetail?> {
                    override fun onResponse(call: Call<MatchDetail?>, response: Response<MatchDetail?>) {
                        loader.visibility= View.GONE
                        if (response.isSuccessful) {
                            response.body()!!
                            recyclerScore?.setNestedScrollingEnabled(false)
                            recyclerScore?.setAdapter(RecyclerMatchesAdapter(activity!!, MyUtils.LIGHTSCORECARD,response.body(),""))
                            PagerSnapHelper().attachToRecyclerView(recyclerScore)
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getJSONObject("data").getString("msg")
                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<MatchDetail?>, t: Throwable) {
                        loader.visibility= View.GONE
                        Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecentMatchesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecentMatchesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}