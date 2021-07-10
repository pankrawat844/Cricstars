package com.app.cricstars.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.cricstars.R
import com.google.android.material.internal.ContextUtils.getActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WebviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WebviewFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val INPUT_FILE_REQUEST_CODE = 1
    private val FILECHOOSER_RESULTCODE = 1
    private val TAG: String = WebviewFragment::class.java.getSimpleName()
    private val mRootLayout: LinearLayout? = null
    private var webView: WebView? = null
    private val webSettings: WebSettings? = null
    private var mUploadMessage: ValueCallback<Uri?>? = null
    private val mCapturedImageURI: Uri? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>?>? = null
    private val mCameraPhotoPath: String? = null

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    // Storage Permissions variables
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val filePath: String? = null
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.MODIFY_AUDIO_SETTINGS,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )


    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have read or write permission
        val writePermission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        val cameraPermission =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        val locationPermission =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        val audioPermission =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.MODIFY_AUDIO_SETTINGS)
        val recordPermission =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED || locationPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    // For offline cache
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var results: Array<Uri>? = null

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = arrayOf<Uri>(Uri.parse(mCameraPhotoPath))
                    }
                } else {
                    val dataString = data.dataString
                    if (dataString != null) {
                        results = arrayOf<Uri>(Uri.parse(dataString))
                    }
                }
            }
            mFilePathCallback!!.onReceiveValue(results)
            mFilePathCallback = null
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == mUploadMessage) {
                    return
                }
                var result: Uri? = null
                try {
                    result = if (resultCode != Activity.RESULT_OK) {
                        null
                    } else {

                        // retrieve from the private variable if the intent is null
                        if (data == null) mCapturedImageURI else data.data
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        activity, "activity :$e",
                        Toast.LENGTH_LONG
                    ).show()
                }
                mUploadMessage!!.onReceiveValue(result)
                mUploadMessage = null
            }
        }
        return
    }


    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(com.app.cricstars.R.layout.fragment_webview, container, false)
        verifyStoragePermissions(activity)
        //        Objects.requireNonNull(getActivity().getSupportActionBar()).hide();
        webView = view.findViewById<View>(R.id.webView) as WebView
        val webSettings = webView!!.settings
        webSettings.setAppCacheEnabled(true)
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.allowFileAccess = true
        webView!!.webViewClient = WebViewClient()
        webView!!.setWebChromeClient(object :WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                callback.invoke(origin, true, false)
            }

            override fun onPermissionRequest(request: PermissionRequest) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources())
                }
            }
        })
        //if SDK version is greater of 19 then activate hardware acceleration otherwise activate software acceleration
        //if SDK version is greater of 19 then activate hardware acceleration otherwise activate software acceleration
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.mixedContentMode = 0
            webView!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else if (Build.VERSION.SDK_INT < 19) {
            webView!!.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
//        if (savedInstanceState == null) {
            webView!!.post { webView!!.loadUrl(  arguments!!.getString("url")) }
//        }
        webView!!.settings.setUserAgentString("Mozilla/5.0 (Linux; Android 9; Pixel 3 XL) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Mobile Safari/537.36")
        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (url == null || url.startsWith("http://") || url.startsWith("https://")) false else try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    view.context.startActivity(intent)
                    true
                } catch (e: Exception) {
                    true
                }
            }
        }
        webView!!.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimeType)
            //------------------------COOKIE!!------------------------
            val cookies: String = CookieManager.getInstance().getCookie(url)
            request.addRequestHeader("cookie", cookies)
            //------------------------COOKIE!!------------------------
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading file...")
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                URLUtil.guessFileName(url, contentDisposition, mimeType)
            )
            val dm = activity!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(activity, "Downloading File", Toast.LENGTH_LONG).show()
        }

        // Error handling

