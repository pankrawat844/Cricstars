package com.app.cricstars.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.app.cricstars.R
import com.app.cricstars.adapter.RecyclerMVPAdapter
import com.google.android.material.tabs.TabLayout

class LeaderboardTabsFrg() : Fragment(), View.OnClickListener,
    MVPMatchFrg.OnFragmentInteractionListener {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private var mListener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mParam1 = getArguments()?.getString(ARG_PARAM1)
            mParam2 = getArguments()?.getString(ARG_PARAM2)
        }
    }

    private val mSectionsPagerAdapter: SectionsPagerAdapter? = null
    var tabs: TabLayout? = null

    /**
     * The [ViewPager] that will host the section contents.
     */
    private val mViewPager: ViewPager? = null
    var recycler: RecyclerView? = null
    var view1: View? = null
    var tvBat: TextView? = null
    var tvBowl: TextView? = null
    var tvField: TextView? = null
    var tvMVP: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fr_leaderboard_tabs, container, false)
        //        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
//        tabs = view.findViewById(R.id.tabs);
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) view. findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
////        mViewPager.setNestedScrollingEnabled(false);
//        tabs.setupWithViewPager(mViewPager);
        tvBat = view1!!.findViewById(R.id.tv_bat)
        tvBowl = view1!!.findViewById(R.id.tv_bowl)
        tvField = view1!!.findViewById(R.id.tv_field)
        tvMVP = view1!!.findViewById(R.id.tv_mvp)
        recycler = view1!!.findViewById(R.id.recycler_leaderboard)
        recycler?.setNestedScrollingEnabled(false)
        recycler?.setLayoutManager(LinearLayoutManager(getActivity()))
        recycler?.setAdapter(RecyclerMVPAdapter(getActivity()!!))
        tvBowl?.setOnClickListener(this)
        tvBat?.setOnClickListener(this)
        tvField?.setOnClickListener(this)
        tvMVP?.setOnClickListener(this)
        return view1
    }

    var tvs = intArrayOf(R.id.tv_bat, R.id.tv_bowl, R.id.tv_field, R.id.tv_mvp)
    fun changeColor(tv: Int) {
        for (i in tvs.indices) {
            if (tv == tvs[i]) {
                val tvFinal = view1!!.findViewById<TextView>(tvs[i])
                val cd = tvFinal.background as ColorDrawable
                val colorCode = cd.color
                if (colorCode == Color.WHITE) {
                    tvFinal.setBackgroundColor(Color.BLACK)
                    tvFinal.setTextColor(Color.WHITE)
                } else {
                    tvFinal.setBackgroundColor(Color.WHITE)
                    tvFinal.setTextColor(Color.BLACK)
                }
            } else {
                val tvFinal = view1!!.findViewById<TextView>(tvs[i])
                tvFinal.setBackgroundColor(Color.WHITE)
                tvFinal.setTextColor(Color.BLACK)
            }
        }
    }

    override fun onFragmentInteraction(uri: Uri?) {}
    override fun onClick(tv: View) {
        when (tv.id) {
            R.id.tv_bat -> changeColor(tv.id)
            R.id.tv_bowl -> changeColor(tv.id)
            R.id.tv_field -> changeColor(tv.id)
            R.id.tv_mvp -> changeColor(tv.id)
            else -> changeColor(tvBat!!.id)
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position);
            return MVPMatchFrg()
        }

        @Nullable
        override fun getPageTitle(position: Int): CharSequence {
//            return super.getPageTitle(position);
            when (position) {
                0 -> return "Bat"
                1 -> return "Bowl"
                2 -> return "Field"
                3 -> return "MVP"
                else -> return "na"
            }
        }

        override fun getCount(): Int {
            return 4
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
            if (context is OnFragmentInteractionListener) {
                mListener = context
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
            private val ARG_PARAM1 = "param1"
            private val ARG_PARAM2 = "param2"

            /**
             * Use this factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param param1 Parameter 1.
             * @param param2 Parameter 2.
             * @return A new instance of fragment LeaderboardTabsFrg.
             */
            // TODO: Rename and change types and number of parameters
            fun newInstance(param1: String?, param2: String?): LeaderboardTabsFrg {
                val fragment = LeaderboardTabsFrg()
                val args = Bundle()
                args.putString(ARG_PARAM1, param1)
                args.putString(ARG_PARAM2, param2)
                fragment.setArguments(args)
                return fragment
            }
        }

}
