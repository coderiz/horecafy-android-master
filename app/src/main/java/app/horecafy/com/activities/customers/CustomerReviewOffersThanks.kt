package app.horecafy.com.activities.customers

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import app.horecafy.com.R
import kotlinx.android.synthetic.main.activity_customer_review_offers_thanks.*

class CustomerReviewOffersThanks : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_review_offers_thanks)

        textMainMenuReviewOffers.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, CustomerMainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        })
    }
}