//improve webview performance
        webView!!.settings.loadsImagesAutomatically = true
        webView!!.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webView!!.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView!!.settings.pluginState = WebSettings.PluginState.ON
        webView!!.settings.allowFileAccess = true
        webSettings.domStorageEnabled = true
        webView!!.settings.setAppCacheEnabled(true)
        webView!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSettings.useWideViewPort = true
        webSettings.saveFormData = true
        webSettings.setEnableSmoothTransition(true)


        //pull to refresh
        swipeRefreshLayout = view.findViewById<View>(R.id.swipe) as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener {
            swipeRefreshLayout!!.isRefreshing = true
            Handler().postDelayed(Runnable {
                swipeRefreshLayout!!.isRefreshing = false
                webView!!.reload()
            }, 1000)
        }
        swipeRefreshLayout!!.setColorSchemeColors(
            resources.getColor(android.R.color.holo_blue_dark),
            resources.getColor(android.R.color.holo_orange_dark),
            resources.getColor(android.R.color.holo_green_dark),
            resources.getColor(android.R.color.holo_red_dark)
        )
        // Offline View
        if (!isNetworkAvailable()) { // loading offline
            webView!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(
                viewLifecycleOwner, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (webView!!.canGoBack()) {
                            webView!!.goBack()
                        } else {
                            activity!!.finish()
                        }
                    }
                }
            )
        return view
    }

    //Offline Cache
    private fun enableWVCache() {

        webView!!.settings.domStorageEnabled = true

        // Set cache size to 8 mb by default. should be more than enough
        webView!!.settings.setAppCacheMaxSize((1024 * 1024 * 8).toLong())
        val dir: File = activity!!.cacheDir
        if (!dir.exists()) {
            dir.mkdirs()
        }
        webView!!.settings.setAppCachePath(dir.getPath())
        webView!!.settings.allowFileAccess = true
        webView!!.settings.setAppCacheEnabled(true)
        webView!!.settings.cacheMode = WebSettings.LOAD_DEFAULT
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }

    //    @Override
    //    public AssetManager getAssets() {
    //        return getResources().getAssets();
    //    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        webView.saveState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        webView.restoreState(savedInstanceState);
//    }


    //    @Override
    //    protected void onSaveInstanceState(Bundle outState) {
    //        super.onSaveInstanceState(outState);
    //        webView.saveState(outState);
    //    }
    //
    //    @Override
    //    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    //        super.onRestoreInstanceState(savedInstanceState);
    //        webView.restoreState(savedInstanceState);
    //    }
//    class PQClient : WebViewClient() {
//        var progressDialog: ProgressDialog? = null
//        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//
//            // If url contains mailto link then open Mail Intent
//            return if (url.contains("mailto:")) {
//
//                // Could be cleverer and use a regex
//                //Open links in new browser
//                view.context.startActivity(
//                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                )
//
//                // Here we can open new activity
//                true
//            } else {
//
//                // Stay within this webview and load url
//                view.loadUrl(url)
//                true
//            }
//        }
//
//        //Show loader on url load
//        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
//
//            // Then show progress  Dialog
//            // in standard case YourActivity.this
//            if (progressDialog == null) {
//                progressDialog = ProgressDialog(getActivity())
//                progressDialog!!.setMessage("Loading...")
//                progressDialog!!.hide()
//            }
//        }
//
//        // Called when all page resources loaded
//        override fun onPageFinished(view: WebView, url: String) {
//            webView.loadUrl(
//                "javascript:(function(){ " +
//                        "document.getElementById('android-app').style.display='none';})()"
//            )
//            try {
//                // Close progressDialog
//                if (progressDialog!!.isShowing) {
//                    progressDialog!!.dismiss()
//                    progressDialog = null
//                }
//            } catch (exception: Exception) {
//                exception.printStackTrace()
//            }
//        }
//
//    }

