package app.horecafy.com.activities.wholesalers

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.Toast
import app.horecafy.com.MyApplication
import app.horecafy.com.R
import app.horecafy.com.Retrofit.APIService
import app.horecafy.com.Retrofit.ApiClient
import app.horecafy.com.models.Category
import app.horecafy.com.models.Demand
import app.horecafy.com.models.Offer
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.OfferService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.Constants.Companion.bitmapToFile
import app.horecafy.com.util.Constants.Companion.try2CreateCompressDir
import app.horecafy.com.util.UiHelpers
import com.android.volley.*
import com.googlecode.mp4parser.util.Path.getPath
import com.squareup.picasso.Picasso
import com.yovenny.videocompress.MediaController
import kotlinx.android.synthetic.main.activity_wholesaler_offer.*
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

class WholesalerOfferActivity : AppCompatActivity() {

    companion object {
        val INTENT_CATEGORY = "INTENT_REVIEW_OFFERS_LIST"
        val INTENT_DEMAND = "INTENT_DEMAND"
        val REQUEST_THANKS = 1
        private val GALLERY = 111
        private val GALLERY_V = 444
        private val CAMERA = 222
        private val VIDEO = 333
        private var imgPOS = 1
        val APP_DIR = "VideoCompressor"
        val COMPRESSED_VIDEOS_DIR = "/Compressed Videos/"
        val TEMP_DIR = "/Temp/"
        private var videoToUploadUri: Uri? = null
        private var videoToUploadUri_comp: String? = null
        private var videoPathBeforeComp = ""
        private var videoPath = ""
        private var imagePath1 = ""
        private var imagePath2 = ""
        private var imagePath3 = ""
        internal var thumbFile: File? = null
        fun intent(context: Context, category: Category, demand: Demand): Intent {
            val intent = Intent(context, WholesalerOfferActivity::class.java)
            intent.putExtra(INTENT_CATEGORY, category);
            intent.putExtra(INTENT_DEMAND, demand);
            return intent
        }
    }

