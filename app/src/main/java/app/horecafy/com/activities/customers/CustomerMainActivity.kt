package app.horecafy.com.activities.customers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.R.id.tv_total_review_count
import app.horecafy.com.activities.MainActivity
import app.horecafy.com.adapters.CustomerNotificationsRecyclerViewAdapter
import app.horecafy.com.models.CustomerOffer
import app.horecafy.com.models.NotificationsDetails
import app.horecafy.com.models.ReviewOfferItems
import app.horecafy.com.models.TypeOfFormat
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CommonService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.services.CustomerService.getReviewOffersList
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_customer_demand.*
import kotlinx.android.synthetic.main.activity_customer_main.*
import kotlinx.android.synthetic.main.activity_customer_review_offers_category.*
import kotlinx.android.synthetic.main.fragment_customer_notifications.*


class CustomerMainActivity : AppCompatActivity() {
    var mNotificationslist: MutableList<NotificationsDetails> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_main)
       // getCustomersNotificationsList()
       // getReviewOffersList()

        //1
        customerCreateListButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CustomerCreateListCategoryActivity::class.java))
        })

        //2
        customerShareListButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CustomerShareListCategoryActivity::class.java))
        })
        //
        customerCheckOffersButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CustomerCheckOffersCategoryActivity::class.java))
        })
        //4
        customerSearchProductButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CustomerSearchProductsActivity::class.java))
        })
        //6
        customerMakeAnOrderButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CustomerMakeAnOrderActivity::class.java))
        })
        //5
        customerCommercialVisitButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CustomerCommercialVisitsActivity::class.java))
        })
        //3
        customerReviewOffersButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CustomerReviewOffersCategoryActivity::class.java))
        })
    }

    override fun onResume() {
        super.onResume()
        getBadge()
       // getCustomersNotificationsList()
      //  getReviewOffersList()
    }

    private fun getBadge() {
        val customerId = AuthService.customer!!.id!!
        CommonService.getBadgeCustomer(this,customerId, { status,C_offer,C_visit ->
            if (status) {
                Log.e("RES Count","C_offer : "+C_offer+" C_visit : "+C_visit)
                if (C_offer > 0) {
                    tv_total_review_count.visibility = View.VISIBLE
                    tv_total_review_count.setText(C_offer.toString())
                } else {
                    tv_total_review_count.visibility = View.GONE
                }

                if (C_visit > 0) {
                    tv_total_comarcial_count.visibility = View.VISIBLE
                    tv_total_comarcial_count.setText(C_visit.toString())
                } else {
                    tv_total_comarcial_count.visibility = View.GONE
                }
            } else {
                // TODO Error loading typeofformat
                UiHelpers.hideProgessBar(window, progressBar)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, CustomerProfileActivity::class.java))

                return true
            }
            R.id.action_logout -> {
                alertDialogforLogout()

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun alertDialogforLogout() {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@CustomerMainActivity)

        // Set the alert dialog title
//        builder.setTitle("App background color")

        // Display a message on alert dialog
//        builder.setMessage("¿Quieres dar consumo y precio objetivo para que te ofrecen mejor?")
        builder.setMessage(this.getString(R.string.logout_msg))

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Sí") { dialog, which ->
            // Do something when user press the positive button
            AuthService.logout(this)
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

}