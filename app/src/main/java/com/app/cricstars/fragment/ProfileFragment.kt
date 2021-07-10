package com.app.cricstars.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.app.cricstars.R
import com.app.cricstars.model.GenericResponse
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.app.cricstars.utils.UserData
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_recent_matches.*
import kotlinx.android.synthetic.main.fragment_recent_matches.loader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var userData: UserData? = null
    var name:TextView?=null
    var phone:TextView?=null
    var email:TextView?=null
    var password:TextView?=null
    var update:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userData = UserData(activity!!)
        name=view.findViewById(R.id.name)
        phone=view.findViewById(R.id.phone)
        email=view.findViewById(R.id.email)
        password=view.findViewById(R.id.password)
        update=view.findViewById(R.id.update)
        name!!.text=userData?.name
        phone!!.text=userData?.mobile
        email!!.text=userData?.email
        password!!.text=userData?.password
        dob!!.setText(userData?.dob!!)
        val battingArr= activity!!.resources.getStringArray(R.array.batting_style)
        for((index,value) in battingArr.iterator().withIndex())
        {
            if(value== userData!!.batting_style)
                batting_style.setSelection(index)
        }
        val playingArr= activity!!.resources.getStringArray(R.array.playig_rol)
        for((index,value) in playingArr.iterator().withIndex())
        {
            if(value== userData!!.player_style)
                playing_style.setSelection(index)
        }

        val bowlingArr= activity!!.resources.getStringArray(R.array.bowling_style)
        for((index,value) in bowlingArr.iterator().withIndex())
        {
            if(value== userData!!.bowling_style)
                bowling_style.setSelection(index)
        }

        update!!.setOnClickListener {
            if(name!!.text.toString().isNullOrEmpty()){
                Toast.makeText(activity, "Please enter name", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
            }else if(phone!!.text.toString().isNullOrEmpty()){
                Toast.makeText(activity, "Please enter phone no", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else  if(email!!.text.toString().isNullOrEmpty()){
                Toast.makeText(activity, "Please enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else  if(password!!.text.toString().isNullOrEmpty()){
                Toast.makeText(activity, "Please enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else  if(dob!!.text.toString().isNullOrEmpty()){
                Toast.makeText(activity, "Please enter date of birth", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                updateProfile()
            }
        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun updateProfile() {
        loader.visibility= View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch{
            val retrofitMethod = ApiBuilder.getRetrofitInstance(activity!!)?.create(
                RetrofitMethods::class.java
            )

            retrofitMethod?.updateProfie(userData?.uid!!,name!!.text.toString(),email!!.text.toString(),phone!!.text.toString(),password!!.text.toString(),batting_style.selectedItem.toString(),playing_style.selectedItem.toString(),bowling_style.selectedItem.toString(),dob.text.toString())?.enqueue(
                object : Callback<GenericResponse?> {
                    override fun onResponse(call: Call<GenericResponse?>, response: Response<GenericResponse?>) {
                        loader.visibility = View.INVISIBLE
                        if (response.isSuccessful) {
                            response.body()!!
                            userData!!.updateInfo(
                                name!!.text.toString(),
                                email!!.text.toString(),
                                phone!!.text.toString(),
                                true,
                                password!!.text.toString(),
                                playing_style.selectedItem.toString(),
                                batting_style.selectedItem.toString(),
                                bowling_style.selectedItem.toString(),
                                dob.text.toString()
                                )
                            Toast.makeText(activity!!, "Profile update successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getJSONObject("data").getString("msg")
                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse?>, t: Throwable) {
                        loader.visibility = View.INVISIBLE
                        Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }
}