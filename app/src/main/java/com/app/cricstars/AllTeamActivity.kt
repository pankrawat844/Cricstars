package com.app.cricstars

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.cricstars.adapter.RecyclerTeamAdapter
import com.app.cricstars.fragment.AddTeamFragment
import com.app.cricstars.fragment.MyTeamFragment
import com.app.cricstars.fragment.SearchTeamFragment
import com.app.cricstars.model.Team
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.app.cricstars.utils.UserData
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_all_team.*
import kotlinx.android.synthetic.main.activity_search_team.*
import kotlinx.android.synthetic.main.activity_search_team.loader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllTeamActivity : AppCompatActivity() {
     val TAB_TITLES = arrayOf(
        "My Team",
        "Search",
        "Add a team"
    )
    var viewPager: ViewPager?=null
    private var list: List<Team.Data>?=null
      var teamData: Team?=null
    private var adapter: RecyclerTeamAdapter?=null
    var tempArr:ArrayList<Team.Data>?= arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_team)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = if (intent.getIntExtra("team",0)==0)  "Select team A" else "Select team B"
         viewPager = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)


        getData()
    }


    private fun getData() {
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@AllTeamActivity)?.create(
                RetrofitMethods::class.java
            )

            retrofitMethod?.getTeams()?.enqueue(
                object : Callback<Team?> {
                    override fun onResponse(call: Call<Team?>, response: Response<Team?>) {
                        loader.visibility = View.GONE
                        if (response.isSuccessful) {
                            var userdata=UserData(this@AllTeamActivity);
                            teamData=response.body()!!
                            teamData!!.data.forEach {
                                if(it.t10_team_captain_email_id==userdata.email)
                                    tempArr!!.add(it)
                            }
                            val sectionsPagerAdapter = SectionsPagerAdapter(this@AllTeamActivity, supportFragmentManager)
                            viewPager?.adapter = sectionsPagerAdapter

                            tabs.setupWithViewPager(viewPager)


                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getString("msg")
                            Toast.makeText(this@AllTeamActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Team?>, t: Throwable) {
                        loader.visibility = View.GONE
                        Toast.makeText(this@AllTeamActivity, t.message, Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }
    }




    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
        FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1)
            when(position)
            {
                0->  return MyTeamFragment.newInstance(tempArr!!,intent.getIntExtra("team",0))
                1-> return SearchTeamFragment.newInstance(teamData?.data!!,intent.getIntExtra("team",0))
                2->return AddTeamFragment()
                else->
                    return return MyTeamFragment.newInstance(tempArr!!,intent.getIntExtra("team",0))


            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return TAB_TITLES.get(position)
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 3
        }
    }
}