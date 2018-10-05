package app.horecafy.com.activities.customers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import app.horecafy.com.R
import kotlinx.android.synthetic.main.activity_customer_demand_success.*

class CustomerSearchProductsSuccessActivity : AppCompatActivity() {

    companion object {
        val INTENT_CATEGORY = "INTENT_REVIEW_OFFERS_LIST"

        fun intent(context: Context): Intent {
            val intent = Intent(context, CustomerSearchProductsSuccessActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_demand_success)

        tvBacktoMainMenu.setOnClickListener { v: View? ->

            val intent = Intent(applicationContext, CustomerMainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}
