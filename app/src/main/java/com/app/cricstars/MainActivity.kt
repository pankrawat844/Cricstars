package com.app.cricstars

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.style.RelativeSizeSpan
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.cricstars.fragment.*
import com.app.cricstars.utils.UserData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,TeamsTrmntFrg.OnFragmentInteractionListener {
    var recyclerMenu: RecyclerView? = null
    var recyclerScore:RecyclerView? = null
    var userData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById<androidx.appcompat.widget.Toolbar>(
            R.id.toolbar
        )
        setSupportActionBar(toolbar)
//        val fab: FloatingActionButton = findViewById<FloatingActionButton>(R.id.fab)
//        fab.setOnClickListener(View.OnClickListener {
////            startActivity(
////                Intent(
////                    this@MainActivity,
////                    MatchesTabActivty::class.java
////                )
////            )
//        })
        userData = UserData(this)
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
//
//        drawer.close()
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
//        drawer.closeDrawer(Gravity.START)
//        drawer.closeDrawer(GravityCompat.START)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        try {
            val v: View = navigationView.getHeaderView(0)
            val pm = this.packageManager
            val pInfo = pm.getPackageInfo(this.packageName, 0)
            (v.findViewById<View>(R.id.tv_nav_username) as TextView).setText(userData?.name)
            (v.findViewById<View>(R.id.tv_nav_mobile) as TextView).setText(userData?.mobile)
        } catch (ne: NullPointerException) {
            ne.printStackTrace()
        } catch (e1: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        supportFragmentManager.beginTransaction().replace(
            R.id.main_content, RecentMatchesFragment.newInstance(
                "Recent Matches",
                ""
            )
        ).commit()

//        recyclerMenu = findViewById<RecyclerView>(R.id.recycler_home_menu)
//        recyclerMenu?.setLayoutManager(
//            LinearLayoutManager(
//                this,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//        )
//        recyclerMenu?.setNestedScrollingEnabled(false)
//        recyclerMenu?.setAdapter(RecyclerHomeMenuAdapter(this))
//        recyclerScore = findViewById(R.id.recycler_home_score)
//        recyclerScore?.setLayoutManager(
//            LinearLayoutManager(
//                this,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//        )
//getData()
    }

    fun increaseFontSizeForPath(spannable: Spannable, path: String, increaseTime: Float) {
        val startIndexOfPath = spannable.toString().indexOf(path)
        spannable.setSpan(
            RelativeSizeSpan(increaseTime), startIndexOfPath,
            startIndexOfPath + path.length, 0
        )
    }


    private val TIME_DELAY = 2000
    private var back_pressed: Long = 0

    override fun onBackPressed() {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed()
            } else {
                Toast.makeText(
                    baseContext, "Press once again to exit!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            back_pressed = System.currentTimeMillis()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            sharingIntent.putExtra(
                Intent.EXTRA_TEXT, """Check out this awesome app 
             https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"""
            )
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
            true
        } else super.onOptionsItemSelected(item)
    }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_matches) {
            // Handle the camera action
            supportFragmentManager.beginTransaction().replace(
                R.id.main_content, RecentMatchesFragment.newInstance(
                    "Recent Matches",
                    ""
                )
            ).commit()

        } else if (id == R.id.nav_add_tournament) {
            startActivity(Intent(this@MainActivity, AddTournamentActivity::class.java))
        }  else if (id == R.id.nav_start_match) {
            startActivity(Intent(this@MainActivity, SelectTeamActivity::class.java))
        } else if (id == R.id.nav_news) {
            val fragment= WebviewFragment()
            val bundle=Bundle()
            bundle.putString("url","http://sport.t-10.in/")
            fragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.main_content,fragment).commit()

        }
        else if (id == R.id.nav_follow) {
            val fragment= WebviewFragment()
            val bundle=Bundle()
            bundle.putString("url","https://t-10.in/cosco-premier-league/")
            fragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.main_content,fragment).commit()

        }  else if (id == R.id.nav_contact) {
            val fragment= WebviewFragment()
            val bundle=Bundle()
            bundle.putString("url","https://t-10.in/contactUs.html")
            fragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.main_content,fragment).commit()

        }
        else if (id == R.id.nav_team) {
            supportFragmentManager.beginTransaction().replace(
                R.id.main_content, TeamsTrmntFrg.newInstance(
                    "",
                    ""
                )
            ).commit()

        } else if (id == R.id.nav_profile) {
          //  startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            supportFragmentManager.beginTransaction().replace(R.id.main_content, ProfileFragment()).commit()
        } else if (id == R.id.nav_mymatches) {
            supportFragmentManager.beginTransaction().replace(
                R.id.main_content, UserMatchesFragment.newInstance(
                    "My Matches",
                    ""
                )
            ).commit()

            //    startActivity(Intent(this@MainActivity, SelectTeamActivity::class.java))
        }
        else if(id==R.id.nav_rate){
            val uri = Uri.parse("market://details?id=$packageName")
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show()
            }
        }
        else if(id==R.id.nav_follow){
            
        }
        else if(id==R.id.nav_share){
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            sharingIntent.putExtra(
                Intent.EXTRA_TEXT, """Check out this awesome app 
             https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"""
            )
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }
        else if (id == R.id.nav_manage3) {
            userData?.setLogout()
            startActivity(
                Intent(this@MainActivity, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            finish()
        }
//        else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//            val uri =
//                Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID) //("market://search?q=pname:" + "com.crazy.voteridapp");
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            try {
//                startActivity(intent)
//            } catch (anfe: ActivityNotFoundException) {
//            }
//        }
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

//    private fun getData() {
//        loader.visibility= View.VISIBLE
//        CoroutineScope(Dispatchers.Main).launch{
//            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@MainActivity)?.create(
//                RetrofitMethods::class.java
//            )
//
//            retrofitMethod?.recentmatches()?.enqueue(
//                object : Callback<MatchDetail?> {
//                    override fun onResponse(call: Call<MatchDetail?>, response: Response<MatchDetail?>) {
//                        loader.visibility= View.GONE
//                        if (response.isSuccessful) {
//                             response.body()!!
//                            recyclerScore?.setNestedScrollingEnabled(false)
//                            recyclerScore?.setAdapter(RecyclerMatchesAdapter(this@MainActivity, LIGHTSCORECARD,response.body(),""))
//                            PagerSnapHelper().attachToRecyclerView(recyclerScore)
//                        } else {
//                            val msg =
//                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
//                                    .getJSONObject("data").getString("msg")
//                            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<MatchDetail?>, t: Throwable) {
//                        loader.visibility= View.GONE
//                        Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
//
//                    }
//
//                })
//        }
//    }

    override fun onFragmentInteraction(uri: Uri?) {

    }
}