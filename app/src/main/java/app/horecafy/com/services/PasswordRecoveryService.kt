package app.horecafy.com.services

import android.content.Context
import android.util.Log
import app.horecafy.com.R
import app.horecafy.com.models.ForgotPassword
import app.horecafy.com.models.Password
import app.horecafy.com.models.ResetPassword
import app.horecafy.com.services.responses.ForgotPasswordResponse
import app.horecafy.com.util.Constants
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson


object PasswordRecoveryService {

    private val PASSWORD_RESET_ENDPOINT = "password-reset"

    fun forgotPassword(context: Context, forgotPassword: ForgotPassword,
                       complete: (Boolean, password: Password?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${PASSWORD_RESET_ENDPOINT}"
        val data = Gson().toJson(forgotPassword)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            val response = Gson().fromJson(json, ForgotPasswordResponse::class.java)

            if (response.data.size > 0) {
                complete(true, response.data[0], null)
            } else {
                complete(false, null, "Datos de acceso incorrectos. Por favor, corríjalos y vuelva a intentarlo")
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create forgotPassword: $error")
            complete(false, null, Constants.SOMETHING_WENT_WRONG)
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

    fun resetPassword(context: Context, resetPassword: ResetPassword,
                      complete: (Boolean, password: Password?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${PASSWORD_RESET_ENDPOINT}"
        val data = Gson().toJson(resetPassword)

        val createRequest = object : StringRequest(Request.Method.PUT, url, Response.Listener { json ->
            val response = Gson().fromJson(json, ForgotPasswordResponse::class.java)
            if (response.data != null) {
                complete(true, response.data[0], null)
            } else {
                complete(false, null, "Error desconocido")
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create ResetPassword: $error")
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
}
