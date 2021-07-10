package com.app.cricstars

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.cricstars.fragment.*
import com.app.cricstars.model.MatchDetail
import com.app.cricstars.model.MatchScoreDetail
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_manual_scoring.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MatchDetailsActivity : AppCompatActivity(), MVPMatchFrg.OnFragmentInteractionListener, GalleryMatchFrg.OnFragmentInteractionListener,ScorecardMatchFrg.OnFragmentInteractionListener, CommentryMatchFrg.OnFragmentInteractionListener, SummaryMatchFrg.OnFragmentInteractionListener, InfoMatchFrg.OnFragmentInteractionListener {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null
    var tabs: TabLayout? = null
    var matchScoreDetail:MatchScoreDetail?=null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matche_details)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        getScoreCard()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }

   override fun onFragmentInteraction(uri: Uri?) {

   }

    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
       override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position);
            return when (position) {
                0 -> ScorecardMatchFrg.newInstance(matchScoreDetail)
                1 -> SummaryMatchFrg.newInstance(matchScoreDetail,"")
                2 -> CommentryMatchFrg.newInstance(intent.getStringExtra("match_id"),"")
                3 -> InfoMatchFrg()
                4 -> MVPMatchFrg()
                5 -> GalleryMatchFrg()
                else -> InfoMatchFrg()
            }
        }

        @Nullable
       override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "SCORECARD"
                1 -> "SUMMARY"
                2 -> "COMMENTARY"
                3 -> "INFO"
                4 -> "MVP"
                5 -> "GALLERY"
                else -> "NA"
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }

    private fun getScoreCard( ) {
        loader.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@MatchDetailsActivity)?.create(
                RetrofitMethods::class.java
            )
            retrofitMethod?.getScore(
                intent.getStringExtra("match_id")
            )?.enqueue(
                object : Callback<MatchScoreDetail?> {
                    override fun onResponse(
                        call: Call<MatchScoreDetail?>,
                        response: Response<MatchScoreDetail?>
                    ) {
                        loader.visibility = View.GONE
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data?.success!!) {
                                matchScoreDetail=data
                                mSectionsPagerAdapter = SectionsPagerAdapter(getSupportFragmentManager())
                                tabs = findViewById(R.id.tabs)
                                // Set up the ViewPager with the sections adapter.
                                mViewPager = findViewById(R.id.container) as ViewPager?
                                mViewPager?.setAdapter(mSectionsPagerAdapter)
                                tabs?.setupWithViewPager(mViewPager)
                              //  Toast.makeText(this@MatchDetailsActivity, "${data.data.toString()}", Toast.LENGTH_SHORT).show()

                            } else
                                Toast.makeText(
                                    this@MatchDetailsActivity,
                                    "No Scorecard found.",
                                    Toast.LENGTH_SHORT
                                ).show()
                        } else {
                            Toast.makeText(
                                this@MatchDetailsActivity,
                                "No Scorecard found.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<MatchScoreDetail?>, t: Throwable) {
                        loader.visibility = View.GONE
                        Toast.makeText(this@MatchDetailsActivity, t.message, Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }
    }
}
