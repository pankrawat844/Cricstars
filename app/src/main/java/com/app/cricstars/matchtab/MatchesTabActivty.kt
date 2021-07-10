package com.app.cricstars.matchtab

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.cricstars.R
import com.app.cricstars.fragment.ScoreAllMatchesFrg
import com.app.cricstars.model.MatchDetail
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_matches_tab_activty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MatchesTabActivty : AppCompatActivity(), ScoreAllMatchesFrg.OnFragmentInteractionListener {
    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    var tabs: TabLayout? = null
    var data:MatchDetail?=null
    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matches_tab_activty)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        tabs = findViewById(R.id.tabs)
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager?

        tabs?.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
             mViewPager?.currentItem=tab?.position!!
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        getData()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_matches_tab_activty, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == android.R.id.home) {
            onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }

   override fun onFragmentInteraction(uri: Uri?) {}

    inner class SectionsPagerAdapter(fm: FragmentManager, var data1: MatchDetail?) :
        FragmentPagerAdapter(fm) {
     override   fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position);
         if(position==0) {
             var newdata:ArrayList<MatchDetail.Data>?= ArrayList()
             for(value in data1?.data!!) {
                 if (value.match_details!=null) {
                     Log.e("TAG", "getItem: $value.match_details?.t10_match_date" )
                     val dateStr = value?.match_details?.t10_match_date //2020-02-07
                     Log.d("TAG", "getItem() returned: $dateStr")
                     val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                     val matchDate = dateFormat.parse(dateStr)
                     val currentDateStr = dateFormat.format(Calendar.getInstance().time)
                     val currentDate = dateFormat.parse(currentDateStr)

                     if (matchDate.equals(currentDate)) {
                         newdata?.add(value)
                     }
                 }
             }
             val matchDetail=MatchDetail(newdata,true)

             return ScoreAllMatchesFrg.newInstance(matchDetail, "live")
         }
         else if(position==1) {
             var newdata:ArrayList<MatchDetail.Data>?= ArrayList()
             for((index,value) in data1?.data?.withIndex()!!) {
                 if (value.match_details!=null) {

                     val dateStr = value?.match_details?.t10_match_date //2020-02-07
                     val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                     val matchDate = dateFormat.parse(dateStr)
                     val currentDateStr = dateFormat.format(Calendar.getInstance().time)
                     val currentDate = dateFormat.parse(currentDateStr)

                     if (matchDate.after(currentDate)) {
                         newdata?.add(value)
                     }
                 }
             }
             val matchDetail=MatchDetail(newdata,true)

             return ScoreAllMatchesFrg.newInstance(matchDetail, "upcoming")
         }
         else if(position==2) {
             var newdata:ArrayList<MatchDetail.Data>?= ArrayList()
             for(value in data1?.data!!) {
                 if(value.match_details!=null){
                 if (value.match_details.t10_organizer_id==getSharedPreferences("app", MODE_PRIVATE).getString("id","")) {


                     newdata!!.add(value)
                 }
                 }
             }
             val matchDetail=MatchDetail(newdata,true)

             return ScoreAllMatchesFrg.newInstance(matchDetail, "my matches")
         }
         else {
             var newdata:ArrayList<MatchDetail.Data>?= ArrayList()
             for(value in data1?.data!!) {
                 if (value.match_details!=null) {

                     val dateStr = value?.match_details?.t10_match_date //2020-02-07
                     val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                     val matchDate = dateFormat.parse(dateStr)
                     Log.e("TAG", "getItem: $dateStr", )
                     Log.e("TAG", "getItem: $matchDate", )
                     val currentDateStr = dateFormat.format(Calendar.getInstance().time)
                     val currentDate = dateFormat.parse(currentDateStr)

                     if (matchDate.before(currentDate)) {
                         newdata!!.add(value)
                     }
                 }
             }
             val matchDetail=MatchDetail(newdata,true)

             return ScoreAllMatchesFrg.newInstance(matchDetail, "previous")
         }
        }

        @Nullable
       override fun getPageTitle(position: Int): CharSequence {
//            return super.getPageTitle(position);
            return when (position) {
                2 -> "My Matches"
                0 -> "Live"
                1 -> "Upcoming"
                3 -> "Previous"
                else -> "na"
            }
        }

        override fun getCount(): Int {
            return 4
        }

    }

    private fun getData() {
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@MatchesTabActivty)?.create(
                RetrofitMethods::class.java
            )

           retrofitMethod?.getmatches()?.enqueue(
                object : Callback<MatchDetail?> {
                    override fun onResponse(call: Call<MatchDetail?>, response: Response<MatchDetail?>) {
                        loader.visibility= View.GONE
                        if (response.isSuccessful) {
                             data = response.body()!!
                            mSectionsPagerAdapter=SectionsPagerAdapter(supportFragmentManager,data)

                            mViewPager?.setAdapter(mSectionsPagerAdapter)

                            tabs?.setupWithViewPager(mViewPager)
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getJSONObject("data").getString("msg")
                            Toast.makeText(this@MatchesTabActivty, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<MatchDetail?>, t: Throwable) {
                        loader.visibility= View.GONE
                        Toast.makeText(this@MatchesTabActivty, t.message, Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }
}