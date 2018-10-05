package app.horecafy.com.activities.wholesalers.fragments


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.Retrofit.APIService
import app.horecafy.com.Retrofit.ApiClient
import app.horecafy.com.activities.wholesalers.WholesalerOfferActivity
import app.horecafy.com.activities.wholesalers.WholesalerOfferActivity.Companion.APP_DIR
import app.horecafy.com.activities.wholesalers.WholesalerOfferActivity.Companion.COMPRESSED_VIDEOS_DIR
import app.horecafy.com.activities.wholesalers.WholesalerOfferActivity.Companion.thumbFile
import app.horecafy.com.models.ProposalItems
import app.horecafy.com.models.RestaurantsTypes
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.WholesalerService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.Constants.Companion.bitmapToFile
import app.horecafy.com.util.Constants.Companion.try2CreateCompressDir
import app.horecafy.com.util.UiHelpers
import com.yovenny.videocompress.MediaController
import kotlinx.android.synthetic.main.activity_wholesaler_offer.*
import kotlinx.android.synthetic.main.fragment_wholesaler_submit_proposals.*
import kotlinx.android.synthetic.main.fragment_wholesaler_submit_proposals.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.*
import java.util.*


class WholesalerSubmitProposalsFragment : Fragment() {

    var mBusinessTypeslist: MutableList<RestaurantsTypes> = mutableListOf()
    var mRestaurantsNamesList: MutableList<String> = mutableListOf()
    var mSelectedRestaurantsId: String = ""

    //Image / Video Upload
    private var imgPOS = 1

    //popup code
    private val GALLERY = 111
    private val GALLERY_V = 444
    private val CAMERA = 222
    private val VIDEO = 333

    //video
    private var videoToUploadUri: Uri? = null
    private var videoToUploadUri_comp: String? = null
    private var videoPathBeforeComp = ""

    //Path
    private var videoPath = ""
    private var imagePath1 = ""
    private var imagePath2 = ""
    private var imagePath3 = ""