//      open class MyChrome  : WebChromeClient() {
//        private var mCustomView: View? = null
//        private var mCustomViewCallback: CustomViewCallback? = null
//        protected var mFullscreenContainer: FrameLayout? = null
//        private var mOriginalOrientation = 0
//        private var mOriginalSystemUiVisibility = 0
//
//        @SuppressLint("RestrictedApi")
//        override fun getDefaultVideoPoster(): Bitmap? {
//            return if (mCustomView == null) {
//                null
//            } else BitmapFactory.decodeResource(getActivity()!!.getResources(), 2130837573)
//
//        }
//
//        override fun onHideCustomView() {
//            (getActivity()!!.getWindow().getDecorView() as FrameLayout).removeView(mCustomView)
//            mCustomView = null
//            getActivity()!!.getWindow().getDecorView()
//                .setSystemUiVisibility(mOriginalSystemUiVisibility)
//            getActivity().setRequestedOrientation(mOriginalOrientation)
//            mCustomViewCallback!!.onCustomViewHidden()
//            mCustomViewCallback = null
//        }
//
//        override fun onShowCustomView(
//            paramView: View,
//            paramCustomViewCallback: CustomViewCallback
//        ) {
//            if (mCustomView != null) {
//                onHideCustomView()
//                return
//            }
//            mCustomView = paramView
//            mOriginalSystemUiVisibility =
//                getActivity().getWindow().getDecorView().getSystemUiVisibility()
//            mOriginalOrientation = getActivity().getRequestedOrientation()
//            mCustomViewCallback = paramCustomViewCallback
//            (getActivity().getWindow().getDecorView() as FrameLayout).addView(
//                mCustomView,
//                FrameLayout.LayoutParams(-1, -1)
//            )
//            getActivity()!!.getWindow().getDecorView()
//                .setSystemUiVisibility(3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
//        }
//
//        override fun onCreateWindow(
//            view: WebView,
//            dialog: Boolean,
//            userGesture: Boolean,
//            resultMsg: Message
//        ): Boolean {
//            val result = view.hitTestResult
//            val data = result.extra
//            val context: Context = view.context
//            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data))
//            context.startActivity(browserIntent)
//            return false
//        }
//
//        // File upload picker
//        // For Android 5.0
//        override fun onShowFileChooser(
//            view: WebView?,
//            filePath: ValueCallback<Array<Uri?>?>,
//            fileChooserParams: FileChooserParams?
//        ): Boolean {
//            // Double check that we don't have any existing callbacks
//            if (mFilePathCallback != null) {
//                mFilePathCallback.onReceiveValue(null)
//            }
//            mFilePathCallback = filePath
//            var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            if (takePictureIntent!!.resolveActivity(getActivity().getPackageManager()) != null) {
//                // Create the File where the photo should go
//                var photoFile: File? = null
//                try {
//                    photoFile = createImageFile()
//                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
//                } catch (ex: IOException) {
//                    // Error occurred while creating the File
//                    Log.e("TAG", "Unable to create Image File", ex)
//                }
//
//                // Continue only if the File was successfully created
//                if (photoFile != null) {
//                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath()
//                    takePictureIntent.putExtra(
//                        MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(photoFile)
//                    )
//                } else {
//                    takePictureIntent = null
//                }
//            }
//            val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
//            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
//            contentSelectionIntent.type = "*/*"
//            val intentArray: Array<Intent?>
//            intentArray = takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
//            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
//            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
//            chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser")
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
//            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
//            return true
//        }
//
//        // openFileChooser for Android 3.0+
//        fun openFileChooser(uploadMsg: ValueCallback<Uri?>, acceptType: String?) {
//            mUploadMessage = uploadMsg
//            // Create AndroidExampleFolder at sdcard
//            // Create AndroidExampleFolder at sdcard
//            val imageStorageDir = File(
//                Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES
//                ), "AndroidExampleFolder"
//            )
//            if (!imageStorageDir.exists()) {
//                // Create AndroidExampleFolder at sdcard
//                imageStorageDir.mkdirs()
//            }
//
//            // Create camera captured image file path and name
//            val file = File(
//                imageStorageDir + File.separator.toString() + "IMG_" + System.currentTimeMillis()
//                    .toString() + ".jpg"
//            )
//            Log.d("File", "File: $file")
//            mCapturedImageURI = Uri.fromFile(file)
//
//            // Camera capture image intent
//            val captureIntent = Intent(
//                MediaStore.ACTION_IMAGE_CAPTURE
//            )
//            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI)
//            val i = Intent(Intent.ACTION_GET_CONTENT)
//            i.addCategory(Intent.CATEGORY_OPENABLE)
//            i.type = "*/*"
//
//            // Create file chooser intent
//            val chooserIntent = Intent.createChooser(i, "File Chooser")
//
//            // Set camera intent to file chooser
//            chooserIntent.putExtra(
//                Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(captureIntent)
//            )
//
//            // On select image call onActivityResult method of activity
//            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE)
//        }
//
//        // openFileChooser for Android < 3.0
//        fun openFileChooser(uploadMsg: ValueCallback<Uri?>) {
//            openFileChooser(uploadMsg, "")
//        }
//
//        //openFileChooser for other Android versions
//        fun openFileChooser(
//            uploadMsg: ValueCallback<Uri?>,
//            acceptType: String?,
//            capture: String?
//        ) {
//            openFileChooser(uploadMsg, acceptType)
//        }
//    }
}