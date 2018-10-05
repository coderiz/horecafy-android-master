package app.horecafy.com

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import android.text.TextUtils
import com.android.volley.Request


class HorecafyApp : Application() {

    override fun onCreate() {
        mInstance = this;

        /*val preferences = SharedPreferencesHelper()
        val typeUser =  preferences.getValue(this, Constants.TYPEUSER_KEY)
        val username = preferences.getValue(this, Constants.USERNAME_KEY)
        val password = preferences.getValue(this, Constants.PASSWORD_KEY)

        if (!typeUser.isNullOrEmpty() && !username.isNullOrEmpty() && !password.isNullOrEmpty()) {
            if (typeUser == TypeUser.CUSTOMER.value) {
                AuthService.loginAsCustomer(this, Login(username, password,
                        TypeUser.CUSTOMER.value), { status, data, error ->
                    if (data != null) {
                        val intent = Intent(this, CustomerMainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                })
            } else {
                AuthService.loginAsWholesaler(this, Login(username, password, TypeUser.WHOLESALER.value), { status, data, error ->
                    if (data != null) {
                        val intent = Intent(this, WholesalerMainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                })
            }
        }*/

        super.onCreate()
    }

    companion object {
        val TAG = HorecafyApp::class.java!!.getSimpleName()
        private var mRequestQueue: RequestQueue? = null
        var mInstance: HorecafyApp? = null
        @JvmStatic
        @Synchronized
        fun getInstance(): HorecafyApp? {
            return mInstance
        }

        fun getRequestQueue(): RequestQueue? {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mInstance)
            }

            return mRequestQueue
        }

        @JvmStatic
        fun <T> addToRequestQueue(req: Request<T>, tag: String) {
            req.setTag(if (TextUtils.isEmpty(tag)) TAG else tag)
            getRequestQueue()!!.add(req)
        }
        @JvmStatic
        fun <T> addToRequestQueue(req: Request<T>) {
            req.tag = TAG
            getRequestQueue()!!.add(req)
        }


        fun cancelPendingRequests(tag: Any) {
            if (mRequestQueue != null) {
                mRequestQueue!!.cancelAll(tag)
            }
        }
    }
}