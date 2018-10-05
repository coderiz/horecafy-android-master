package app.horecafy.com.activities.customers

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import app.horecafy.com.R

class CustomerCheckOffersThanks : AppCompatActivity() {

    companion object {
        fun intent(context: Context): Intent {
            val intent = Intent(context, CustomerCheckOffersThanks::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_check_offers_thanks)
    }
}
