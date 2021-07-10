package com.app.cricstars

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.cricstars.fragment.TournamentFragment
import com.app.cricstars.model.MatchDetail
import com.app.cricstars.model.Tournament
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_matches_tab_activty.*
import kotlinx.android.synthetic.main.activity_tournament_tab.*
import kotlinx.android.synthetic.main.activity_tournament_tab.loader
import kotlinx.android.synthetic.main.activity_tournament_tab.toolbar
import kotlinx.android.synthetic.main.fr_matches_tab_activty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TournamentTabActivity : AppCompatActivity() {
    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null
    var tabs: TabLayout? = null
    var data:Tournament?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_tab)
//        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        val fab: FloatingActionButton = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener(View.OnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        })
        getData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }
    private fun getData() {
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@TournamentTabActivity)?.create(
                RetrofitMethods::class.java
            )

            retrofitMethod?.getTournament(getSharedPreferences("app", MODE_PRIVATE).getString("email","")!!)?.enqueue(
                object : Callback<Tournament?> {
                    override fun onResponse(call: Call<Tournament?>, response: Response<Tournament?>) {
                        loader.visibility= View.GONE
                        if (response.isSuccessful) {
                            data = response.body()!!
                            mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

                            // Set up the ViewPager with the sections adapter.
                            mViewPager = findViewById(R.id.container) as ViewPager?
                            tabs = findViewById(R.id.tabs)
                            mViewPager?.setAdapter(mSectionsPagerAdapter)
                            tabs?.setupWithViewPager(mViewPager)
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getJSONObject("data").getString("msg")
                            Toast.makeText(this@TournamentTabActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Tournament?>, t: Throwable) {
                        loader.visibility= View.GONE
                        Toast.makeText(this@TournamentTabActivity, t.message, Toast.LENGTH_SHORT).show()

                    }

                })
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
           return when(position) {
            0-> {

                 TournamentFragment.newInstance(position, data!!)
            }
            1->    TournamentFragment.newInstance(position, data!!)
               else ->  TournamentFragment.newInstance(position,data!!)
           }
        }

        @Nullable
        override fun getPageTitle(position: Int): CharSequence {
//            return super.getPageTitle(position);
            return when (position) {
                0 -> "My Tournament"
                1 -> "Ongoing"
                2 -> "Upcoming"
//                3 -> "Following"
                else -> "na"
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}