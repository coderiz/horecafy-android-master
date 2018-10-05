package app.horecafy.com.services

import android.content.Context
import android.util.Log
import app.horecafy.com.R
import app.horecafy.com.models.WholesalerList
import app.horecafy.com.services.responses.WholesalerListResponse
import app.horecafy.com.util.Constants
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import android.R.attr.password




object WholesalerListService {

    private val WHOLESALER_ENDPOINT = "wholesaler"
    private val WHOLESALERLIST_ENDPOINT = "wholesaler-List"
    private val WHOLESALERLIST_ENDPOINT_CAT = "wholesaler-list/category"

    fun get(context: Context, id: Int, complete: (Boolean, wholesalerList: WholesalerList?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${WHOLESALERLIST_ENDPOINT}/${id}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, WholesalerListResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
            } else {
                Log.d(Constants.TAG, "Could not get the demand: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the demand: $error")
            complete(false, null, "Error desconocido")
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun get(context: Context, wholesalerId: Long, categoryId: Int, complete: (Boolean, wholesalerList: List<WholesalerList>, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${WHOLESALER_ENDPOINT}/${wholesalerId}/lists?categoryId=${categoryId}&borrado=0"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, WholesalerListResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data, null)
            } else {
                Log.d(Constants.TAG, "Could not get the demands: $response.error")
                complete(false, mutableListOf(), response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the demands: $error")
            complete(false, mutableListOf(), "Error desconocido")
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun create(context: Context, wholesalerList: WholesalerList, complete: (Boolean, wholesalerList: WholesalerList?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${WHOLESALERLIST_ENDPOINT}"
        val data = Gson().toJson(wholesalerList)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            val response = Gson().fromJson(json, WholesalerListResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
            } else {
                Log.d(Constants.TAG, "Could not create the wholesaler mNotificationsList: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create the wholesaler mNotificationsList: $error")
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

    fun Addcategory(context: Context, category: String, id: Long,complete: (Boolean,  error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${WHOLESALERLIST_ENDPOINT_CAT}"
       // val data = Gson().toJson(wholesalerList)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            Log.e("CATTTTTTTT","Res: "+json.toString())
            val response = Gson().fromJson(json, WholesalerListResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true,  null)
            } else {
                Log.d(Constants.TAG, "Could not create the wholesaler mNotificationsList: $response.error")
                complete(false,  response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create the wholesaler mNotificationsList: $error")
            complete(false,  "Error desconocido")
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["wholesalerId"] = id.toString()
                params["categoryIds"] = category
                return params
            }
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun update(context: Context, wholesalerList: WholesalerList, complete: (Boolean, wholesalerList: WholesalerList?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${WHOLESALERLIST_ENDPOINT}"
        val data = Gson().toJson(wholesalerList)

        val createRequest = object : StringRequest(Request.Method.PUT, url, Response.Listener { json ->
            val response = Gson().fromJson(json, WholesalerListResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
            } else {
                Log.d(Constants.TAG, "Could not update the demand: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not update the demand: $error")
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

    fun delete(context: Context, id: Int, complete: (Boolean, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${WHOLESALERLIST_ENDPOINT}/${id}"

        val createRequest = object : StringRequest(Request.Method.DELETE, url, Response.Listener { json ->
            val response = Gson().fromJson(json, WholesalerListResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, null)
            } else {
                Log.d(Constants.TAG, "Could not delete the wholesaler mNotificationsList: $response.error")
                complete(false, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not delete the wholesaler mNotificationsList: $error")
            complete(false, "Error desconocido")
        }) {}

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }
}