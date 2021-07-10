package com.app.cricstars

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.app.cricstars.fragment.*
import com.app.cricstars.utils.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_main.*

class BottomNavigationActivity : AppCompatActivity() {

    private var appBarConfiguration: AppBarConfiguration?=null
    var userData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.bottomNav)
        userData = UserData(this)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val bottomnavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        try {
            val v: View = navigationView.getHeaderView(0)
            (v.findViewById<View>(R.id.tv_nav_username) as TextView).setText(userData?.name)
            (v.findViewById<View>(R.id.tv_nav_mobile) as TextView).setText(userData?.mobile)
        } catch (ne: NullPointerException) {
            ne.printStackTrace()
        } catch (e1: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }

         appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,R.id.navigation_dashboard, R.id.navigation_notifications,R.id.nav_matches
            ,R.id.nav_add_tournament, R.id.nav_start_match,R.id.nav_news,R.id.nav_follow,
                R.id.nav_contact,R.id.nav_team,R.id.nav_profile,R.id.nav_manage3,R.id.nav_share ),drawer
        )
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)
        NavigationUI.setupWithNavController(bottomnavigationView, navController)
        fab.setOnClickListener {
            navController.navigate(R.id.nav_matches)

        }
        navigationView.setNavigationItemSelectedListener(
            object : NavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    NavigationUI.navigateUp(navController, appBarConfiguration!!)
                    val id = item.itemId
                    if(id==R.id.navigation_home){
                        val bundle = Bundle()
                        bundle.putString("url", "http://sport.t-10.in/")
                        navController.navigate(R.id.nav_news,bundle)
                    }
                    else if(id==R.id.navigation_dashboard){

                    }
                    else if (id == R.id.nav_matches) {
                         navController.navigate(R.id.nav_matches)
                    }

                    else if (id == R.id.nav_add_tournament) {
                        startActivity(
                            Intent(
                                this@BottomNavigationActivity,
                                AddTournamentActivity::class.java
                            )
                        )
                    } else if (id == R.id.nav_start_match) {
                        startActivity(
                            Intent(
                                this@BottomNavigationActivity,
                                SelectTeamActivity::class.java
                            )
                        )
                    } else if (id == R.id.nav_news) {
                        val bundle = Bundle()
                        bundle.putString("url", "http://sport.t-10.in/")
                        navController.navigate(R.id.nav_news,bundle)


                    } else if (id == R.id.nav_follow) {
                        val fragment = WebviewFragment()
                        val bundle = Bundle()
                        bundle.putString("url", "https://t-10.in/cosco-premier-league/")
                        navController.navigate(R.id.nav_news,bundle)
                        navigationView.menu.getItem(10).setChecked(true)


                    } else if (id == R.id.nav_contact) {
                        val fragment = WebviewFragment()
                        val bundle = Bundle()
                        bundle.putString("url", "https://t-10.in/contactUs.html")
                        navController.navigate(R.id.nav_news,bundle)
                        navigationView.menu.getItem(11).setChecked(true)


                    } else if (id == R.id.nav_team) {
//
                        navController.navigate(R.id.nav_team)

                    } else if (id == R.id.nav_profile) {

                        navController.navigate(R.id.nav_profile)

                    } else if (id == R.id.nav_mymatches) {
//                        supportFragmentManager.beginTransaction().replace(
//                            R.id.main_content, UserMatchesFragment.newInstance(
//                                "My Matches",
//                                ""
//                            )
//                        ).commit()
                            navController.navigate(id)
                        //    startActivity(Intent(this@MainActivity, SelectTeamActivity::class.java))
                    } else if (id == R.id.nav_rate) {
                        val uri = Uri.parse("market://details?id=$packageName")
                        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
                        try {
                            startActivity(myAppLinkToMarket)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(this@BottomNavigationActivity, " unable to find market app", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else if (id == R.id.nav_share) {
                        val sharingIntent = Intent(Intent.ACTION_SEND)
                        sharingIntent.type = "text/plain"
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                        sharingIntent.putExtra(
                            Intent.EXTRA_TEXT, """Check out this awesome app
                         https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"""
                        )
                        startActivity(Intent.createChooser(sharingIntent, "Share via"))
                    } else if (id == R.id.nav_manage3) {
                        userData?.setLogout()
                        startActivity(
                            Intent(this@BottomNavigationActivity, LoginActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                        finish()
                    }


                    val drawer: DrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
                    drawer.closeDrawer(GravityCompat.START)
                    return true
                }
            })

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, appBarConfiguration!!)
                || super.onSupportNavigateUp())
    }
}