package com.app.cricstars.retrofit

import com.app.cricstars.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

public interface RetrofitMethods {

    @FormUrlEncoded
    @POST("login.php")
    fun login(@Field("email") email: String?, @Field("password") password: String?): Call<Login?>?


    @FormUrlEncoded
    @POST("signup.php")
    fun signup(@Field("email") email: String?, @Field("password") password: String?,@Field("name") name:String?,@Field("mobile") moile:String?): Call<Signup?>?

    @FormUrlEncoded
    @POST("forgotPassword.php")
    fun forgotPassword(@Field("mobile") mobile: String?, @Field("otp") otp: String?): Call<GenericResponse?>?

    @FormUrlEncoded
    @POST("change_password.php")
    fun changePassword(@Field("mobile") mobile: String?, @Field("password") otp: String?): Call<GenericResponse?>?

    @GET("matches.php")
    fun getmatches():Call<MatchDetail>

    @GET("recent_matches.php")
    fun recentmatches():Call<MatchDetail>

    @FormUrlEncoded
    @POST("user_matches.php")
    fun usermatches(@Field("user_id") user_id: String):Call<MatchDetail>

    @FormUrlEncoded
    @POST("getcities.php")
    fun getcities(@Field("state_id")stateId: String):Call<AllCities>

    @GET("getstates.php")
    fun getstate():Call<State>

    @FormUrlEncoded
    @POST("getgrounds.php")
    fun getgrounds(@Field("city") ground_name:String):Call<GenericResponse>

    @FormUrlEncoded
    @POST("get_tournament.php")
    fun getTournament(@Field("email") id:String):Call<Tournament>

    @FormUrlEncoded
    @POST("get_tournament_matches.php")
    fun getTournamentMatches(@Field("tournament_id") id:String):Call<MatchDetail>

    @GET("getteams.php")
    fun getTeams():Call<Team>

    @FormUrlEncoded
    @POST("getplayers.php")
    fun getPlayers(@Field("team_id") team_id:String):Call<Players>

    @FormUrlEncoded
    @POST("add_match.php")
    fun addMatch(@Field("user_id") user_id:String,@Field("match_title") match_title:String,@Field("team1_id") team1_id:String,@Field("team2_id") team2_id:String,@Field("toss_won") toss_won:String,@Field("overs") overs:String,@Field("choose_bat_bowl") choose_bat_bowl:String,@Field("batsman1_id") batsman1_id:String,@Field("batsman2_id") batsman2_id:String,@Field("bowler_id") bowler_id:String):Call<GenericResponse>

    @FormUrlEncoded
    @POST("add_score.php")
        fun addScore(@Field("user_id") user_id:String,@Field("bat_team_id") bat_team_id:String,@Field("bowl_team_id") bowl_team_id:String,@Field("bat_member_id") bat_member_id:String,@Field("non_strike_batsman") non_strike_batsman:String,@Field("bowl_member_id") bowl_member_id:String,@Field("t10_match_id") t10_match_id:String,@Field("run_type") run_type:String,@Field("extras") extras:String,@Field("extras_run") extras_run:String,@Field("four_count") four_count:String,@Field("six_count") six_count:String,@Field("runs") runs:String,@Field("wkt_type") wkt_type:String,@Field("wicket") wicket:String,@Field("wicket_help_member_id") wicket_help_member_id:String,@Field("new_batsman_id") new_batsman_id:String,@Field("next_bowler") next_bowler:String,@Field("strike1") strike1:String,@Field("strike2") strike2:String,@Field("bowl_to_count") bowl_to_count:String,@Field("over") over:String,@Field("ball_number") ball_number:String):Call<GenericResponse>

    @FormUrlEncoded
    @POST("add_player.php")
    fun addPlayer(@Field("state_id") state_id: String, @Field("city_id") city_id: String,@Field("team_id") team_id: String,@Field("name") name: String,@Field("email") email: String,@Field("age") age: String,@Field("phone") phone: String,@Field("memeber_type") memeber_type: String): Call<GenericResponse?>?
    @FormUrlEncoded
    @POST("edit_player.php")
    fun editPlayer(@Field("state_id") state_id: String, @Field("city_id") city_id: String,@Field("name") name: String,@Field("email") email: String,@Field("age") age: String,@Field("phone") phone: String,@Field("memeber_type") memeber_type: String,@Field("member_id") memeber_id: String): Call<GenericResponse?>?



    @FormUrlEncoded
    @POST("getScoreSummary.php")
    fun getScore(@Field("match_id") match_id:String):Call<MatchScoreDetail>

    @FormUrlEncoded
    @POST("undo_score.php")
    fun undoScore(@Field("match_id") match_id:String):Call<GenericResponse>

    @Multipart
    @POST("add_tournament.php")
    fun addTournament(@Part("tournament_name") tournament_name: RequestBody, @Part tournament_banner:MultipartBody.Part, @Part tournament_logo:MultipartBody.Part, @Part("city") city:RequestBody, @Part("ground") ground:RequestBody, @Part("organiser_name") organiser_name:RequestBody, @Part("organiser_phone") organiser_phone:RequestBody, @Part("start_date") start_date:RequestBody, @Part("end_date") end_date:RequestBody, @Part("ball_type") ball_type:RequestBody, @Part("overs") overs:RequestBody, @Part("about") about:RequestBody):Call<GenericResponse>

    @FormUrlEncoded
    @POST("update_profile.php")
    fun updateProfie(@Field("user_id") user_id:String,@Field("name") name:String,@Field("email") email:String,@Field("phone") phone:String,@Field("password") password:String,@Field("batting_role")battingRole:String,@Field("player_role") playerRole:String,@Field("bowling_role") bowling_role:String,@Field("dob") dob:String):Call<GenericResponse>

    @FormUrlEncoded
    @POST("add_team.php")
    fun addTeam(@Field("state_id") state_id:String,@Field("city_id") city_id:String,@Field("team_name") team_name:String,@Field("captain_name") captain_name:String,@Field("captain_email") captain_email:String,@Field("captain_password") captain_password:String,@Field("phone") phone:String,@Field("occupation") occupation:String,@Field("age") age:String,@Field("memeber_type") memberTyype:String):Call<AddTeam>

    @FormUrlEncoded
    @POST("get_user_teams.php")
    fun getUserTeam(@Field("email") email:String):Call<Team>

    @FormUrlEncoded
    @POST("get_team_members.php")
    fun getTeamMember(@Field("team_id") email:String):Call<TeamMember>

    @FormUrlEncoded
    @POST("getCommentary.php")
    fun getCommentary(@Field("match_id") match_id:String):Call<Commentary>

    @FormUrlEncoded
    @POST("current_bowler_detail.php")
    fun getCurrentBowler(@Field("match_id") match_id:String,@Field("bowler_id") bowler_id:String):Call<CurrentBowler>

}
