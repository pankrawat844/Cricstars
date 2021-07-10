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
import com.app.cricstars.R
import com.app.cricstars.adapter.RecyclerFullCommentryAdapter
import com.app.cricstars.model.Commentary
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.tuyenmonkey.mkloader.MKLoader
import kotlinx.android.synthetic.main.fr_commentry_match.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentryMatchFrg : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mParam1 = arguments?.getString(ARG_PARAM1)
            mParam2 = arguments?.getString(ARG_PARAM2)
        }
    }

    var recycler: RecyclerView? = null
    var loader: MKLoader? = null
   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fr_commentry_match, container, false)
        recycler = view.findViewById(R.id.recycler_commentry)
       loader=view.findViewById(R.id.loader)
        getCommentary()
        return view
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
         * @return A new instance of fragment CommentryMatchFrg.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): CommentryMatchFrg {
            val fragment = CommentryMatchFrg()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.setArguments(args)
            return fragment
        }
    }

    private fun getCommentary() {
        loader!!.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(activity!!)?.create(
                RetrofitMethods::class.java
            )

            retrofitMethod?.getCommentary(mParam1!!)?.enqueue(
                object : Callback<Commentary?> {
                    override fun onResponse(call: Call<Commentary?>, response: Response<Commentary?>) {
                        loader!!.visibility = View.INVISIBLE
                        if (response.isSuccessful) {
                            response.body()!!
                            recycler?.layoutManager = LinearLayoutManager(activity)
                            if(response.body()!!.data.commentary!=null) {
                                val reversedArr = response.body()!!.data!!.commentary!!.asReversed()
                                response.body()!!.data!!.commentary = reversedArr
                                recycler?.adapter = RecyclerFullCommentryAdapter(
                                    activity!!,
                                    response.body()!!.data!!
                                )
                            }
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getJSONObject("data").getString("msg")
                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Commentary?>, t: Throwable) {
                        loader!!.visibility = View.INVISIBLE
                        Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

}
