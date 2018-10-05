package app.horecafy.com.services

import android.content.Context
import android.util.Log
import app.horecafy.com.R
import app.horecafy.com.models.Customer
import app.horecafy.com.models.Login
import app.horecafy.com.models.TypeOfBusiness
import app.horecafy.com.models.Wholesaler
import app.horecafy.com.services.responses.CustomerLoginResponse
import app.horecafy.com.services.responses.WholesalerLoginResponse
import app.horecafy.com.util.Constants
import app.horecafy.com.util.SharedPreferencesHelper
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson


object AuthService {

    var customer: Customer? = null
    var wholesaler: Wholesaler? = null

    private val LOGIN_ENDPOINT = "login"

    fun loginAsCustomer(context: Context, login: Login, complete: (Boolean, customer: Customer?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${LOGIN_ENDPOINT}"
        val data = Gson().toJson(login)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            val response = Gson().fromJson(json, CustomerLoginResponse::class.java)
            complete(true, when (response.data.size){
                0 -> null
                else -> {
                    updatePreferences(context, login)
                    customer = response.data[0]
                    response.data[0]
                }
            }, null)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create login: $error")
            complete(false, null, "Error desconocido")
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }

            override fun getBody(): ByteArray {
                return data.toByteArray()
            }
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun loginAsWholesaler(context: Context, login: Login, complete: (Boolean, wholesaler: Wholesaler?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${LOGIN_ENDPOINT}"
        val data = Gson().toJson(login)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            val response = Gson().fromJson(json, WholesalerLoginResponse::class.java)
            complete(true, when (response.data.size){
                0 -> null
                else -> {
                    updatePreferences(context, login)
                    wholesaler = response.data[0]
                    response.data[0]
                }
            }, null)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create login: $error")
            complete(false, null, "Error desconocido")
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }

            override fun getBody(): ByteArray {
                return data.toByteArray()
            }
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun logout(context: Context) {
        val preferences = SharedPreferencesHelper()
        customer = null
        wholesaler = null
        preferences.setValue(context, Constants.REMEMBER, "false")
        updatePreferences(context, Login("", "", ""))
    }


    private fun updatePreferences(context: Context, login: Login){
        val preferences = SharedPreferencesHelper()
        preferences.setValue(context, Constants.TYPEUSER_KEY, login.typeUser)
        preferences.setValue(context, Constants.USERNAME_KEY, login.email)
        preferences.setValue(context, Constants.PASSWORD_KEY, login.password)
    }
}
