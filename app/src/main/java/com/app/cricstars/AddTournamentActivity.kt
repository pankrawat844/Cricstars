package com.app.cricstars

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.cricstars.model.AllCities
import com.app.cricstars.model.GenericResponse
import com.app.cricstars.retrofit.ApiBuilder
import com.app.cricstars.retrofit.RetrofitMethods
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_add_tournament.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddTournamentActivity : AppCompatActivity() {
    private var image1Path: File?=null
    private var image2Path: File?=null
    val myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tournament)
//        supportActionBar!!.title = "Add Tournament"
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvStartDate.setOnClickListener {
            DatePickerDialog(
                this@AddTournamentActivity, date, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        tvEndDate.setOnClickListener {
            DatePickerDialog(
                this@AddTournamentActivity, date_end, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
        imgVProfileEmpty.setOnClickListener {
            Dexter.withContext(this@AddTournamentActivity).withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object:PermissionListener{
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        intent.type = "image/*"
                        startActivityForResult(intent, 1)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                p1?.continuePermissionRequest()
                    }
                }).check()

        }

        imgCircleIcon.setOnClickListener {
            Dexter.withContext(this@AddTournamentActivity).withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object:PermissionListener{
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        intent.type = "image/*"
                        startActivityForResult(intent, 2)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        p1?.continuePermissionRequest()
                    }
                }).check()

        }
        btnSave.setOnClickListener {

            if (etTournamentName.text.toString().isNullOrEmpty()) {
                ilTournamentname.error = "Enter tournament name"
                ilTournamentname.isErrorEnabled = true

                Snackbar.make(it, "Please enter tournament name", Snackbar.LENGTH_SHORT).show()
            } else if (atCity.text.toString().isNullOrEmpty()) {
                ilCity.error = "Enter city name"
                ilCity.isErrorEnabled = true
                Snackbar.make(it, "Please enter city name", Snackbar.LENGTH_SHORT).show()
            } else if (atGround.text.toString().isNullOrEmpty()) {
                ilGround.error = "Enter ground name"
                ilGround.isErrorEnabled = true
                Snackbar.make(it, "Please enter ground name", Snackbar.LENGTH_SHORT).show()
            } else if (etOrganizerName.text.toString().isNullOrEmpty()) {
                ilOrgName.error = "Enter Organizer name"
                ilOrgName.isErrorEnabled = true
                Snackbar.make(it, "Please enter Organizer name", Snackbar.LENGTH_SHORT).show()
            } else if (etOrganizerNumber.text.toString().isNullOrEmpty()) {
                ilOrgNumber.error = "Enter Organizer number"
                ilOrgNumber.isErrorEnabled = true
                Snackbar.make(it, "Please enter Organizer number", Snackbar.LENGTH_SHORT).show()
            } else if (tvStartDate.text.toString().isNullOrEmpty()) {
                tlStartDate.error = "Enter start date"
                tlStartDate.isErrorEnabled = true
                Snackbar.make(it, "Please enter start date", Snackbar.LENGTH_SHORT).show()
            } else if (tvEndDate.text.toString().isNullOrEmpty()) {
                tlEndDate.error = "Enter end date"
                tlEndDate.isErrorEnabled = true
                Snackbar.make(it, "Please enter end date", Snackbar.LENGTH_SHORT).show()
            } else {

                addTournament()
            }
        }
        getcities()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if(requestCode==1){
                if(resultCode== RESULT_OK){
                if(data!=null){
                    val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,data.data)
                    imgVProfileEmpty.setImageBitmap(bitmap)


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val parcelFileDescription=contentResolver.openFileDescriptor(data?.data!!,"r",null)?:return
                        val inputStream = FileInputStream(parcelFileDescription.fileDescriptor)

                        image1Path= File(cacheDir,contentResolver.getFileName(data.data!!))
                        val outputStream = FileOutputStream(image1Path)
                        inputStream.copyTo(outputStream)
                        Log.e("TAG", "onActivityResult: "+image1Path?.path)

                    } else {
                        image1Path= File(getPath(data?.data))

                        Log.e("TAG", "onActivityResult: "+getPath(data?.data))

                    }
                }
                }
            }
        if (requestCode === 2) {
            if (resultCode === Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            contentResolver,
                            data.data
                        )
                        imgCircleIcon.setImageBitmap(bitmap)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val parcelFileDescription=contentResolver.openFileDescriptor(data?.data!!,"r",null)?:return
                            val inputStream = FileInputStream(parcelFileDescription.fileDescriptor)

                            image2Path=File(cacheDir,contentResolver.getFileName(data.data!!))
                            val outputStream = FileOutputStream(image2Path)
                            inputStream.copyTo(outputStream)
                            Log.e("TAG", "onActivityResult: "+image2Path?.path)

                        } else {
                            image2Path=File(getPath(data?.data))

                            Log.e("TAG", "onActivityResult: "+getPath(data?.data))

                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else if (resultCode === Activity.RESULT_CANCELED) {
                Toast.makeText(this@AddTournamentActivity, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun ContentResolver.getFileName(uri: Uri):String{
        var name=""
        val cursor=query(uri,null,null,null,null)
        cursor?.use {
            it.moveToFirst()
            name=cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        return name
    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null)
        if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else
            return null
    }

    var date =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
    var date_end =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel1()
        }

    private fun updateLabel() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tvStartDate.setText(sdf.format(myCalendar.time))
    }

    private fun updateLabel1() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tvEndDate.setText(sdf.format(myCalendar.time))
    }


    private fun getcities() {
        CoroutineScope(Dispatchers.Main).launch {
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@AddTournamentActivity)?.create(
                RetrofitMethods::class.java
            )
            val response = retrofitMethod?.getcities("")?.enqueue(
                object : Callback<AllCities?> {
                    override fun onResponse(call: Call<AllCities?>, response: Response<AllCities?>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data?.success!!) {
                                atCity.threshold = 2
                                atCity.setAdapter(
                                    ArrayAdapter(
                                        this@AddTournamentActivity,
                                        android.R.layout.simple_dropdown_item_1line,
                                        data?.data!!
                                    )
                                )
                                atGround.setAdapter(
                                    ArrayAdapter(
                                        this@AddTournamentActivity,
                                        android.R.layout.simple_spinner_item,
                                        data?.data!!
                                    )
                                )

                            }
//                                Toast.makeText(
//                                    this@AddTournamentActivity,
//                                    data?.data?.msg,
//                                    Toast.LENGTH_SHORT
//                                ).show()
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getString("msg")
                            Toast.makeText(this@AddTournamentActivity, msg, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<AllCities?>, t: Throwable) {
                        Toast.makeText(this@AddTournamentActivity, t.message, Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }

        atCity.setOnItemClickListener { parent, view, position, id ->
            getGrounds()
        }
    }

    private fun getGrounds() {
        CoroutineScope(Dispatchers.Main).launch {
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@AddTournamentActivity)?.create(
                RetrofitMethods::class.java
            )
            val response = retrofitMethod?.getgrounds(atCity.text.toString())?.enqueue(
                object : Callback<GenericResponse?> {
                    override fun onResponse(call: Call<GenericResponse?>, response: Response<GenericResponse?>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data?.success!!) {
                                atGround.threshold = 2
                                atGround.setAdapter(
                                    ArrayAdapter(
                                        this@AddTournamentActivity,
                                        android.R.layout.simple_dropdown_item_1line,
                                        data?.data!!
                                    )
                                )

                            }
//                                Toast.makeText(
//                                    this@AddTournamentActivity,
//                                    data?.data?.msg,
//                                    Toast.LENGTH_SHORT
//                                ).show()
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getString("msg")
                            Toast.makeText(this@AddTournamentActivity, msg, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse?>, t: Throwable) {
                        Toast.makeText(this@AddTournamentActivity, t.message, Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }
    }

    private fun addTournament() {
        val mediaStorageDir = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/Android/data/"
                    + applicationContext.packageName
                    + "/Files"
        )

//        val bannerImageName =Calendar.getInstance().timeInMillis.toString()+"_banner."+image1Path?.extension
//        val bannerImageFile = File(mediaStorageDir.getPath() + File.separator.toString() + bannerImageName)
//        val bannerFileRequest= bannerImageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//        val bannerMultipart=MultipartBody.Part.createFormData("tournament_banner",bannerImageFile.name,bannerFileRequest)
//        val logoImageName=Calendar.getInstance().timeInMillis.toString()+"_logo."+image2Path?.extension
//        val logoImageFile=File(mediaStorageDir.path+File.separator.toString()+logoImageName)
//        val logoFileRequest=logoImageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//        val logonMultipart= MultipartBody.Part.createFormData("tournament_logo",logoImageFile.name,logoFileRequest)
        val image1RequestFile =
            image1Path?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
       val image1MultipartBody = MultipartBody.Part.createFormData(
            "tournament_banner",
            image1Path?.name,
            image1RequestFile!!
        )
        val image2RequestFile =
            image2Path?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val image2MultipartBody = MultipartBody.Part.createFormData(
            "tournament_logo",
            image2Path?.name,
            image2RequestFile!!
        )
        val tournamentName= etTournamentName.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val city= atCity.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val ground= atGround.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val organiser_name= etOrganizerName.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val organiser_phone= etOrganizerNumber.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val start_date= tvStartDate.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val end_date= tvEndDate.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        var balltype:String=""

            when(ballType.checkedRadioButtonId){
                R.id.rbTennis->balltype="Tennis"
                R.id.rbLeather->balltype="Leather"
                R.id.rbOther->balltype="Other"
            }
        val ballTypeRequest=balltype.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        var overs=""
        when(rGOvers.checkedRadioButtonId){
            R.id.rbOneInning->overs="Limited Overs"
            R.id.rbTwoInning->overs="Test Match"
        }
        val overRequest=overs.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val about=edtAboutTournament.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.Main).launch {
            val retrofitMethod = ApiBuilder.getRetrofitInstance(this@AddTournamentActivity)?.create(
                RetrofitMethods::class.java
            )


            val response = retrofitMethod?.addTournament(tournamentName,image1MultipartBody,image2MultipartBody,city,ground,organiser_name,organiser_phone,start_date,end_date,ballTypeRequest,overRequest,about)?.enqueue(
                object : Callback<GenericResponse?> {
                    override fun onResponse(call: Call<GenericResponse?>, response: Response<GenericResponse?>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data?.success!!) {

                                Toast.makeText(this@AddTournamentActivity, data?.msg!!, Toast.LENGTH_SHORT)
                                    .show()
                            finish()
                            }
//                                Toast.makeText(
//                                    this@AddTournamentActivity,
//                                    data?.data?.msg,
//                                    Toast.LENGTH_SHORT
//                                ).show()
                        } else {
                            val msg =
                                JSONObject(response.errorBody()?.string()).getJSONObject("data")
                                    .getString("msg")
                            Toast.makeText(this@AddTournamentActivity, msg, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse?>, t: Throwable) {
                        Toast.makeText(this@AddTournamentActivity, t.message, Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }
    }

}