package app.horecafy.com.activities.customers

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import app.horecafy.com.R
import kotlinx.android.synthetic.main.activity_customer_share_list_thanks.*

class CustomerShareListThanks : AppCompatActivity() {

    companion object {
        fun intent(context: Context): Intent {
            val intent = Intent(context, CustomerShareListThanks::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_share_list_thanks)

        textMainMenu.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, CustomerMainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        })
    }
}
