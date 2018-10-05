package app.horecafy.com.services

import android.content.Context
import android.util.Log
import app.horecafy.com.R
import app.horecafy.com.models.Demand
import app.horecafy.com.services.responses.DemandResponse
import app.horecafy.com.util.Constants
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson


object DemandService {

    private val CUSTOMER_ENDPOINT = "customer"
    private val WHOLESALER_ENDPOINT = "wholesaler"
    private val DEMAND_ENDPOINT = "demand"

    fun get(context: Context, id: Long, complete: (Boolean, demand: Demand?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${DEMAND_ENDPOINT}/${id}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, DemandResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
            } else {
                Log.d(Constants.TAG, "Could not get the demand: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the demand: $error")
            complete(false, null, "Error desconocido")
        }) { }

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun getByCustomer(context: Context, customerId: Long, categoryId: Int, complete: (Boolean, demand: List<Demand>, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CUSTOMER_ENDPOINT}/${customerId}/demands?categoryId=${categoryId}&sentTo=0&borrado=0"
        Log.e("DEMAND","REQ DATA : : "+url)
        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, DemandResponse::class.java)
            Log.e("DEMAND","RES DATA JSON: : "+response.toString())
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data, null)
            } else {
                Log.d(Constants.TAG, "Could not get the demands: $response.error")
                complete(false, mutableListOf(), response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the demands: $error")
            complete(false, mutableListOf(), "Error desconocido")
        }) { }

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun getByCustomerWithOffers(context: Context, customerId: Long, categoryId: Int, complete: (Boolean, demand: List<Demand>, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CUSTOMER_ENDPOINT}/${customerId}/demandsWithOffers?categoryId=${categoryId}&borrado=0&sentTo=1"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, DemandResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data, null)
            } else {
                Log.d(Constants.TAG, "Could not get the demands: $response.error")
                complete(false, mutableListOf(), response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the demands: $error")
            complete(false, mutableListOf(), "Error desconocido")
        }) { }

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun getByWholesaler(context: Context, wholesalerId: Long, categoryId: Int, complete: (Boolean, demand: List<Demand>, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${WHOLESALER_ENDPOINT}/${wholesalerId}/demands?categoryId=${categoryId}&sentTo=0&borrado=0"
        Log.e("REQ","Get by Wholesaler : "+url)
        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, DemandResponse::class.java)
            Log.e("RES","Get by Wholesaler : "+json.toString())
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data, null)
            } else {
                Log.d(Constants.TAG, "Could not get the demands: $response.error")
                complete(false, mutableListOf(), response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the demands: $error")
            complete(false, mutableListOf(), "Error desconocido")
        }) { }

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun create(context: Context, demand: Demand, complete: (Boolean, demand: Demand?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${DEMAND_ENDPOINT}"
        val data = Gson().toJson(demand)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            val response = Gson().fromJson(json, DemandResponse::class.java)
            Log.e("DEMAND","RES AAAA: : "+response)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
            } else {
                Log.d(Constants.TAG,    "Could not create the demand: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create the demand: $error")
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

    fun update(context: Context, demand: Demand, complete: (Boolean, demand: Demand?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${DEMAND_ENDPOINT}"
        val data = Gson().toJson(demand)

        val createRequest = object : StringRequest(Request.Method.PUT, url, Response.Listener { json ->
            val response = Gson().fromJson(json, DemandResponse::class.java)
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

    fun delete(context: Context, id: Long, complete: (Boolean, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${DEMAND_ENDPOINT}/${id}"

        val createRequest = object : StringRequest(Request.Method.DELETE, url, Response.Listener { json ->
            val response = Gson().fromJson(json, DemandResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, null)
            } else {
                Log.d(Constants.TAG, "Could not delete the demand: $response.error")
                complete(false, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not delete the demand: $error")
            complete(false, "Error desconocido")
        }) { }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun share(context: Context, id: String, complete: (Boolean, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${DEMAND_ENDPOINT}/share/${id}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, DemandResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, null)
            } else {
                Log.d(Constants.TAG, "Could not share the demand: $response.error")
                complete(false, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not share the demand: $error")
            complete(false, "Error desconocido")
        }) { }

        Volley.newRequestQueue(context).add(createRequest)
    }
}