    companion object {
        fun newInstance(): WholesalerSubmitProposalsFragment {
            val fragment = WholesalerSubmitProposalsFragment()
            /*val bundle = Bundle()
            bundle.putString("Text", text)
            fragment.arguments = bundle*/
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wholesaler_submit_proposals, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRestaurantsNamesList.add(Constants.CONSTANT_SELECT_THE_TYPE_OF_RESTAURANTS)

        if (acsTypeofRestaurants != null) {
            acsTypeofRestaurants.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    if (mRestaurantsNamesList[position].equals(Constants.CONSTANT_SELECT_THE_TYPE_OF_RESTAURANTS)) {

                        mSelectedRestaurantsId = ""
                    } else {

                        mSelectedRestaurantsId = mBusinessTypeslist[position - 1].id.toString()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    mSelectedRestaurantsId = ""
                }
            }
        }

        img_f_C.setOnClickListener(View.OnClickListener {
            imgPOS = 1
            if (checkAndRequestPermissions(activity)) {
                showPictureDialog()
            }
        })

        img_s_C.setOnClickListener(View.OnClickListener {
            imgPOS = 2
            if (checkAndRequestPermissions(activity)) {
                showPictureDialog()
            }
        })

        img_t_C.setOnClickListener(View.OnClickListener {
            imgPOS = 3
            if (checkAndRequestPermissions(activity)) {
                showPictureDialog()
            }
        })

        vid_f_C.setOnClickListener(View.OnClickListener {
            imgPOS = 4
            if (checkAndRequestPermissions(activity)) {
                showPictureDialog()
            }
        })

        loadBusinessTypesList()

        btnSubmitProposal.setOnClickListener({ v: View? ->

            if (isValid()) {

                val strZipCode = etZipCode.text.toString()
                val strProposalDescription = etProposalDescription.text.toString()

                UiHelpers.showProgessBar(activity.window, rlProgressBarSubmitProposal)

                val wholesalerId = AuthService.wholesaler!!.hiddenId!!
//                Log.e("Wholesaler HiddenId", "" + wholesalerId)

                val id = AuthService.wholesaler!!.id!!
//                Log.e("Wholesaler Id", "" + id)

                val inputStrProposal = ProposalItems(wholesalerId,
                        strZipCode, mSelectedRestaurantsId, strProposalDescription)

                WholesalerService.submitProposal(activity, inputStrProposal) { status, error, GroupID ->

                    UiHelpers.hideProgessBar(activity.window, rlProgressBarSubmitProposal)

                    if (status) {
                        Log.e("AAAAAA","GROUP ID : : "+GroupID)
                        if (!imagePath1.equals("") || !imagePath2.equals("") || !imagePath3.equals("") || !videoPath.equals("")) {

                            UploadImageVideo(GroupID)

                        }else{
                            Toast.makeText(activity, "propuesta enviada correctamente.", Toast.LENGTH_LONG).show()
                            etZipCode.setText(Constants.CONSTANT_EMPTY_STRING)
                            acsTypeofRestaurants.setSelection(0)
                            etProposalDescription.setText(Constants.CONSTANT_EMPTY_STRING)
                        }

                    } else {

                        Toast.makeText(activity, "Algo salió mal. Inténtalo de nuevo.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun UploadImageVideo(groupID: String) {
        UiHelpers.showProgessBar(activity.window, rlProgressBarSubmitProposal)

        var bodyvideo: MultipartBody.Part? = null
        val partsList = ArrayList<MultipartBody.Part>()
        val imageArray = ArrayList<String>()
        Log.e("ADDDDD", "22  : : NOT ZERO : "+imagePath1+" : "+imagePath2+" : "+imagePath3+" : "+videoPath)
        if (!imagePath1.equals("")){
            imageArray.add(imagePath1)
        }
        if (!imagePath2.equals("")){
            imageArray.add(imagePath2)
        }
        if (!imagePath3.equals("")){
            imageArray.add(imagePath3)
        }

        if (imageArray.size > 0) {
            for (index in imageArray.indices) {
                Log.e("AddClaim", "88  : : "+imageArray.indices)
                Log.e("AddClaim", "88  " + imageArray.get(index))
                partsList.add(prepareFilePart("images", imageArray.get(index)))
            }
        }else{
            Log.e("AddClaim", "88  : : Zero Array")
        }

        if(!videoPath.equals("")){
            val file = File(videoPath)
            val reqFile = RequestBody.create(MediaType.parse("video/*"), file)
            bodyvideo = MultipartBody.Part.createFormData("video", file.getName(), reqFile)
        }else{

        }
        Log.e("ADDDDD", "22  : : NOT ZERO : "+imageArray.size)
        val apiService = ApiClient.getClient(activity).create(APIService::class.java)

        val call  = apiService.proposalUploadArray(groupID,partsList,bodyvideo)
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                UiHelpers.hideProgessBar(activity.window, rlProgressBarSubmitProposal)

                if (response.isSuccessful) {
                    try {
                        var res: String? = null
                        try {
                            res = response.body().string()
                            Log.e("res ", "Image Upload Praposal : " + res!!)
                            Toast.makeText(activity, "propuesta enviada correctamente.", Toast.LENGTH_LONG).show()
                            imagePath1 = ""
                            imagePath2 = ""
                            imagePath3 = ""
                            videoPath = ""
                            img_f_C.setImageResource(R.drawable.ic_add_category)
                            img_s_C.setImageResource(R.drawable.ic_add_category)
                            img_t_C.setImageResource(R.drawable.ic_add_category)
                            vid_f_C.setImageResource(R.drawable.ic_add_category)
                            etZipCode.setText(Constants.CONSTANT_EMPTY_STRING)
                            acsTypeofRestaurants.setSelection(0)
                            etProposalDescription.setText(Constants.CONSTANT_EMPTY_STRING)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        val res = response.errorBody().string()
                        Log.e("res ", "ERROR Response : $res")
                        val jsonObject = JSONObject(res)
                        val success = jsonObject.optBoolean("success")
                        if (success == false) {
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                UiHelpers.hideProgessBar(activity.window, rlProgressBarSubmitProposal)
                Log.e("CCC", "Failure: " + call)
            }
        })
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun prepareFilePart(partName: String, file: String): MultipartBody.Part {

        val file = File(file)
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData(partName, file.getName(), reqFile)
    }

    private fun loadBusinessTypesList() {
        UiHelpers.showProgessBar(activity.window, rlProgressBarSubmitProposal)
        WholesalerService.getBusinessTypeList(activity) { status, data, error ->
            UiHelpers.hideProgessBar(activity.window, rlProgressBarSubmitProposal)
            if (status) {

                mBusinessTypeslist = data.toMutableList()
                mBusinessTypeslist.removeAt(0)

                for (i in mBusinessTypeslist.indices) {

                    mRestaurantsNamesList.add(mBusinessTypeslist[i].name)
                }

                if (mRestaurantsNamesList.isEmpty()) {
                    Log.e("List", "Empty")
                } else {
                    Log.e("List Size", "" + mRestaurantsNamesList.size)
                    val arrayAdapter = ArrayAdapter(activity,
                            android.R.layout.simple_spinner_dropdown_item, mRestaurantsNamesList)
                    acsTypeofRestaurants.adapter = arrayAdapter as SpinnerAdapter?
                }
            }
        }
    }

    private fun isValid(): Boolean {
        var isValid = true

        val strZipCode = etZipCode.text.toString()
        val strProposalDescription = etProposalDescription.text.toString()

        /*if (mSelectedRestaurantsId.isEmpty()) {
            Toast.makeText(activity,
                    "Seleccione el tipo de restaurantes.", Toast.LENGTH_SHORT).show()
            isValid = false
        }*/

        if (strZipCode.isNullOrEmpty()) {
            etZipCode.setError("Este campo es obligatorio")
            isValid = false
        }

        if (strProposalDescription.isNullOrEmpty()) {
            etProposalDescription.setError("Este campo es obligatorio")
            isValid = false
        }

        return isValid
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Seleccione la acción")
        if (imgPOS == 4) {
            val pictureDialogItems = arrayOf("Seleccionar video de la galería", "Capture video de la cámara")
            pictureDialog.setItems(pictureDialogItems
            ) { dialog, which ->
                when (which) {
                    0 -> chooseVideoFromGallary()
                    1 -> takeVideoFromCamera()
                }
            }
        } else {
            val pictureDialogItems = arrayOf("Seleccionar foto de la galería", "Capturar fotos desde la cámara")

            pictureDialog.setItems(pictureDialogItems
            ) { dialog, which ->
                when (which) {
                    0 -> choosePhotoFromGallary()
                    1 -> takePhotoFromCamera()
                }
            }
        }

        pictureDialog.show()
    }

    private fun takeVideoFromCamera() {
        var f: File
        f = File("" + Environment.getExternalStorageDirectory())
        if (!f.exists()) {
            f.mkdir()
        }
        f = File(Environment.getExternalStorageDirectory(), "/myvideo.mp4")
        if (!f.exists()) {
            try {
                f.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val f1: File
        f1 = File(Environment.getExternalStorageDirectory(), "/horecafy_video/")
        if (!f1.exists()) {
            try {
                f1.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val chooserIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity.getApplicationContext(),
                "app.horecafy.com",
                f))
        videoToUploadUri = Uri.fromFile(f)
       videoToUploadUri_comp = f1.toString()
        chooserIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 90)
        startActivityForResult(chooserIntent,VIDEO)
    }

    private fun chooseVideoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "video/*"
        startActivityForResult(galleryIntent,GALLERY_V)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,CAMERA)
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent,GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e("ActivityResult", "requestCode : " + requestCode + " ,resultCode : " + resultCode + " ,data : " + data);
        if (requestCode == WholesalerOfferActivity.REQUEST_THANKS && resultCode == AppCompatActivity.RESULT_CANCELED) {
            activity.setResult(Activity.RESULT_OK, activity.intent)
            activity.finish()
        } else {
            if (resultCode != 0)
                if (requestCode ==GALLERY) {
                    if (data != null) {
                        val contentURI = data!!.data
                        try {
                            val bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, contentURI)

                            if (imgPOS == 1) {
                                img_f_C!!.setImageBitmap(bitmap)
                                  imagePath1 = saveImage(bitmap)
                               //imagePath1 = getPath1(contentURI)
                            } else if (imgPOS == 2) {
                                img_s_C!!.setImageBitmap(bitmap)
                                imagePath2 = saveImage(bitmap)
                            } else {
                                img_t_C!!.setImageBitmap(bitmap)
                               imagePath3 = saveImage(bitmap)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                        }

                    }

                } else if (requestCode == CAMERA) {
                    val thumbnail = data!!.extras!!.get("data") as Bitmap
                    if (imgPOS == 1) {
                        img_f_C!!.setImageBitmap(thumbnail)
                       imagePath1 = saveImage(thumbnail)
                    } else if (imgPOS == 2) {
                        img_s_C!!.setImageBitmap(thumbnail)
                        imagePath2 = saveImage(thumbnail)
                    } else {
                        img_t_C!!.setImageBitmap(thumbnail)
                       imagePath3 = saveImage(thumbnail)
                    }
                } else if (requestCode == VIDEO) {
                    try {
                        try2CreateCompressDir()
                        videoPath = (Environment.getExternalStorageDirectory().toString()
                                + File.separator
                                + APP_DIR
                                + COMPRESSED_VIDEOS_DIR
                                + "VIDEO_1" + ".mp4")
                        videoPathBeforeComp = videoToUploadUri!!.getPath()
                        VideoCompressor().execute(videoToUploadUri!!.getPath(), videoPath)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else if (requestCode == GALLERY_V) {
                    val selectedMediaUri = data!!.getData()
                    Log.e("ppp", "imagePath: $selectedMediaUri")
                    val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)

                    // Get the cursor
                    val cursor = activity.getContentResolver().query(data.data,
                            filePathColumn, null, null, null)
                    Log.e("FILEDATA ", "*** " + cursor!!)
                    if (cursor != null) {
                        // Move to first row
                        cursor!!.moveToFirst()
                        //============================
                        val columnIndex = cursor!!.getColumnIndex(filePathColumn[0])
                        Log.e("VIDEO_path", cursor!!.getString(columnIndex) + "-------")

                        try2CreateCompressDir()
                       videoPath = (Environment.getExternalStorageDirectory().toString()
                                + File.separator
                                + APP_DIR
                                +COMPRESSED_VIDEOS_DIR
                                + "VIDEO_1" + ".mp4")

                        videoPathBeforeComp = cursor!!.getString(columnIndex)
                        Log.e("result", videoPathBeforeComp + "")
                        VideoCompressor().execute(videoPathBeforeComp, videoPath)

                    } else {
                        Toast.makeText(activity, "Please choose valid Image or Video", Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }

    private fun saveImage(myBitmap: Bitmap?): String {

        val bytes = ByteArrayOutputStream()
        myBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + "/demonuts")
        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(activity,
                    arrayOf(f.getPath()),
                    arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    fun checkAndRequestPermissions(context: Activity): Boolean {
        val ExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        val cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
        var listPermissionsNeeded: ArrayList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (ExtstorePermission !== PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                    .toArray(arrayOfNulls<String>(listPermissionsNeeded.size)),
                    11)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            11 -> if ((ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(activity,
                        "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                        .show()
                activity.finish()
            } else if ((ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(activity,
                        "FlagUp Requires Access to Your Storage.",
                        Toast.LENGTH_SHORT).show()
                activity.finish()
            } else {
                showPictureDialog()
            }
        }
    }

    inner class VideoCompressor : AsyncTask<String, Void, Boolean>() {

        override fun onPreExecute() {
            super.onPreExecute()
            UiHelpers.showProgessBar(activity.window, rlProgressBarSubmitProposal)
        }

        override fun doInBackground(vararg params: String?): Boolean {
            return MediaController.getInstance().convertVideo(params[0], params[1])
        }

        override fun onPostExecute(compressed: Boolean?) {
            super.onPostExecute(compressed)

            if (compressed!!) {
                UiHelpers.hideProgessBar(activity.window, rlProgressBarSubmitProposal)
                Log.e("video", "Compression successfully!")
                val bitmap = ThumbnailUtils.createVideoThumbnail(videoPathBeforeComp, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND)

                vid_f_C.setImageBitmap(bitmap)

                if (bitmap != null)
                    thumbFile = bitmapToFile(activity,bitmap)

            } else {
                UiHelpers.hideProgessBar(activity.window, rlProgressBarSubmitProposal)
            }
        }
    }

}