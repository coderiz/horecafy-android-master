package app.horecafy.com.services

import android.content.Context
import android.util.Log
import app.horecafy.com.R
import app.horecafy.com.models.*
import app.horecafy.com.services.responses.*
import app.horecafy.com.util.Constants
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject


object CustomerService {

    private val ENDPOINT = "customer"
    private val FREE_DEMAND_ENDPOINT = "freedemand"
    private val CUSTOMER_REQUEST_PRODUCT = "customer/requestProduct"
    private val WHOLESALERS_ENDPOINT = "wholesaler"
    private val MAKE_AN_ORDER_ENDPOINT = "order"
    private val INVITE_DISTRIBUTOR_ENDPOINT = "order/invite"
    private val CUSTOMERS_NOTIFICATIONS_ENDPOINT = "businessvisit/customer/"
    private val ACCEPT_NOTIFICATION_ENDPOINT = "businessvisit/accept/"
    private val REJECT_NOTIFICATION_ENDPOINT = "businessvisit/reject/"
    private val SUBMIT_CUSTOMR_AVAILABILITY_ENDPOINT = "customer/"
    private val REVIEW_OFFERS_ENDPOINT = "offer/customer"
    private val CONTACT_OFFER_ENDPOINT = "offer/contact"
    private val CONTACT_OFFER_ENDPOINT_DECLINE = "offer/decline/"

