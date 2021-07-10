//package com.app.cricstars
//
//import android.app.Activity
//import android.app.AlertDialog
//import android.app.Dialog
//import android.content.DialogInterface
//import android.content.Intent
//import android.graphics.Color
//import android.os.Bundle
//import android.view.View
//import android.view.Window
//import android.view.WindowManager
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.app.cricstars.adapter.RecyclerRunsAdapter
//import com.app.cricstars.model.*
//import com.app.cricstars.retrofit.ApiBuilder
//import com.app.cricstars.retrofit.RetrofitMethods
//import com.app.cricstars.utils.CustomBottomSheetDialogFragment
//import kotlinx.android.synthetic.main.activity_manual_scoring.*
//import kotlinx.android.synthetic.main.activity_manual_scoring.loader
//import kotlinx.android.synthetic.main.activity_overn_location.*
//import kotlinx.android.synthetic.main.select_player_dialog.*
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import org.json.JSONObject
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.util.*
//
//class ManualScoringActivityOld : AppCompatActivity(), View.OnClickListener {
//    private lateinit var team2: Team.Data
//    private lateinit var team1: Team.Data
//    private lateinit var team2List: ArrayList<Players.Data>
//    private lateinit var team1List: ArrayList<Players.Data>
//    var recyclerRun: RecyclerView? = null
//    private val tvscore: TextView? = null
//
//    private var tv0: TextView? = null
//    private var tv1: TextView? = null
//    private var tv2: TextView? = null
//    private var tvundo: TextView? = null
//    private var tv3: TextView? = null
//    private var tv4: TextView? = null
//    private var tv6: TextView? = null
//    private var tvout: TextView? = null
//    private var tvwd: TextView? = null
//    private var tvnb: TextView? = null
//    private var tvbye: TextView? = null
//    private var tvlb: TextView? = null
//    var tvScoreRun: TextView? =
//        null
//    var tvScoreWkt: TextView? = null
//    var tvBats1: TextView? = null
//    var tvBats2: TextView? = null
//    var tvBatsRun1: TextView? = null
//    var tvBatsRun2: TextView? = null
//    var tvBolwerStats: TextView? = null
//    var batsman_1: Players.Data? = null
//    var batsman_2: Players.Data? = null
//    var bowlerid: Players.Data? = null
//    var inningNo=1
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_manual_scoring)
//        tvlb = findViewById<TextView>(R.id.tv_lb)
//        tvbye = findViewById<TextView>(R.id.tv_bye)
//        tvnb = findViewById<TextView>(R.id.tv_nb)
//        tvwd = findViewById<TextView>(R.id.tv_wd)
//        tvout = findViewById<TextView>(R.id.tv_out)
//        tv6 = findViewById<TextView>(R.id.tv_6)
//        tv4 = findViewById<TextView>(R.id.tv_4)
//        tv3 = findViewById<TextView>(R.id.tv_3)
//        tvundo = findViewById<TextView>(R.id.tv_undo)
//        tv2 = findViewById<TextView>(R.id.tv_2)
//        tv1 = findViewById<TextView>(R.id.tv_1)
//        tv0 = findViewById<TextView>(R.id.tv_0)
//        tvScoreRun = findViewById(R.id.tv_score_run)
//        tvScoreWkt = findViewById(R.id.tv_wicket)
//        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        recyclerRun = findViewById(R.id.recycler_runs)
//        recyclerRun?.layoutManager = LinearLayoutManager(
//            this,
//            LinearLayoutManager.HORIZONTAL,
//            false
//        )
//
//         team1List = intent.getSerializableExtra("team1List") as ArrayList<Players.Data>
//         team2List = intent.getSerializableExtra("team2List") as ArrayList<Players.Data>
//         team1 = intent.getSerializableExtra("team1") as Team.Data
//         team2 = intent.getSerializableExtra("team2") as Team.Data
//         inningNo = intent.getIntExtra("inningNo",1)
//        tvlb!!.setOnClickListener(this)
//        tvbye!!.setOnClickListener(this)
//        tvnb!!.setOnClickListener(this)
//        tvwd!!.setOnClickListener(this)
//        tvout!!.setOnClickListener(this)
//        tv6!!.setOnClickListener(this)
//        tv4!!.setOnClickListener(this)
//        tv3!!.setOnClickListener(this)
//        tvundo!!.setOnClickListener(this)
//        tv2!!.setOnClickListener(this)
//        tv1!!.setOnClickListener(this)
//        tv0!!.setOnClickListener(this)
//        tvBats1 = findViewById(R.id.tv_bats_name1)
//        tvBats2 = findViewById(R.id.tv_bats_name2)
//        tvBatsRun1 = findViewById(R.id.tv_bats_runs1)
//        tvBatsRun2 = findViewById(R.id.tv_bats_runs2)
//        tvBolwerStats = findViewById(R.id.tv_bowler_stats)
//        tvBats1?.setOnClickListener(this)
//        tvBats2?.setOnClickListener(this)
//        tvBats1?.setTextColor(Color.YELLOW)
//        tvBats2?.setTextColor(Color.WHITE)
//
//        toss.text =
//            intent.getStringExtra("toss_won") + " won the toss and elected to " + intent.getStringExtra(
//                "choose"
//            )
//        fullScoreCard.setOnClickListener {
//            val intent = Intent(this@ManualScoringActivity, MatchDetailsActivity::class.java)
//            intent.putExtra("match_id", "")
//            startActivity(intent)
//        }
//        batsman_1 = intent.getSerializableExtra("batsman1") as Players.Data
//        batsman_2 = intent.getSerializableExtra("batsman2") as Players.Data
//        bowlerid = intent.getSerializableExtra("bowler") as Players.Data
//        tvBats1!!.text = batsman_1!!.t10_team_member_name
//        tvBats2!!.text = batsman_2!!.t10_team_member_name
//        tv_bowler_name!!.text = bowlerid!!.t10_team_member_name
//        battingTeam.text = intent.getStringExtra("bat_team_name")
//        endInning.setOnClickListener {
//
//            alertBoxChangeInning("Change Inning", "Do you want to finish this inning?")
//
//        }
//    }
//
//    var arrayRun: ArrayList<RunsModel> = ArrayList<RunsModel>()
//    var ball = 0
//    var wkt = 0
//    var runs = 0
//    var strikeChng = 0
//    var overRun = 0
//    var overWkt = 0
//    var overs = 0
//    var ballFaced1 = 0
//    var ballFaced2 = 0
//    var bat1Run = 0
//    var bat2Run = 0
//    var ballRun = 0
//
//    override fun onClick(view: View) {
//        ballRun = 0
//        val adapter = RecyclerRunsAdapter(this@ManualScoringActivity, arrayRun)
//        recyclerRun?.adapter = adapter
//        adapter.notifyDataSetChanged()
//        if (arrayRun.size != 0) {
//            recyclerRun?.smoothScrollToPosition(arrayRun.size)
//        }
//
//        when (view.id) {
//            R.id.tv_undo -> if (arrayRun.size > 0) {
//                val i = arrayRun.size - 1
//                arrayRun.removeAt(i)
//                adapter.notifyItemRemoved(i)
//                ball -= 1
//            }
//            R.id.tv_0 -> {
//
//                var strikeBatsman = ""
//                var nonstrikeBatsman = ""
//                var bowler = ""
//                if (battingTeam.text.toString() == team1.t10_team_name) {
//                    for (batsmanId in team1List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team2List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//
//                    addRun(
//                        R.id.tv_0,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "1",
//                        "",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                } else {
//                    for (batsmanId in team2List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team1List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//
//                    addRun(
//                        R.id.tv_0,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "1",
//                        "",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                }
//
////                ballRun = 0
////                ball += 1
////                runs += 0
////                overRun += 0
////                arrayRun.add(RunsModel("0", ""))
////                strikeChng = 0
////                setAdapterManually()
//            }
//            R.id.tv_1 -> {
//
//                var strikeBatsman = ""
//                var nonstrikeBatsman = ""
//                var bowler = ""
//                if (battingTeam.text.toString() == team1.t10_team_name) {
//                    for (batsmanId in team1List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team2List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//
//                    addRun(
//                        R.id.tv_1,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "1",
//                        "",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                } else {
//                    for (batsmanId in team2List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team1List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//
//                    addRun(
//                        R.id.tv_1,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "1",
//                        "",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                }
////                ballRun = 1
////                ball += 1
////                runs += 1
////                overRun += 1
////                arrayRun.add(RunsModel("1", ""))
////                strikeChng = 1
//            }
//            R.id.tv_2 -> {
//
//                var strikeBatsman = ""
//                var nonstrikeBatsman = ""
//                var bowler = ""
//                if (battingTeam.text.toString() == team1.t10_team_name) {
//                    for (batsmanId in team1List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team2List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//
//                    addRun(
//                        R.id.tv_2,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "1",
//                        "",
//                        "0",
//                        "0",
//                        "0",
//                        "2",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                } else {
//                    for (batsmanId in team2List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team1List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//
//                    addRun(
//                        R.id.tv_2,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "1",
//                        "",
//                        "0",
//                        "0",
//                        "0",
//                        "2",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                }
////                ballRun = 2
////                ball += 1
////                runs += 2
////                overRun += 2
////                arrayRun.add(RunsModel("2", ""))
////                strikeChng = 0
//            }
//            R.id.tv_3 -> {
//                var strikeBatsman = ""
//                var nonstrikeBatsman = ""
//                var bowler = ""
//                if (battingTeam.text.toString() == team1.t10_team_name) {
//                    for (batsmanId in team1List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team2List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//
//                    addRun(
//                        R.id.tv_3,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "1",
//                        "",
//                        "0",
//                        "0",
//                        "0",
//                        "3",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                } else {
//                    for (batsmanId in team2List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team1List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//
//                    addRun(
//                        R.id.tv_3,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "1",
//                        "",
//                        "0",
//                        "0",
//                        "0",
//                        "3",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                }
////                runs += 3
////                ballRun = 3
////                ball += 1
////                overRun += 3
////                arrayRun.add(RunsModel("3", ""))
////                strikeChng = 1
//            }
//            R.id.tv_4 -> {
//
//                var strikeBatsman = ""
//                var nonstrikeBatsman = ""
//                var bowler = ""
//                if (battingTeam.text.toString() == team1.t10_team_name) {
//                    for (batsmanId in team1List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team2List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//                    addRun(
//                        R.id.tv_4,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "8",
//                        "",
//                        "0",
//                        "1",
//                        "0",
//                        "4",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                } else {
//                    for (batsmanId in team2List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team1List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//
//                    addRun(
//                        R.id.tv_4,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "8",
//                        "",
//                        "0",
//                        "1",
//                        "0",
//                        "4",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                }
////                ballRun = 4
////                runs += 4
////                ball += 1
////                overRun += 4
////                arrayRun.add(RunsModel("4", "FOUR"))
////                strikeChng = 0
//            }
//            R.id.tv_6 -> {
//
//
//                var strikeBatsman = ""
//                var nonstrikeBatsman = ""
//                var bowler = ""
//                if (battingTeam.text.toString() == team1.t10_team_name) {
//                    for (batsmanId in team1List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team2List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//
//                    addRun(
//                        R.id.tv_6,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "9",
//                        "",
//                        "0",
//                        "0",
//                        "1",
//                        "6",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                } else {
//                    for (batsmanId in team2List) {
//                        if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                            if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                        }
//                        if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                            if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                strikeBatsman = batsmanId.t10_team_member_id
//                            else
//                                nonstrikeBatsman = batsmanId.t10_team_member_id
//                        }
//                    }
//
//                    for (bowlwerId in team1List) {
//                        if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                            bowler = bowlwerId.t10_team_member_id
//                        }
//                    }
//
//
//                    addRun(
//                        R.id.tv_6,
//                        team1.t10_team_id,
//                        team2.t10_team_id,
//                        strikeBatsman,
//                        nonstrikeBatsman,
//                        bowler,
//                        "9",
//                        "",
//                        "0",
//                        "0",
//                        "1",
//                        "6",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "0",
//                        "1",
//                        overs.toString(),
//                        ball.toString()
//                    )
//                }
////                ballRun = 6
////                runs += 6
////                ball += 1
////                overRun += 6
////                arrayRun.add(RunsModel("6", "SIX"))
////                strikeChng = 0
//            }
//            R.id.tv_out -> {
//                wkt += 1
//                ball += 1
//                overWkt += 1
//                //                arrayRun.add(new RunsModel("0", "OUT"));
//                showBottomSheet(R.id.tv_out, "W")
//                if (tvBats1?.currentTextColor == Color.YELLOW) {
//                    ballFaced1 = 0
//                    bat1Run = 0
//                } else if (tvBats2?.currentTextColor == Color.YELLOW) {
//                    ballFaced2 = 0
//                    bat2Run = 0
//                }
//
//            }
//            R.id.tv_wd -> showBottomSheet(R.id.tv_wd, "wd")
//            R.id.tv_nb -> //                runs += 1;
////                overRun += 1;
////                arrayRun.add(new RunsModel("0", "nb"));
//                showBottomSheet(R.id.tv_nb, "nb")
//            R.id.tv_bye -> showBottomSheet(R.id.tv_bye, "b")
//            R.id.tv_lb -> showBottomSheet(R.id.tv_lb, "lb")
//            R.id.tv_bats_name1 -> alertBox(
//                "Are your sure?",
//                "Are your sure to change strike manually ?",
//                0
//            )
//            R.id.tv_bats_name2 -> alertBox(
//                "Are your sure?",
//                "Are your sure to change strike manually ?",
//                1
//            )
//        }
//
////        tvScoreWkt!!.setText("" + wkt)
////        tvScoreRun!!.text = "$runs/"
////        tvBolwerStats?.setText("$overs.$ball-0-$overRun-$overWkt-0") //OMRWE
////        if (tvBats1!!.currentTextColor == Color.YELLOW) {
////            ballFaced1 += 1
////            bat1Run += ballRun
////            tvBatsRun1!!.setText("$bat1Run($ballFaced1)")
////        }
////        if (tvBats2!!.currentTextColor == Color.YELLOW) {
////            ballFaced2 += 1
////            bat2Run += ballRun
////            tvBatsRun2!!.setText("$bat2Run($ballFaced2)")
////        }
////        if (strikeChng == 1) {
////            if (tvBats1!!.currentTextColor == Color.YELLOW) {
////                tvBats1!!.setTextColor(Color.WHITE)
////                tvBats2!!.setTextColor(Color.YELLOW)
////            } else {
////                tvBats1!!.setTextColor(Color.YELLOW)
////                tvBats2!!.setTextColor(Color.WHITE)
////            }
////        }
//    }
//
//    fun setAdapterManually() {
//        val adapter = RecyclerRunsAdapter(this@ManualScoringActivity, arrayRun)
//        recyclerRun!!.adapter = adapter
//        adapter.notifyDataSetChanged()
//    }
//
//    fun alertBox(title: String?, msg: String?, i: Int) {
//        val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
//        alertDialog.setTitle(title)
//        alertDialog.setMessage(msg)
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//            DialogInterface.OnClickListener { dialog, which ->
//                if (i == 0) {
//                    tvBats2!!.setTextColor(Color.WHITE)
//                    tvBats1!!.setTextColor(Color.YELLOW)
//                } else {
//                    tvBats1!!.setTextColor(Color.WHITE)
//                    tvBats2!!.setTextColor(Color.YELLOW)
//                }
//            })
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
//            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
//        alertDialog.show()
//    }
//
//    private fun alertBoxChangeInning(title: String?, msg: String?) {
//        val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
//        alertDialog.setTitle(title)
//        alertDialog.setMessage(msg)
//        alertDialog.setCancelable(false)
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//            DialogInterface.OnClickListener { dialog, which ->
//                if(inningNo==1) {
//                    dialog.dismiss()
//                    inningNo=2
//                    val temp = team2List
//                    val temp1 = team2
//                    team2List = team1List
//                    team1List = temp
//                    team2 = team1
//                    team1 = temp1
//                    tvBats2!!.setTextColor(Color.WHITE)
//                    tvBats1!!.setTextColor(Color.YELLOW)
//                    battingTeam.text=team1.t10_team_name
//                    dialogBoxNewInning()
//                }else
//                {
//                    dialog.dismiss()
//                }
//            })
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
//            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
//        alertDialog.show()
//    }
//
//    fun showBottomSheet(viewId: Int?, word: String?) {
//
//        val cs = CustomBottomSheetDialogFragment()
//        cs.show(supportFragmentManager, "Dialog")
//        cs.setRuns(object : CustomBottomSheetDialogFragment.ExtraDialog {
//            override fun extraRun(i: Int) {
//                var bowlToCount = "0"
//                if (word == "b" || word == "lb" || word == "W")
//                    bowlToCount = "1"
//
//                cs.dismiss()
//                if (i % 2 == 0)
//                    strikeChng = 0
//                else
//                    strikeChng = 1
//
//
//                if (word == "W")
//                    dialogBox(word, bowlToCount, i)
//                else {
//
//                    var strikeBatsman = ""
//                    var nonstrikeBatsman = ""
//                    var bowler = ""
//                    if (battingTeam.text.toString() == team1.t10_team_name) {
//                        for (batsmanId in team1List) {
//                            if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                                if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                    strikeBatsman = batsmanId.t10_team_member_id
//                                else
//                                    nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                            }
//                            if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                                if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                    strikeBatsman = batsmanId.t10_team_member_id
//                                else
//                                    nonstrikeBatsman = batsmanId.t10_team_member_id
//                            }
//                        }
//
//                        for (bowlwerId in team2List) {
//                            if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                                bowler = bowlwerId.t10_team_member_id
//                            }
//                        }
//
//
//                        addRunExtra(
//                            viewId!!,
//                            team1.t10_team_id,
//                            team2.t10_team_id,
//                            strikeBatsman,
//                            nonstrikeBatsman,
//                            bowler,
//                            "1",
//                            word!!,
//                            i.toString(),
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            strikeBatsman,
//                            nonstrikeBatsman,
//                            bowlToCount,
//                            overs.toString(),
//                            ball.toString()
//                        )
//                    } else {
//                        for (batsmanId in team2List) {
//                            if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                                if (tvBats1!!.currentTextColor == Color.YELLOW)
//                                    strikeBatsman = batsmanId.t10_team_member_id
//                                else
//                                    nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                            }
//                            if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                                if (tvBats2!!.currentTextColor == Color.YELLOW)
//                                    strikeBatsman = batsmanId.t10_team_member_id
//                                else
//                                    nonstrikeBatsman = batsmanId.t10_team_member_id
//                            }
//                        }
//
//                        for (bowlwerId in team1List) {
//                            if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                                bowler = bowlwerId.t10_team_member_id
//                            }
//                        }
//
//
//                        addRunExtra(
//                            viewId!!,
//                            team1.t10_team_id,
//                            team2.t10_team_id,
//                            strikeBatsman,
//                            nonstrikeBatsman,
//                            bowler,
//                            "1",
//                            word!!,
//                            i.toString(),
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            "0",
//                            bowlToCount,
//                            overs.toString(),
//                            ball.toString()
//                        )
//                    }
//                }
//            }
//        })
//    }
//
//    fun extraRun(run: Int) {}
//
//    fun dialogBox(word: String, bowlToCount: String, i: Int) {
//
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        /////make map clear
//        // dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
//        dialog.setContentView(R.layout.select_player_dialog)////your custom content
//        val select_wicket_type: Spinner = dialog.findViewById(R.id.select_wicket_type)
//        val runOut: Spinner = dialog.findViewById(R.id.runout)
//        val select_wicket_type_helper: Spinner = dialog.findViewById(R.id.select_wicket_type_helper)
//        val new_batsman: Spinner = dialog.findViewById(R.id.new_batsman)
//        val submit: Button = dialog.findViewById(R.id.submit)
//
//        var team2listString = arrayListOf<String>()
//        team2listString.add(0, "Select Wicket Helper")
//        team2List.forEach {
//            team2listString.add(it.t10_team_member_name)
//        }
//        select_wicket_type_helper.adapter = ArrayAdapter(
//            this@ManualScoringActivity,
//            android.R.layout.simple_dropdown_item_1line,
//            team2listString
//        )
//        select_wicket_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                if (select_wicket_type.selectedItem.toString() == "Run Out")
//                    runOut.visibility = View.VISIBLE
//                else
//                    runOut.visibility = View.GONE
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//
//        }
//        var team1listString = arrayListOf<String>()
//        team1listString.add(0, "Select next batsman")
//        team1List.forEach {
//            team1listString.add(it.t10_team_member_name)
//        }
//        new_batsman.adapter = ArrayAdapter(
//            this@ManualScoringActivity,
//            android.R.layout.simple_dropdown_item_1line,
//            team1listString
//        )
//
//        dialog.setCanceledOnTouchOutside(false)
//        dialog.show()
//        val window: Window? = dialog.window
//        window!!.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//        submit.setOnClickListener {
//            if (select_wicket_type.selectedItem.toString() == "Select Wicket Type") {
//                Toast.makeText(
//                    this@ManualScoringActivity,
//                    "Please select wicket type",
//                    Toast.LENGTH_SHORT
//                ).show()
//                return@setOnClickListener
//            }
//            if (select_wicket_type.selectedItem.toString() == "Run Out") {
//                if (runOut.selectedItem.toString() == "Select Run Out") {
//                    Toast.makeText(
//                        this@ManualScoringActivity,
//                        "Please select Run out",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@setOnClickListener
//                }
//
//            }
//            if (select_wicket_type_helper.selectedItem.toString() == "Select Wicket Helper") {
//                Toast.makeText(
//                    this@ManualScoringActivity,
//                    "Please select wicket helper",
//                    Toast.LENGTH_SHORT
//                ).show()
//                return@setOnClickListener
//            }
//            if (new_batsman.selectedItem.toString() == "Select next batsman") {
//                Toast.makeText(
//                    this@ManualScoringActivity,
//                    "Please select next batsman",
//                    Toast.LENGTH_SHORT
//                ).show()
//                return@setOnClickListener
//            }
//
//            var wicket_type_id = 0
//            if (select_wicket_type.selectedItem.toString() == "Catch Out")
//                wicket_type_id = 10
//            else if (select_wicket_type.selectedItem.toString() == "Run Out") {
//                wicket_type_id = if (runout.selectedItem.toString() == "Striker")
//                    11
//                else
//                    21
//
//            } else if (select_wicket_type.selectedItem.toString() == "Stumping")
//                wicket_type_id = 22
//            else if (select_wicket_type.selectedItem.toString() == "LBW")
//                wicket_type_id = 23
//            else if (select_wicket_type.selectedItem.toString() == "Hit Wicket")
//                wicket_type_id = 24
//            else if (select_wicket_type.selectedItem.toString() == "Bowled")
//                wicket_type_id = 25
//            var strikeBatsman = ""
//            var nonstrikeBatsman = ""
//            var bowler = ""
//            var wicketHelperID = ""
//            var nextBatsmanID = ""
//            if (battingTeam.text.toString() == team1.t10_team_name) {
//                for (batsmanId in team1List) {
//                    if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                        if (tvBats1!!.currentTextColor == Color.YELLOW)
//                            strikeBatsman = batsmanId.t10_team_member_id
//                        else
//                            nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                    }
//                    if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                        if (tvBats2!!.currentTextColor == Color.YELLOW)
//                            strikeBatsman = batsmanId.t10_team_member_id
//                        else
//                            nonstrikeBatsman = batsmanId.t10_team_member_id
//                    }
//
//
//                    if (new_batsman.selectedItem.toString() == batsmanId.t10_team_member_name)
//                        nextBatsmanID = batsmanId.t10_team_member_id
//
//                }
//
//                for (bowlwerId in team2List) {
//                    if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                        bowler = bowlwerId.t10_team_member_id
//                    }
//                    if (select_wicket_type_helper.selectedItem.toString() == bowlwerId.t10_team_member_name)
//                        wicketHelperID = bowlwerId.t10_team_member_id
//                }
//
//                addOut(
//                    team1.t10_team_id,
//                    team2.t10_team_id,
//                    strikeBatsman,
//                    nonstrikeBatsman,
//                    bowler,
//                    "1",
//                    word,
//                    i.toString(),
//                    "0",
//                    "0",
//                    "0",
//                    wicket_type_id.toString(),
//                    "1",
//                    wicketHelperID,
//                    nextBatsmanID,
//                    "0",
//                    "0",
//                    "0",
//                    bowlToCount,
//                    overs.toString(),
//                    ball.toString()
//                )
//            } else {
//                for (batsmanId in team2List) {
//                    if (batsmanId.t10_team_member_name == tvBats1!!.text.toString()) {
//                        if (tvBats1!!.currentTextColor == Color.YELLOW)
//                            strikeBatsman = batsmanId.t10_team_member_id
//                        else
//                            nonstrikeBatsman = batsmanId.t10_team_member_id
//
//                    }
//                    if (batsmanId.t10_team_member_name == tvBats2!!.text.toString()) {
//                        if (tvBats2!!.currentTextColor == Color.YELLOW)
//                            strikeBatsman = batsmanId.t10_team_member_id
//                        else
//                            nonstrikeBatsman = batsmanId.t10_team_member_id
//                    }
//                }
//
//                for (bowlwerId in team1List) {
//                    if (bowlwerId.t10_team_member_name == tv_bowler_name!!.text.toString()) {
//                        bowler = bowlwerId.t10_team_member_id
//                    }
//                }
//
//                addOut(
//                    team1.t10_team_id,
//                    team2.t10_team_id,
//                    strikeBatsman,
//                    nonstrikeBatsman,
//                    bowler,
//                    "1",
//                    word,
//                    i.toString(),
//                    "0",
//                    "0",
//                    "0",
//                    wicket_type_id.toString(),
//                    "1",
//                    wicketHelperID,
//                    nextBatsmanID,
//                    "0",
//                    "0",
//                    "0",
//                    bowlToCount,
//                    overs.toString(),
//                    ball.toString()
//                )
//            }
//            dialog.dismiss()
//        }
//
//    }
//    fun dialogBoxNewInning() {
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        /////make map clear
//        // dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
//        dialog.setContentView(R.layout.dialog_new_inning)////your custom content
//        val batsman1: Spinner = dialog.findViewById(R.id.batsman1)
//        val batsman2: Spinner = dialog.findViewById(R.id.batsman2)
//        val bowler: Spinner = dialog.findViewById(R.id.bowler)
//        var batsmanListName = arrayListOf<String>()
//        var batsmanListName2 = arrayListOf<String>()
//        var bowlerList = arrayListOf<String>()
//
//        batsmanListName.add(0,"Select Batsman 1")
//        batsmanListName2.add(0,"Select Batsman 2")
//        team1List.forEach {
//            batsmanListName.add(it.t10_team_member_name)
//            batsmanListName2.add(it.t10_team_member_name)
//        }
//        val  batsmanAdapter=ArrayAdapter(
//            this@ManualScoringActivity,
//            android.R.layout.simple_dropdown_item_1line,
//            batsmanListName
//        )
//        val  batsmanAdapter2=ArrayAdapter(
//            this@ManualScoringActivity,
//            android.R.layout.simple_dropdown_item_1line,
//            batsmanListName2
//        )
//        bowlerList.add(0,"Select Bowler")
//        team2List.forEach{
//            bowlerList.add(it.t10_team_member_name)
//        }
//        val  bowlerAdapter=ArrayAdapter(
//            this@ManualScoringActivity,
//            android.R.layout.simple_dropdown_item_1line,
//            bowlerList
//        )
//        batsman1.adapter=batsmanAdapter
//        batsman2.adapter=batsmanAdapter2
//        bowler.adapter=bowlerAdapter
//        val submit: Button = dialog.findViewById(R.id.submit)
//        dialog.setCanceledOnTouchOutside(false)
//        dialog.show()
//        val window: Window? = dialog.window
//        window!!.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//        submit.setOnClickListener {
//            if (batsman1.selectedItem.toString() == "Select Batsman 1") {
//                Toast.makeText(
//                    this@ManualScoringActivity,
//                    "Please Select Batsman 1",
//                    Toast.LENGTH_SHORT
//                ).show()
//                return@setOnClickListener
//            }
//            if (batsman2.selectedItem.toString() == "Select Batsman 2") {
//                    Toast.makeText(
//                        this@ManualScoringActivity,
//                        "Please Select Batsman 2",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                return@setOnClickListener
//
//            }
//            if (bowler.selectedItem.toString() == "Select Bowler") {
//                Toast.makeText(
//                    this@ManualScoringActivity,
//                    "Please select Bowler",
//                    Toast.LENGTH_SHORT
//                ).show()
//                return@setOnClickListener
//            }
//            ball = 0
//            wkt = 0
//            runs = 0
//            strikeChng = 0
//            overRun = 0
//            overWkt = 0
//            overs = 0
//            ballFaced1 = 0
//            ballFaced2 = 0
//            bat1Run = 0
//            bat2Run = 0
//            ballRun = 0
//            tvScoreWkt!!.text = "0"
//            tvScoreRun!!.text = "0"
//            battingTeam.text=team1.t10_team_name
//            tvBats1?.text=batsman1.selectedItem.toString()
//            tvBats2?.text=batsman2.selectedItem.toString()
//            tvBatsRun1?.text="0(0)"
//            tvBatsRun2?.text="0(0)"
//            tv_bowler_name?.text=bowler.selectedItem.toString()
//            tvBolwerStats?.text = "$overs.$ball-0-$overRun-$overWkt-0" //OMRWE
//             tvBats1!!.setTextColor(Color.YELLOW)
//            tvBats2!!.setTextColor(Color.WHITE)
//            dialog.dismiss()
//
//            }
//        }
//
//
//    fun dialogBoxBowler() {
//
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        /////make map clear
//        // dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
//        dialog.setContentView(R.layout.dialog_select_bowler)////your custom content
//        val select_bowler: Spinner = dialog.findViewById(R.id.select_bowler)
//
//
//        var bowlerListString = arrayListOf<String>()
//        bowlerListString.add(0, "Select next Bowler")
//        team2List.forEach {
//            bowlerListString.add(it.t10_team_member_name)
//        }
//        select_bowler.adapter = ArrayAdapter(
//            this@ManualScoringActivity,
//            android.R.layout.simple_dropdown_item_1line,
//            bowlerListString
//        )
//
//        val submit: Button = dialog.findViewById(R.id.submit)
//        dialog.setCanceledOnTouchOutside(false)
//        dialog.show()
//        val window: Window? = dialog.window
//        window!!.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//        submit.setOnClickListener {
//            if (select_bowler.selectedItem.toString() == "Select next Bowler") {
//                Toast.makeText(
//                    this@ManualScoringActivity,
//                    "Please select next bowler",
//                    Toast.LENGTH_SHORT
//                ).show()
//                return@setOnClickListener
//
//            }
//            tv_bowler_name.text=select_bowler.selectedItem.toString()
//            dialog.dismiss()
//        }
//
//    }
//
//    class OverDialog     // TODO Auto-generated constructor stub
//        (var c: Activity) : Dialog(c) {
//        var d: Dialog? = null
//        var tvClose: TextView? = null
//        override fun onCreate(savedInstanceState: Bundle) {
//            super.onCreate(savedInstanceState)
//            requestWindowFeature(Window.FEATURE_NO_TITLE)
//            setContentView(R.layout.custom_profile_dialog)
//            tvClose = findViewById(R.id.tv_close)
//            tvClose!!.setOnClickListener(View.OnClickListener { dismiss() })
//        }
//    }
//
//    private fun addRun(
//        view: Int,
//        batTeamId: String,
//        bowlTeamId: String,
//        strikeBatsman: String,
//        nonStrikeBatsman: String,
//        bowlerId: String,
//        runType: String,
//        extraType: String,
//        extraRun: String,
//        fourCount: String,
//        sixCount: String,
//        run: String,
//        wicketType: String,
//        wicket: String,
//        wicketHelpId: String,
//        newBatsmanId: String,
//        nextBowlerId: String,
//        striker1: String,
//        strike2: String,
//        bowlToCount: String,
//        over: String,
//        ballNumber: String
//    ) {
//        loader.visibility = View.VISIBLE
//        CoroutineScope(Dispatchers.Main).launch {
//            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@ManualScoringActivity)?.create(
//                RetrofitMethods::class.java
//            )
//            val response = retrofitMethod?.addScore(
//                getSharedPreferences("app", MODE_PRIVATE).getString(
//                    "id",
//                    ""
//                )!!,
//                batTeamId,
//                bowlTeamId,
//                strikeBatsman,
//                nonStrikeBatsman,
//                bowlerId,
//                intent.getStringExtra("match_id"),
//                runType,
//                extraType,
//                extraRun,
//                fourCount,
//                sixCount,
//                run,
//                wicketType,
//                wicket,
//                wicketHelpId,
//                newBatsmanId,
//                nextBowlerId,
//                striker1,
//                strike2,
//                bowlToCount,
//                over,
//                ballNumber
//            )?.enqueue(
//                object : Callback<Citites?> {
//                    override fun onResponse(call: Call<Citites?>, response: Response<Citites?>) {
//                        loader.visibility = View.GONE
//                        if (response.isSuccessful) {
//                            val data = response.body()
//                            if (data?.success!!) {
//                                Toast.makeText(
//                                    this@ManualScoringActivity,
//                                    "Synced Successfully",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                when (view) {
//                                    R.id.tv_0 -> {
//                                        ballRun = 0
//                                        ball += 1
//                                        runs += 0
//                                        overRun += 0
//                                        arrayRun.add(RunsModel("0", ""))
//                                        strikeChng = 0
//                                        setAdapterManually()
//                                    }
//                                    R.id.tv_1 -> {
//                                        ballRun = 1
//                                        ball += 1
//                                        runs += 1
//                                        overRun += 1
//                                        arrayRun.add(RunsModel("1", ""))
//                                        strikeChng = 1
//                                    }
//                                    R.id.tv_2 -> {
//                                        ballRun = 2
//                                        ball += 1
//                                        runs += 2
//                                        overRun += 2
//                                        arrayRun.add(RunsModel("2", ""))
//                                        strikeChng = 0
//                                    }
//                                    R.id.tv_3 -> {
//                                        runs += 3
//                                        ballRun = 3
//                                        ball += 1
//                                        overRun += 3
//                                        arrayRun.add(RunsModel("3", ""))
//                                        strikeChng = 1
//                                    }
//                                    R.id.tv_4 -> {
//                                        ballRun = 4
//                                        runs += 4
//                                        ball += 1
//                                        overRun += 4
//                                        arrayRun.add(RunsModel("4", "FOUR"))
//                                        strikeChng = 0
//                                    }
//                                    R.id.tv_6 -> {
//                                        ballRun = 6
//                                        runs += 6
//                                        ball += 1
//                                        overRun += 6
//                                        arrayRun.add(RunsModel("6", "SIX"))
//                                        strikeChng = 0
//                                    }
//                                }
//
//                                tvScoreWkt!!.text = "" + wkt
//                                tvScoreRun!!.text = "$runs/"
//                                tvBolwerStats?.text = "$overs.$ball-0-$overRun-$overWkt-0" //OMRWE
//                                if (tvBats1!!.currentTextColor == Color.YELLOW) {
//                                    ballFaced1 += 1
//                                    bat1Run += ballRun
//                                    tvBatsRun1!!.text = "$bat1Run($ballFaced1)"
//                                }
//                                if (tvBats2!!.currentTextColor == Color.YELLOW) {
//                                    ballFaced2 += 1
//                                    bat2Run += ballRun
//                                    tvBatsRun2!!.text = "$bat2Run($ballFaced2)"
//                                }
//                                if (strikeChng == 1) {
//                                    if (tvBats1!!.currentTextColor == Color.YELLOW) {
//                                        tvBats1!!.setTextColor(Color.WHITE)
//                                        tvBats2!!.setTextColor(Color.YELLOW)
//                                    } else {
//                                        tvBats1!!.setTextColor(Color.YELLOW)
//                                        tvBats2!!.setTextColor(Color.WHITE)
//                                    }
//                                }
//                                if (ball == 6) {
//                                    Toast.makeText(this@ManualScoringActivity, "OVER", Toast.LENGTH_SHORT).show()
//                                         arrayRun.clear()
//                                    dialogBoxBowler()
//
//                                    overRun = 0
//                                    overWkt = 0
//                                    //            return;
//                                    ball = 0
//                                    overs += 1
//
//                                }
//                            } else
//                                Toast.makeText(
//                                    this@ManualScoringActivity,
//                                    data.msg,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                        } else {
//                            val msg =
//                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
//                                    .getString("msg")
//                            Toast.makeText(this@ManualScoringActivity, msg, Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Citites?>, t: Throwable) {
//                        loader.visibility = View.GONE
//                        Toast.makeText(this@ManualScoringActivity, t.message, Toast.LENGTH_SHORT)
//                            .show()
//
//                    }
//
//                })
//        }
//    }
//
//    private fun addRunExtra(
//        view: Int,
//        batTeamId: String,
//        bowlTeamId: String,
//        strikeBatsman: String,
//        nonStrikeBatsman: String,
//        bowlerId: String,
//        runType: String,
//        extraType: String,
//        extraRun: String,
//        fourCount: String,
//        sixCount: String,
//        run: String,
//        wicketType: String,
//        wicket: String,
//        wicketHelpId: String,
//        newBatsmanId: String,
//        nextBowlerId: String,
//        striker1: String,
//        strike2: String,
//        bowlToCount: String,
//        over: String,
//        ballNumber: String
//    ) {
//        loader.visibility = View.VISIBLE
//        CoroutineScope(Dispatchers.Main).launch {
//            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@ManualScoringActivity)?.create(
//                RetrofitMethods::class.java
//            )
//            val response = retrofitMethod?.addScore(
//                getSharedPreferences("app", MODE_PRIVATE).getString(
//                    "id",
//                    ""
//                )!!,
//                batTeamId,
//                bowlTeamId,
//                strikeBatsman,
//                nonStrikeBatsman,
//                bowlerId,
//                intent.getStringExtra("match_id"),
//                runType,
//                extraType,
//                extraRun,
//                fourCount,
//                sixCount,
//                run,
//                wicketType,
//                wicket,
//                wicketHelpId,
//                newBatsmanId,
//                nextBowlerId,
//                striker1,
//                strike2,
//                bowlToCount,
//                over,
//                ballNumber
//            )?.enqueue(
//                object : Callback<Citites?> {
//                    override fun onResponse(call: Call<Citites?>, response: Response<Citites?>) {
//                        loader.visibility = View.GONE
//                        if (response.isSuccessful) {
//                            val data = response.body()
//                            if (data?.success!!) {
//                                Toast.makeText(
//                                    this@ManualScoringActivity,
//                                    "Synced Successfully",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                when (view) {
//
//                                    R.id.tv_lb, R.id.tv_bye -> {
//                                        ballRun = extraRun.toInt()
//                                        runs += extraRun.toInt()
//                                        overRun += extraRun.toInt()
//                                        arrayRun.add(RunsModel(extraRun, extraType))
//                                        val adapter = RecyclerRunsAdapter(
//                                            this@ManualScoringActivity,
//                                            arrayRun
//                                        )
//                                        recyclerRun!!.adapter = adapter
//                                        adapter.notifyDataSetChanged()
//                                    }
//                                }
//
//                                tvScoreWkt!!.text = "" + wkt
//                                tvScoreRun!!.text = "$runs/"
//                                tvBolwerStats?.text = "$overs.$ball-0-$overRun-$overWkt-0" //OMRWE
//                                if (tvBats1!!.currentTextColor == Color.YELLOW) {
//                                    ballFaced1 += 1
//                                    bat1Run += ballRun
//                                    tvBatsRun1!!.text = "$bat1Run($ballFaced1)"
//                                }
//                                if (tvBats2!!.currentTextColor == Color.YELLOW) {
//                                    ballFaced2 += 1
//                                    bat2Run += ballRun
//                                    tvBatsRun2!!.text = "$bat2Run($ballFaced2)"
//                                }
//                                if (strikeChng == 1) {
//                                    if (tvBats1!!.currentTextColor == Color.YELLOW) {
//                                        tvBats1!!.setTextColor(Color.WHITE)
//                                        tvBats2!!.setTextColor(Color.YELLOW)
//                                    } else {
//                                        tvBats1!!.setTextColor(Color.YELLOW)
//                                        tvBats2!!.setTextColor(Color.WHITE)
//                                    }
//                                }
//                            } else
//                                Toast.makeText(
//                                    this@ManualScoringActivity,
//                                    data.msg,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                        } else {
//                            val msg =
//                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
//                                    .getString("msg")
//                            Toast.makeText(this@ManualScoringActivity, msg, Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Citites?>, t: Throwable) {
//                        loader.visibility = View.GONE
//                        Toast.makeText(this@ManualScoringActivity, t.message, Toast.LENGTH_SHORT)
//                            .show()
//
//                    }
//
//                })
//        }
//    }
//
//    private fun addOut(
//        batTeamId: String,
//        bowlTeamId: String,
//        strikeBatsman: String,
//        nonStrikeBatsman: String,
//        bowlerId: String,
//        runType: String,
//        extraType: String,
//        extraRun: String,
//        fourCount: String,
//        sixCount: String,
//        run: String,
//        wicketType: String,
//        wicket: String,
//        wicketHelpId: String,
//        newBatsmanId: String,
//        nextBowlerId: String,
//        striker1: String,
//        strike2: String,
//        bowlToCount: String,
//        over: String,
//        ballNumber: String
//    ) {
//        loader.visibility = View.VISIBLE
//        CoroutineScope(Dispatchers.Main).launch {
//            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@ManualScoringActivity)?.create(
//                RetrofitMethods::class.java
//            )
//            retrofitMethod?.addScore(
//                getSharedPreferences("app", MODE_PRIVATE).getString(
//                    "id",
//                    ""
//                )!!,
//                batTeamId,
//                bowlTeamId,
//                strikeBatsman,
//                nonStrikeBatsman,
//                bowlerId,
//                intent.getStringExtra("match_id"),
//                runType,
//                extraType,
//                extraRun,
//                fourCount,
//                sixCount,
//                run,
//                wicketType,
//                wicket,
//                wicketHelpId,
//                newBatsmanId,
//                nextBowlerId,
//                striker1,
//                strike2,
//                bowlToCount,
//                over,
//                ballNumber
//            )?.enqueue(
//                object : Callback<Citites?> {
//                    override fun onResponse(call: Call<Citites?>, response: Response<Citites?>) {
//                        loader.visibility = View.GONE
//                        if (response.isSuccessful) {
//                            val data = response.body()
//                            if (data?.success!!) {
//                                Toast.makeText(
//                                    this@ManualScoringActivity,
//                                    "Synced Successfully",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//
//                              for (batsmanId in team1List) {
//                                    if (batsmanId.t10_team_member_id == newBatsmanId) {
//                                        if (tvBats1!!.currentTextColor == Color.YELLOW) {
//                                            tvBats1!!.text = batsmanId.t10_team_member_name
//                                            tvBatsRun1!!.text="0(0)"
//
//                                        }else
//                                        {
//                                            tvBats2!!.text = batsmanId.t10_team_member_name
//                                            tvBatsRun2!!.text="0(0)"
//                                        }
//                                    }
//
//
//                                }
//                                if (strikeChng == 1) {
//
//                                    if (tvBats1!!.currentTextColor == Color.YELLOW) {
//                                        tvBats1!!.setTextColor(Color.WHITE)
//                                        tvBats2!!.setTextColor(Color.YELLOW)
//                                    } else {
//                                        tvBats1!!.setTextColor(Color.YELLOW)
//                                        tvBats2!!.setTextColor(Color.WHITE)
//                                    }
//                                }
//                            } else
//                                Toast.makeText(
//                                    this@ManualScoringActivity,
//                                    data.msg,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                        } else {
//                            val msg =
//                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
//                                    .getString("msg")
//                            Toast.makeText(this@ManualScoringActivity, msg, Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Citites?>, t: Throwable) {
//                        loader.visibility = View.GONE
//                        Toast.makeText(this@ManualScoringActivity, t.message, Toast.LENGTH_SHORT)
//                            .show()
//
//                    }
//
//                })
//        }
//    }
//
//
//    private fun getScoreCard(
//      match_id:String
//    ) {
//        loader.visibility = View.VISIBLE
//        CoroutineScope(Dispatchers.Main).launch {
//            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@ManualScoringActivity)?.create(
//                RetrofitMethods::class.java
//            )
//           retrofitMethod?.getScore(
//                intent.getStringExtra("match_id")
//            )?.enqueue(
//                object : Callback<Citites?> {
//                    override fun onResponse(call: Call<Citites?>, response: Response<Citites?>) {
//                        loader.visibility = View.GONE
//                        if (response.isSuccessful) {
//                            val data = response.body()
//                            if (data?.success!!) {
//                                Toast.makeText(
//                                    this@ManualScoringActivity,
//                                    "Synced Successfully",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//
//
//                                tvScoreWkt!!.text = "" + wkt
//                                tvScoreRun!!.text = "$runs/"
//                                tvBolwerStats?.text = "$overs.$ball-0-$overRun-$overWkt-0" //OMRWE
//                                if (tvBats1!!.currentTextColor == Color.YELLOW) {
//                                    ballFaced1 += 1
//                                    bat1Run += ballRun
//                                    tvBatsRun1!!.text = "$bat1Run($ballFaced1)"
//                                }
//                                if (tvBats2!!.currentTextColor == Color.YELLOW) {
//                                    ballFaced2 += 1
//                                    bat2Run += ballRun
//                                    tvBatsRun2!!.text = "$bat2Run($ballFaced2)"
//                                }
//                                if (strikeChng == 1) {
//                                    if (tvBats1!!.currentTextColor == Color.YELLOW) {
//                                        tvBats1!!.setTextColor(Color.WHITE)
//                                        tvBats2!!.setTextColor(Color.YELLOW)
//                                    } else {
//                                        tvBats1!!.setTextColor(Color.YELLOW)
//                                        tvBats2!!.setTextColor(Color.WHITE)
//                                    }
//                                }
//                                if (ball == 6) {
//                                    Toast.makeText(this@ManualScoringActivity, "OVER", Toast.LENGTH_SHORT).show()
//                                    arrayRun.clear()
//                                    dialogBoxBowler()
//
//                                    overRun = 0
//                                    overWkt = 0
//                                    //            return;
//                                    ball = 0
//                                    overs += 1
//
//                                }
//                            } else
//                                Toast.makeText(
//                                    this@ManualScoringActivity,
//                                    data.msg,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                        } else {
//                            val msg =
//                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
//                                    .getString("msg")
//                            Toast.makeText(this@ManualScoringActivity, msg, Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Citites?>, t: Throwable) {
//                        loader.visibility = View.GONE
//                        Toast.makeText(this@ManualScoringActivity, t.message, Toast.LENGTH_SHORT)
//                            .show()
//
//                    }
//
//                })
//        }
//    }
//
//}