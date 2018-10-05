package app.horecafy.com.activities.wholesalers

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import app.horecafy.com.R
import kotlinx.android.synthetic.main.activity_wholesaler_make_offer_thanks.*

class WholesalerMakeOfferThanks : AppCompatActivity() {

    companion object {
        fun intent(context: Context): Intent {
            val intent = Intent(context, WholesalerMakeOfferThanks::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wholesaler_make_offer_thanks)

        textMainMenu.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, WholesalerMainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        })
    }
}
