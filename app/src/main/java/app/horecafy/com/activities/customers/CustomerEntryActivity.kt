package app.horecafy.com.activities.customers

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import app.horecafy.com.R
import kotlinx.android.synthetic.main.activity_customer_entry.*


class CustomerEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        configureUI()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_entry)

        buttonCustomerRegister.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CustomerRegisterActivity::class.java))
        })

        buttonCustomerLogin.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CustomerLoginActivity::class.java))
        })
    }

    private fun configureUI() {
        val window = this.window

        // Hide ActionBar
        window.requestFeature(Window.FEATURE_ACTION_BAR);
        supportActionBar!!.hide();

        // Change StatusBar color
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.backgroundColor);
        }*/
    }
}