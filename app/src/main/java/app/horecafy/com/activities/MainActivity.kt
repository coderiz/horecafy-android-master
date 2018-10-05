package app.horecafy.com.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.activities.customers.CustomerEntryActivity
import app.horecafy.com.activities.customers.CustomerMainActivity
import app.horecafy.com.activities.wholesalers.WholesalerEntryActivity
import app.horecafy.com.activities.wholesalers.WholesalerMainActivity
import app.horecafy.com.models.Login
import app.horecafy.com.services.AuthService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.SharedPreferencesHelper
import app.horecafy.com.util.TypeUser
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        configureUI()
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        val preferences = SharedPreferencesHelper()
        val typeUser = preferences.getValue(this, Constants.TYPEUSER_KEY)
        val username = preferences.getValue(this, Constants.USERNAME_KEY)
        val password = preferences.getValue(this, Constants.PASSWORD_KEY)

        if (preferences.getValue(this@MainActivity, Constants.REMEMBER).equals("true")) {
            if (!typeUser.isNullOrEmpty()) {
                if (typeUser == TypeUser.CUSTOMER.value) {
                    Log.e("MAINRESULT : : ", "CUSTOME")
                    AuthService.loginAsCustomer(this, Login(username, password,
                            TypeUser.CUSTOMER.value), { status, data, error ->
                        if (data != null) {
                            val intent = Intent(this, CustomerMainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                    })
                } else {
                    Log.e("MAINRESULT : : ", "WHOLESALER")
                    AuthService.loginAsWholesaler(this, Login(username, password,
                            TypeUser.WHOLESALER.value), { status, data, error ->
                        if (data != null) {
                            val intent = Intent(this, WholesalerMainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                    })
                }
            }
        }

        buttonHoreca.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, WholesalerEntryActivity::class.java))
        })

        buttonCustomer.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CustomerEntryActivity::class.java))
        })
    }

    private fun configureUI() {
        val window = this.window

        // Hide ActionBar
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide();

        // Change StatusBar color
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.backgroundColor);
        }*/
    }
}
