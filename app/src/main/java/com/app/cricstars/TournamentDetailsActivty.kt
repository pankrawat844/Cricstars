package com.app.cricstars

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.app.cricstars.adapter.RecyclerMatchesAdapter
import com.app.cricstars.fragment.*
import com.app.cricstars.utils.MyUtils.LIGHTSCORECARD
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

class TournamentDetailsActivty : AppCompatActivity(), TeamsTrmntFrg.OnFragmentInteractionListener,
    PointsTableTFrg.OnFragmentInteractionListener, LeaderboardTabsFrg.OnFragmentInteractionListener,
    GalleryMatchFrg.OnFragmentInteractionListener, ScoreAllMatchesFrg.OnFragmentInteractionListener,
    MVPMatchFrg.OnFragmentInteractionListener,ScoreTournamentAllMatchesFrg.OnFragmentInteractionListener //for leaderboard viewpager
{
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_details_activty)
        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager?
        tabs = findViewById(R.id.tabs)
        mViewPager?.setAdapter(mSectionsPagerAdapter)
        tabs?.setupWithViewPager(mViewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener(View.OnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_tournament_details_activty, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onFragmentInteraction(uri: Uri?) {}

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {
        var recyclerMatches: RecyclerView? = null
      override  fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val rootView: View = inflater.inflate(R.layout.fr_tournament_details, container, false)
            recyclerMatches = rootView.findViewById(R.id.recycler_matches)
            recyclerMatches?.setLayoutManager(LinearLayoutManager(getActivity()))
            recyclerMatches?.setAdapter(RecyclerMatchesAdapter(getActivity()!!, LIGHTSCORECARD,null,""))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private const val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.setArguments(args)
                return fragment
            }
        }
    }

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
            return when (position) {
                0 -> ScoreTournamentAllMatchesFrg.newInstance(intent.getStringExtra("tournament_id")!!)
                1 -> LeaderboardTabsFrg()
                2 -> PointsTableTFrg()
                3 -> TeamsTrmntFrg()
                4 -> GalleryMatchFrg()
                else -> ScoreTournamentAllMatchesFrg.newInstance(intent.getStringExtra("tournament_id")!!)
            }
            //            return PlaceholderFragment.newInstance(position);
        }

        @Nullable
        override fun getPageTitle(position: Int): CharSequence {
//            return super.getPageTitle(position);
            return when (position) {
                0 -> "Matches"
                1 -> "Leaderboard"
                2 -> "Points Table"
                3 -> "Teams"
                4 -> "Gallery"
                else -> "na"
            }
        }

        override fun getCount(): Int {
            return 5
        }
    }
}