    fun create(context: Context, customer: Customer, complete: (Boolean, customer: Customer?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${ENDPOINT}"
        val data = Gson().toJson(customer)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            val response = Gson().fromJson(json, CustomerCreateResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
            } else {
                Log.d(Constants.TAG, "Could not create the customer: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create the customer: $error")
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

    fun update(context: Context, customer: Customer, complete: (Boolean, customer: Customer?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${ENDPOINT}"
        val data = Gson().toJson(customer)

        val createRequest = object : StringRequest(Request.Method.PUT, url, Response.Listener { json ->
            val response = Gson().fromJson(json, CustomerCreateResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
            } else {
                Log.d(Constants.TAG, "Could not update the customer: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not update the customer: $error")
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

    fun submitFreeDemandList(context: Context, freeDemand: FreeDemand, complete: (Boolean, customer: FreeDemand?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${FREE_DEMAND_ENDPOINT}"
        val data = Gson().toJson(freeDemand)
        data.replace("\\", "")

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            val response = Gson().fromJson(json, FreeDemandCreateResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data[0], null)
            } else {
                Log.d(Constants.TAG, "Could not create the freeDemand: $response.error")
                complete(false, null, response.error)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create the freeDemand: $error")
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

    fun requestProduct(context: Context, requestProduct: RequestProduct,
                       complete: (Boolean, searchProductResponse: SearchProductResponse?, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CustomerService.CUSTOMER_REQUEST_PRODUCT}"
        val data = Gson().toJson(requestProduct)
        Log.e("REQ","Request Product : : "+data.toString())
        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            val response = Gson().fromJson(json, RequestProductResponse::class.java)
            Log.e("RES","Product : : "+json.toString())
            if (response.data != null && response.data.size > 0) {
                complete(true, response.data[0], null)
            } else {
                complete(false, null, "Error desconocido")
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create requestProduct: $error")
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

    fun getWholesalersList(context: Context, complete: (Boolean, wholesalerList: List<Wholesaler>, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CustomerService.WHOLESALERS_ENDPOINT}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, WholesalersListResponse::class.java)
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

    fun makeAnOrder(context: Context, makeAnOrder: MakeAnOrder,
                    complete: (Boolean, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CustomerService.MAKE_AN_ORDER_ENDPOINT}"
        val data = Gson().toJson(makeAnOrder)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            complete(true, null)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create makeAnOrder: $error")
            complete(false, "Error desconocido")
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

    fun inviteDistributor(context: Context, inviteDistributor: InviteDistributor,
                          complete: (Boolean, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CustomerService.INVITE_DISTRIBUTOR_ENDPOINT}"
        val data = Gson().toJson(inviteDistributor)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            complete(true, null)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create inviteDistributor: $error")
            complete(false, "Error desconocido")
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

    fun getCustomersNotifications(context: Context, customerId: String, complete: (Boolean, data: List<NotificationsDetails>, error: String?) -> Unit) {

        val url = "${context.getString(R.string.API_URL)}${CustomerService.CUSTOMERS_NOTIFICATIONS_ENDPOINT}${customerId}"
        Log.e("REQ","GET customer Noti: : "+url)
        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, NotificationsResponse::class.java)
            Log.e("BBBBBBBBBB","Customer NOTI RES : :"+json.toString())
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

    fun acceptNotification(context: Context, notificationId: String, complete: (Boolean, error: String?) -> Unit) {

        val url = "${context.getString(R.string.API_URL)}${CustomerService.ACCEPT_NOTIFICATION_ENDPOINT}${notificationId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            complete(true, null)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create proposalItems: $error")
            complete(false, "Error desconocido")
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun rejectNotification(context: Context, notificationId: String, complete: (Boolean, error: String?) -> Unit) {

        val url = "${context.getString(R.string.API_URL)}${CustomerService.REJECT_NOTIFICATION_ENDPOINT}${notificationId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            complete(true, null)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create proposalItems: $error")
            complete(false, "Error desconocido")
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun submitCustomerAvailability(context: Context, customerId: String, availability: Availability,
                                   complete: (Boolean, error: String?) -> Unit) {

        val url = "${context.getString(R.string.API_URL)}${CustomerService.SUBMIT_CUSTOMR_AVAILABILITY_ENDPOINT}" +
                "${customerId}${"/availability"}"
        val data = Gson().toJson(availability)
        Log.e("REQ","Submit Avail : "+ url+" DATA : :"+data.toString())

        val createRequest = object : StringRequest(Request.Method.PUT, url, Response.Listener { json ->
            Log.e("RES","Submit Avail : "+ json.toString())
            complete(true, null)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create proposalItems: $error")
            complete(false, "Error desconocido")
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

    fun getCustomerAvailability(context: Context, customerId: String, complete: (Boolean, data: CustomerAvailability, error: String?) -> Unit) {

        val url = "${context.getString(R.string.API_URL)}${CustomerService.SUBMIT_CUSTOMR_AVAILABILITY_ENDPOINT}" +
                "${customerId}${"/availability"}"
        Log.e("REQ","Get Avail : "+url)
        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            Log.e("RES","Get Avail : "+json.toString())
            val response = Gson().fromJson(json, CustomerAvailabilityResponse::class.java)
            complete(true, response.data, null)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get Customer Availbility: $error")
            var data = CustomerAvailability()
            complete(false, data, "Error desconocido")
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun getReviewOffersList(context: Context, inputObject: CustomerOffer, complete: (Boolean, data: List<ReviewOfferResponseItems>, error: String?) -> Unit) {

        val url = "${context.getString(R.string.API_URL)}${CustomerService.REVIEW_OFFERS_ENDPOINT}"
        Log.e("REQ","GET customer ReviewOffers: : "+url)
        val inputData = Gson().toJson(inputObject)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            val response = Gson().fromJson(json, ReviewOfferResponse::class.java)
            Log.e("RES","GET customer ReviewOffers: : "+json.toString())
            complete(true, response.data, null)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get Review Offers: $error")
            val data: MutableList<ReviewOfferResponseItems> = mutableListOf()
            complete(false, data, "Error desconocido")
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }
            override fun getBody(): ByteArray {
                return inputData.toByteArray()
            }
        }
        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun contactOffer(context: Context, inputModel: ContactOffer,
                     complete: (Boolean, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CustomerService.CONTACT_OFFER_ENDPOINT}"
        val data = Gson().toJson(inputModel)

        val createRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { json ->
            complete(true, null)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create inputModel: $error")
            complete(false, "Error desconocido")
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

    fun contactOfferDecline(context: Context, offerid: String,
                            complete: (Boolean, error: String?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CustomerService.CONTACT_OFFER_ENDPOINT_DECLINE}${offerid}"
        Log.e("REQ","contactOfferDecline : "+url)
        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            Log.e("RES","contactOfferDecline : "+json.toString())
            complete(true, null)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not create inputModel: $error")
            complete(false, "Error desconocido")
        }) {
            override fun getBodyContentType(): String {
                return Constants.JSON_CONTENT_TYPE
            }
        }

        createRequest.setRetryPolicy(DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(createRequest)
    }

}