    var category: Category? = null
    var demand: Demand? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wholesaler_offer)
        // Get category and demand from intent and load it from API
        category = intent.getSerializableExtra(INTENT_CATEGORY) as Category
        demand = intent.getSerializableExtra(INTENT_DEMAND) as Demand

        // Bind useful data
        val uri = "${this.getString(R.string.IMAGE_URL)}${category!!.image}"
        Picasso.with(this).load(uri).into(imageCategory)
        textCategory.text = category!!.name

        textHiddenId.text = demand!!.hiddenId.toString()
        textBrand.text = demand!!.brand
        textFormat.text = demand!!.format
        if (demand!!.quantyPerMonth != null) {
            textQuantyPerMonth.text = demand!!.quantyPerMonth.toString()
        }
        if (demand!!.targetPrice != null) {
            textTargetPrice.text = demand!!.targetPrice.toString()
        }
        textComments.text = demand!!.comments

        img_f!!.setOnClickListener(View.OnClickListener {
            imgPOS = 1
            if (checkAndRequestPermissions(this)) {
                showPictureDialog()
            }
        })

        img_s!!.setOnClickListener(View.OnClickListener {
            imgPOS = 2
            if (checkAndRequestPermissions(this)) {
                showPictureDialog()
            }
        })

        img_t!!.setOnClickListener(View.OnClickListener {
            imgPOS = 3
            if (checkAndRequestPermissions(this)) {
                showPictureDialog()
            }

        })

        vid_f!!.setOnClickListener(View.OnClickListener {
            imgPOS = 4
            if (checkAndRequestPermissions(this)) {
                showPictureDialog()
            }

        })

        buttonSave.setOnClickListener(View.OnClickListener {

            if (isValid()) {
                UiHelpers.showProgessBar(window, progressBar)
                val brand = editBrand.text.toString()
                val format = editFormat.text.toString()
                val offerPrice = editOfferPrice.text.toString()
                val comments = editComments.text.toString()
                val offer = Offer(customerId = demand!!.customerId,
                        demandId = demand!!.hiddenId,
                        wholesalerId = AuthService.wholesaler?.hiddenId!!,
                        quantyPerMonth = demand!!.quantyPerMonth ?: 0,
                        typeOfFormatId = demand!!.typeOfFormatId,
                        offerPrice = offerPrice.toDouble(),
                        brand = brand,
                        fomat = format,
                        comments = comments)
                OfferService.create(this, offer) { status, data, error ->
                    UiHelpers.hideProgessBar(window, progressBar)
                    if (status) {
                        Log.d(Constants.TAG, "offer After : : "+ data!!.id)
                        if (!imagePath1.equals("") || !imagePath2.equals("") || !imagePath3.equals("") || !videoPath.equals("")) {
                           Log.e("PATH","11111111111: "+ imagePath1)
                           Log.e("PATH","22222222222: "+ imagePath2)
                           Log.e("PATH","33333333333: "+ imagePath3)
                           Log.e("PATH","44444444444: "+ videoPath)
                            UploadImageVideo(data!!.id)
                        } else {
                            val intent = WholesalerMakeOfferThanks.intent(this@WholesalerOfferActivity)
                            startActivityForResult(intent, REQUEST_THANKS)
                        }
                    } else {
                        Toast.makeText(this, "Error desconocido", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun UploadImageVideo(offerid: String?) {
        UiHelpers.showProgessBar(window, progressBar)
        var bodyvideo: MultipartBody.Part? = null
        val partsList = ArrayList<MultipartBody.Part>()
        val imageArray = ArrayList<String>()
        if (!imagePath1.equals("")){
            imageArray.add(imagePath1)
        }
        if (!imagePath2.equals("")){
            imageArray.add(imagePath2)
        }
        if (!imagePath3.equals("")){
            imageArray.add(imagePath3)
        }
       /* if(imagePath1!=null) {
            val file = File(imagePath1)
            val reqFile = RequestBody.create(MediaType.parse("image*//*"), file)
            bodyimage = MultipartBody.Part.createFormData("images", file.getName(), reqFile)
        }*/
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
        }
        Log.e("AAAAAAAAAAAAAA","offerid: "+offerid)
        Log.e("ADDDDD", "22  : : NOT ZERO : "+imageArray.size)
        val apiService = ApiClient.getClient(this).create(APIService::class.java)

        //val call = apiService.UpdateProfile(offerid,bodyimage,bodyvideo)
        val call = apiService.UploadArray(offerid,partsList,bodyvideo)

        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                UiHelpers.hideProgessBar(window, progressBar)
                if (response.isSuccessful) {
                    try {
                        var res: String? = null
                        try {
                            res = response.body().string()
                            Log.e("res ", "Image Upload : " + res!!)
                            videoPath = ""
                            imagePath1 = ""
                            imagePath2 = ""
                            imagePath3 = ""
                            val intent = WholesalerMakeOfferThanks.intent(this@WholesalerOfferActivity)
                            startActivityForResult(intent, REQUEST_THANKS)
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
                UiHelpers.hideProgessBar(window, progressBar)
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
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
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
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplication().getApplicationContext(),
                "app.horecafy.com",
                f))
        videoToUploadUri = Uri.fromFile(f)
        videoToUploadUri_comp = f1.toString()
        chooserIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 90)
        startActivityForResult(chooserIntent, VIDEO)
    }

    private fun chooseVideoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "video/*"
        startActivityForResult(galleryIntent, GALLERY_V)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun isValid(): Boolean {
        var isValid = true

        val brand = editBrand.text.toString()
        val format = editFormat.text.toString()
        val offerPrice = editOfferPrice.text.toString()

        if (brand.isNullOrEmpty()) {
            editBrand.setError("Este campo es obligatorio")
            isValid = false
        }

        /*if (format.isNullOrEmpty()) {
            editFormat.setError("Este campo es obligatorio")
            isValid = false
        }*/

        if (offerPrice.isNullOrEmpty()) {
            editOfferPrice.setError("Este campo es obligatorio")
            isValid = false
        }

        return isValid
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e("ActivityResult", "requestCode : " + requestCode + " ,resultCode : " + resultCode + " ,data : " + data);
        if (requestCode == REQUEST_THANKS && resultCode == AppCompatActivity.RESULT_CANCELED) {
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            if (resultCode != 0)
                if (requestCode == GALLERY) {
                    if (data != null) {
                        val contentURI = data!!.data
                        try {
                            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)

                            if (imgPOS == 1) {
                                img_f!!.setImageBitmap(bitmap)
                                imagePath1 = saveImage(bitmap)
                              //  imagePath1 = getPath1(contentURI)
                            } else if (imgPOS == 2) {
                                img_s!!.setImageBitmap(bitmap)
                                imagePath2 = saveImage(bitmap)
                            } else {
                                img_t!!.setImageBitmap(bitmap)
                                imagePath3 = saveImage(bitmap)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(this@WholesalerOfferActivity, "Failed!", Toast.LENGTH_SHORT).show()
                        }

                    }

                } else if (requestCode == CAMERA) {
                    val thumbnail = data!!.extras!!.get("data") as Bitmap
                    if (imgPOS == 1) {
                        img_f!!.setImageBitmap(thumbnail)
                        imagePath1 = saveImage(thumbnail)
                    } else if (imgPOS == 2) {
                        img_s!!.setImageBitmap(thumbnail)
                        imagePath2 = saveImage(thumbnail)
                    } else {
                        img_t!!.setImageBitmap(thumbnail)
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
                    val cursor = getContentResolver().query(data.data,
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
                                + COMPRESSED_VIDEOS_DIR
                                + "VIDEO_1" + ".mp4")

                        videoPathBeforeComp = cursor!!.getString(columnIndex)
                        Log.e("result", videoPathBeforeComp + "")
                        VideoCompressor().execute(videoPathBeforeComp, videoPath)

                    } else {
                        Toast.makeText(applicationContext, "Please choose valid Image or Video", Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }

    inner class VideoCompressor : AsyncTask<String, Void, Boolean>() {

        override fun onPreExecute() {
            super.onPreExecute()
            UiHelpers.showProgessBar(window, progressBar)
        }

        override fun doInBackground(vararg params: String?): Boolean {
            return MediaController.getInstance().convertVideo(params[0], params[1])
        }

        override fun onPostExecute(compressed: Boolean?) {
            super.onPostExecute(compressed)

            if (compressed!!) {
                UiHelpers.hideProgessBar(window, progressBar)
                Log.e("video", "Compression successfully!")
                val bitmap = ThumbnailUtils.createVideoThumbnail(videoPathBeforeComp, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND)

                vid_f.setImageBitmap(bitmap)

                if (bitmap != null)
                    thumbFile = bitmapToFile(this@WholesalerOfferActivity,bitmap)

            } else {
                UiHelpers.hideProgessBar(window, progressBar)
            }
        }
    }

   /* fun bitmapToFile(context: Context,bmp: Bitmap): File? {
        try {
            val REQUIRED_SIZE = 200
            val bos = ByteArrayOutputStream(REQUIRED_SIZE)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val bArr = bos.toByteArray()
            bos.flush()
            bos.close()
            val timestemp = System.currentTimeMillis() / 100
            val file_name = "image_" + timestemp.toString() + ".png"
            val fos = openFileOutput(file_name, Context.MODE_PRIVATE)
            fos.write(bArr)
            fos.flush()
            fos.close()
            val mFile = File(getFilesDir().getAbsolutePath(), file_name)
            return mFile
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }*/

  /*  private fun try2CreateCompressDir() {
        var f = File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR)
        f.mkdirs()
        f = File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + COMPRESSED_VIDEOS_DIR)
        f.mkdirs()
        f = File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + TEMP_DIR)
        f.mkdirs()
    }*/

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
            MediaScannerConnection.scanFile(this,
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
            11 -> if ((ContextCompat.checkSelfPermission(this@WholesalerOfferActivity,
                            Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(),
                        "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                        .show()
                finish()
            } else if ((ContextCompat.checkSelfPermission(this@WholesalerOfferActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(),
                        "FlagUp Requires Access to Your Storage.",
                        Toast.LENGTH_SHORT).show()
                finish()
            } else {
                showPictureDialog()
            }
        }
    }

}
