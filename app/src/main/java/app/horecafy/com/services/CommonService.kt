package app.horecafy.com.services

import android.content.Context
import android.util.Log
import app.horecafy.com.R
import app.horecafy.com.models.Category
import app.horecafy.com.models.Family
import app.horecafy.com.models.TypeOfBusiness
import app.horecafy.com.models.TypeOfFormat
import app.horecafy.com.services.responses.CategoryGetResponse
import app.horecafy.com.services.responses.FamilyResponse
import app.horecafy.com.services.responses.TypeOfBusinessGetResponse
import app.horecafy.com.services.responses.TypeOfFormatGetResponse
import app.horecafy.com.util.Constants
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject


object CommonService {
    private val TYPE_OF_BUSINESS_ENDPOINT = "type-business"
    private val CATEGORY_ENDPOINT = "category"
    private val FAMILY_ENDPOINT = "family"
    private val TYPE_OF_FORMAT_ENDPOINT = "type-format"
    private val CUSTOMER_STATE = "customer/stats/"
    private val WHOLESALER_STATE = "wholesaler/stats/"
    private val CUSTOMER_ENDPOINT = "customer"
    private val WHOLESALER_ENDPOINT = "wholesaler"

    fun typeOfBusiness(context: Context, complete: (Boolean, List<TypeOfBusiness>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${TYPE_OF_BUSINESS_ENDPOINT}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, TypeOfBusinessGetResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get the type of business: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the type of business: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun categories(context: Context, complete: (Boolean, MutableList<Category>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CATEGORY_ENDPOINT}"
        Log.e("REQ","CAT : : "+url)
        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, CategoryGetResponse::class.java)
            Log.e("RES","CAT : : "+json.toString())

            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get mUserAvailabilityList: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get mUserAvailabilityList: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }
    //Wholesalers
    fun categories(context: Context,wid:String, complete: (Boolean, MutableList<Category>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CATEGORY_ENDPOINT}"+"/"+wid
        Log.e("REQ","CAT : : "+url)
        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, CategoryGetResponse::class.java)
            Log.e("RES","CAT : : "+json.toString())

            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get mUserAvailabilityList: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get mUserAvailabilityList: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun categoriesDemandWithFamilyCount(context: Context, customerId: Long, complete: (Boolean, MutableList<Category>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CATEGORY_ENDPOINT}/demand/family?customerId=${customerId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, CategoryGetResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get mUserAvailabilityList: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get mUserAvailabilityList: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun categoriesOfferWithFamilyCount(context: Context, customerId: Long, complete: (Boolean, MutableList<Category>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CATEGORY_ENDPOINT}/offer/family?customerId=${customerId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, CategoryGetResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get mUserAvailabilityList: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get mUserAvailabilityList: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun categoriesWholesalerListWithFamilyCount(context: Context, wholesalerId: Long, complete: (Boolean, MutableList<Category>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CATEGORY_ENDPOINT}/wholesaler-list/family?wholesalerId=${wholesalerId}"
        Log.e("URL","F_URL"+url)
        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, CategoryGetResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get mUserAvailabilityList: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get mUserAvailabilityList: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun families(context: Context, categoryId: Int, complete: (Boolean, List<Family>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${FAMILY_ENDPOINT}?categoryId=${categoryId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, FamilyResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get families: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get families: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun familiesByCustomerId(context: Context, customerId: Long, categoryId: Int, complete: (Boolean, List<Family>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CUSTOMER_ENDPOINT}/${customerId}/families?categoryId=${categoryId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, FamilyResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get families: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get families: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun familiesByWholesalerId(context: Context, wholesalerId: Long, categoryId: Int, complete: (Boolean, List<Family>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${WHOLESALER_ENDPOINT}/${wholesalerId}/families?categoryId=${categoryId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, FamilyResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get families: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get families: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun familiesInDemand(context: Context, customerId: Long, categoryId: Int, complete: (Boolean, List<Family>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${FAMILY_ENDPOINT}/demand?customerId=${customerId}&categoryId=${categoryId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, FamilyResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get families in demand: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get families in demand: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun familiesInOffer(context: Context, customerId: Long, categoryId: Int, complete: (Boolean, List<Family>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${FAMILY_ENDPOINT}/offer?customerId=${customerId}&categoryId=${categoryId}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, FamilyResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get families in offer: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get families in offer: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun typeOfFormat(context: Context, complete: (Boolean, List<TypeOfFormat>?) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${TYPE_OF_FORMAT_ENDPOINT}"

        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            val response = Gson().fromJson(json, TypeOfFormatGetResponse::class.java)
            if (response.error.isNullOrEmpty()) {
                complete(true, response.data)
            } else {
                Log.d(Constants.TAG, "Could not get the type of format: $response.error")
                complete(false, null)
            }
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the type of format: $error")
            complete(false, null)
        }) {}

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun getBadgeCustomer(context: Context,cID:String, complete: (Boolean,POffer:Int,Pvisit:Int) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${CUSTOMER_STATE}${cID}"
        Log.e("REQ","Coustomer Count :"+url)
        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            Log.e("RES","Coustomer Count :"+json.toString())
                var PendingOffers = 0
                var PendingVisits = 0
                try {
                    val jsonObject = JSONObject(json)
                    val jsonObject1 = jsonObject.getJSONObject("data")
                    PendingOffers = jsonObject1.getInt("totalPendingOffers")
                    PendingVisits = jsonObject1.getInt("totalPendingVisits")

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                complete(true, PendingOffers,PendingVisits)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the type of format: $error")
            complete(false, 0,0)
        }) {}
        Volley.newRequestQueue(context).add(createRequest)
    }

    fun getBadgeWholesaler(context: Context,cID:String, complete: (Boolean,POffer:Int,Pvisit:Int) -> Unit) {
        val url = "${context.getString(R.string.API_URL)}${WHOLESALER_STATE}${cID}"
        Log.e("REQ","Wholesaler Count :"+url)
        val createRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { json ->
            Log.e("RES","Wholesaler Count :"+json.toString())
            var PendingOffers = 0
            var PendingVisits = 0
            try {
                val jsonObject = JSONObject(json)
                val jsonObject1= jsonObject.getJSONObject("data")
                PendingOffers = jsonObject1.getInt("totalPendingDemands")
                PendingVisits = jsonObject1.getInt("totalPendingVisits")

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            complete(true, PendingOffers,PendingVisits)
        }, Response.ErrorListener { error ->
            Log.d(Constants.TAG, "Could not get the type of format: $error")
            complete(false, 0,0)
        }) {}
        Volley.newRequestQueue(context).add(createRequest)
    }
}