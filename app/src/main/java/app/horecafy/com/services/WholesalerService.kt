package app.horecafy.com.services

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import app.horecafy.com.R
import app.horecafy.com.Retrofit.ApiClient
import app.horecafy.com.activities.wholesalers.WholesalerMakeOfferDemandsActivity
import app.horecafy.com.models.*
import app.horecafy.com.services.responses.BusinessTypesListResponse
import app.horecafy.com.services.responses.CustomerAvailabilityResponse
import app.horecafy.com.services.responses.NotificationsResponse
import app.horecafy.com.services.responses.WholesalerCreateResponse
import app.horecafy.com.util.Constants
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject


object WholesalerService {

    val ENDPOINT = "wholesaler"
    val TYPE_BUSINESS_ENDPOINT = "type-business"
    val BUSINESS_VISIT_ENDPOINT = "businessvisit"
    val BUSINESS_DELETE_DEMAND = "demand/"
    val WHOLESALER_NOTIFICATIONS_ENDPOINT = "businessvisit/wholesaler/"
    val CUSTOMER_AVAILABILITY_ENDPOINT = "customer/"

    fun create(context: Context, wholesaler: Wholesaler, complete: (Boolean, wholesaler: Wholesaler?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${ENDPOINT}"
        val data = Gson().toJson(wholesaler)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            val response = Gson().fromJson(json, WholesalerCreateResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
            } else {
                Log.d(Constants.TAG, "Could not create the wholesaler: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create the wholesaler: $error")
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

    fun update(context: Context, wholesaler: Wholesaler, complete: (Boolean, wholesaler: Wholesaler?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${ENDPOINT}"
        val data = Gson().toJson(wholesaler)

        val createRequest = object : StringRequest(Request.Method.PUT, url, Response.Listener { json ->
            val response = Gson().fromJson(json, WholesalerCreateResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
            } else {
                Log.d(Constants.TAG, "Could not update the wholesaler: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not update the wholesaler: $error")
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

    fun getBusinessTypeList(context: Context, complete: (Boolean, restaurantsTypeList: List<RestaurantsTypes>, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${WholesalerService.TYPE_BUSINESS_ENDPOINT}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, BusinessTypesListResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data, null)
            } else {
                Log.d(Constants.TAG, "Could not get the business types mNotificationsList: $response.error")
                complete(false, mutableListOf(), response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the business types mNotificationsList: $error")
            complete(false, mutableListOf(), "Error desconocido")
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun submitProposal(context: Context, proposalItems: ProposalItems, complete: (Boolean, error: String?, groupid: String ) -> Unit) {
        var GID =""
        val url = "${context.getString(R.string.API_URL)}${WholesalerService.BUSINESS_VISIT_ENDPOINT}"
        val inputStringData = Gson().toJson(proposalItems)
        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            Log.e("AAAAAAAAAASUBMIT", "RES proposalItems:" +json.toString())
            GID = ApiClient.getGroupID(json);
            complete(true, null, GID)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create proposalItems: $error")
            complete(false, "Error desconocido",GID)
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }

            override fun getBody(): ByteArray {
                return inputStringData.toByteArray()
            }
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun getWholesalerNotifications(context: Context, wholesalerId: String, complete: (Boolean, data: List<NotificationsDetails>, error: String?) -> Unit) {

        val url = "${context.getString(R.string.API_URL)}${WholesalerService.WHOLESALER_NOTIFICATIONS_ENDPOINT}${wholesalerId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, NotificationsResponse::class.java)
            Log.e("BBBBBBBBBB","Wholesalers NOTI RES : :"+json.toString())
            if (response.data != null) {
                complete(true, response.data, null)
            } else {
                val data: MutableList<NotificationsDetails> = mutableListOf()
                complete(true, data, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create proposalItems: $error")
            val data: MutableList<NotificationsDetails> = mutableListOf()
            complete(false, data, "Error desconocido")
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }

            /*override fun getBody(): ByteArray {
                return inputStringData.toByteArray()
            }*/
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun getCustomerAvailability(context: Context, customerId: String, complete: (Boolean, data: CustomerAvailability, error: String?) -> Unit) {

        val url = "${context.getString(R.string.API_URL)}${WholesalerService.CUSTOMER_AVAILABILITY_ENDPOINT}" +
                "${customerId}${"/availability"}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, CustomerAvailabilityResponse::class.java)
            if (response.data != null) {
                complete(true, response.data, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create proposalItems: $error")
            var data = CustomerAvailability()
            complete(false, data, "Error desconocido")
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }

            /*override fun getBody(): ByteArray {
                return inputStringData.toByteArray()
            }*/
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun selectCustomerAvailabilityTimeslot(context: Context, notificationId: String, timeSlot: TimeSlotItems, complete: (Boolean, error: String?) -> Unit) {

        val url = "${context.getString(R.string.API_URL)}${WholesalerService.BUSINESS_VISIT_ENDPOINT}" + "/" +
                "${notificationId}"

        val inputStringData = Gson().toJson(timeSlot)
            Log.e("REQ","Notification : : "+inputStringData.toString())
        val createRequest = object : StringRequest(Request.Method.PUT, url, Response.Listener { json ->
            //            val response = Gson().fromJson(json, CustomerAvailabilityResponse::class.java)
//            if (response.data != null) {
            Log.e("RES","Notification : : "+json.toString())
            complete(true, null)
//            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create timeSlot: $error")
            var data = CustomerAvailability()
            complete(false, "Error desconocido")
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }

            override fun getBody(): ByteArray {
                return inputStringData.toByteArray()
            }
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun DeleteDemand(context: Context,wholesalerId: Long,DemandId: String, complete: (Boolean, error: String? ) -> Unit) {
        var GID =""
        val url = "${context.getString(R.string.API_URL)}${WholesalerService.BUSINESS_DELETE_DEMAND}"+DemandId
        Log.e("REQ","delete : "+url + " WID : "+wholesalerId+" DId : "+DemandId)
        val createRequest = object : StringRequest(Request.Method.DELETE, url, Response.Listener { json ->
            Log.e("RES", "delete:" +json.toString())
            complete(true, null)
        }, Response.ErrorListener { error ->5
            Log.d(Constants.TAG, "Could not create proposalItems: $error")
            complete(false, "Error desconocido")
        }) {
           /* override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }*/
            /*override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["wholesalerId"] = wholesalerId.toString()
                params["demandId"] = DemandId
                return params
            }*/
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }
}