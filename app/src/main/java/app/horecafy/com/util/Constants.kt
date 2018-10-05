package app.horecafy.com.util

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.util.Log
import app.horecafy.com.R.id.editProvince
import app.horecafy.com.Retrofit.APIService
import app.horecafy.com.Retrofit.ApiClient
import app.horecafy.com.activities.wholesalers.WholesalerOfferActivity
import kotlinx.android.synthetic.main.activity_customer_register.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat


class Constants {

    companion object {
        val TAG = "HORECAFY"

        val JSON_CONTENT_TYPE = "application/json; charset=utf-8"

        val REMEMBER = "REMEMBER"
        val TYPEUSER_KEY = "TYPEUSER"
        val USERNAME_KEY = "USERNAME"
        val PASSWORD_KEY = "PASSWORD"

        val CONSTANT_CREATE_LIST_FIRST_ELEMENT = "Lista personalizada"

        val CONSTANT_EMPTY_STRING = ""
        val CONSTANT_SELECT_DISTRIBUTOR = "Seleccionar Distribuidor"
        val CONSTANT_SELECT_THE_TYPE_OF_RESTAURANTS = "Seleccione el tipo de restaurantes"
        val SOMETHING_WENT_WRONG = "Algo salió mal. Inténtalo de nuevo."
        val NO_DATA_AVAILABLE = "Datos no disponibles."
        val NO_INTERNET_CONNECTION_AVAILABLE = "No se ha podido establecer conexión. Por favor, intentelo de nuevo."

        val MONDAY = "Lun"
        val TUESDAY = "Mar"
        val WEDNESDAY = "Mier" //mier //Mie
        val THURSDAY = "Jue"
        val FRIDAY = "Vier" //vier  //Vie
        val SATURDAY = "Sab"
        val SUNDAY = "Dom" //Dom

        val SLOT_ONE = "00:00 - 02:00"
        val SLOT_TWO = "02:00 - 04:00"
        val SLOT_THREE = "04:00 - 06:00"
        val SLOT_FOUR = "06:00 - 08:00"
        val SLOT_FIVE = "08:00 - 10:00"
        val SLOT_SIX = "10:00 - 12:00"
        val SLOT_SEVEN = "12:00 - 14:00"
        val SLOT_EIGHT = "14:00 - 16:00"
        val SLOT_NINE = "16:00 - 18:00"
        val SLOT_TEN = "18:00 - 20:00"
        val SLOT_ELEVEN = "20:00 - 22:00"
        val SLOT_TWELVE = "22:00 - 00:00"

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        fun FlipDateFormat(inputDate: String):String{
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = inputFormat.parse(inputDate)
            val outputDateStr = outputFormat.format(date)
            return outputDateStr
        }

        fun FlipDateFormatinvers(inputDate: String):String{
            val inputFormat = SimpleDateFormat("dd-MM-yyyy")
            val outputFormat= SimpleDateFormat("yyyy-MM-dd")
            val date = inputFormat.parse(inputDate)
            val outputDateStr = outputFormat.format(date)
            return outputDateStr
        }

        fun try2CreateCompressDir() {
            var f = File(Environment.getExternalStorageDirectory(), File.separator + WholesalerOfferActivity.APP_DIR)
            f.mkdirs()
            f = File(Environment.getExternalStorageDirectory(), File.separator + WholesalerOfferActivity.APP_DIR + WholesalerOfferActivity.COMPRESSED_VIDEOS_DIR)
            f.mkdirs()
            f = File(Environment.getExternalStorageDirectory(), File.separator + WholesalerOfferActivity.APP_DIR + WholesalerOfferActivity.TEMP_DIR)
            f.mkdirs()
        }

        fun bitmapToFile(context: Context,bmp: Bitmap): File? {
            try {
                val REQUIRED_SIZE = 200
                val bos = ByteArrayOutputStream(REQUIRED_SIZE)
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bos)
                val bArr = bos.toByteArray()
                bos.flush()
                bos.close()
                val timestemp = System.currentTimeMillis() / 100
                val file_name = "image_" + timestemp.toString() + ".png"
                val fos = context.openFileOutput(file_name, Context.MODE_PRIVATE)
                fos.write(bArr)
                fos.flush()
                fos.close()
                val mFile = File(context.getFilesDir().getAbsolutePath(), file_name)
                return mFile
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return null
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }
        fun ShowAlertDialog(context: Context) {
            lateinit var dialog:AlertDialog
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error de red")
            builder.setMessage("No podemos obtener una lista de provincias debido a un error de red.")
            val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->  dialog.dismiss()
                }
            }
            builder.setPositiveButton("CANCELAR",dialogClickListener)
            dialog = builder.create()
            dialog.show()
        }
        fun GetProvincia(cotext: Context): ArrayList<String> {
            var Provincia: ArrayList<String> = ArrayList()
            val apiService = ApiClient.getClient(cotext as Activity?).create(APIService::class.java)
            val call = apiService.getProvincia()
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        try {
                            var res: String? = null
                            try {
                                res = response.body().string()
                                Log.e("res ", "Add CAT : " + res!!)
                                val jsonObject = JSONObject(res)
                                val jsonArray = jsonObject.optJSONArray("data")
                                if (jsonArray.length()>0){
                                    for (i in 0..(jsonArray.length() - 1)) {
                                        val provinceName = jsonArray.getJSONObject(i)
                                        Provincia.add(provinceName.getString("province"))

                                    }
                                }
                                Log.e("LLLLLLLLL","Provincia Name Array: "+Provincia)

                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else {
                        Log.e("res ", "Add CAT ERROR: ")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("CCC", "Failure: $call")
                }
            })

            return Provincia
        }
    }
}