package app.horecafy.com.services

import android.content.Context
import android.util.Log
import app.horecafy.com.R
import app.horecafy.com.models.Offer
import app.horecafy.com.services.responses.OfferResponse
import app.horecafy.com.util.Constants
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson


object OfferService {

    private val CUSTOMER_ENDPOINT = "customer"
    private val OFFER_ENDPOINT = "offer"

    fun get(context: Context, offerId: Long, complete: (Boolean, offer: Offer?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${OFFER_ENDPOINT}/${offerId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, OfferResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
            } else {
                Log.d(Constants.TAG, "Could not get the offer: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the offer: $error")
            complete(false, null, "Error desconocido")
        }) { }

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun getByCategory(context: Context, customerId: Long, categoryId: Int, complete: (Boolean, offers: List<Offer>, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CUSTOMER_ENDPOINT}/${customerId}/offers?categoryId=${categoryId}&borrado=0"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, OfferResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data, null)
            } else {
                Log.d(Constants.TAG, "Could not get the offers: $response.error")
                complete(false, mutableListOf(), response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the offers: $error")
            complete(false, mutableListOf(), "Error desconocido")
        }) { }

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun getByDemandId(context: Context, customerId: Long, demandId: Long, complete: (Boolean, offers: List<Offer>, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CUSTOMER_ENDPOINT}/${customerId}/offers?demandId=${demandId}&borrado=0&approvedByCustomer=0"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, OfferResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data, null)
            } else {
                Log.d(Constants.TAG, "Could not get the offers: $response.error")
                complete(false, mutableListOf(), response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the offers: $error")
            complete(false, mutableListOf(), "Error desconocido")
        }) { }

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun create(context: Context, offer: Offer, complete: (Boolean, offer: Offer?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${OFFER_ENDPOINT}"
        val data = Gson().toJson(offer)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            val response = Gson().fromJson(json, OfferResponse::class.java)
            Log.d(Constants.TAG, "offer: : "+ response.toString())
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
                Log.d(Constants.TAG, "offer: : "+ response.toString())
            } else {
                Log.d(Constants.TAG, "Could not create the offer: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create the offer: $error")
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

    fun accept(context: Context, offerId: Long, complete: (Boolean, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${OFFER_ENDPOINT}/accept/${offerId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, OfferResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, null)
            } else {
                Log.e(Constants.TAG, "Could not accept the offer: $response.error")
                complete(false, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.e(Constants.TAG, "Could not accept the offer: $error")
            complete(false, "Error desconocido")
        }) { }

        Volley.newRequestQueue(context).add(createRequest)
    }
}