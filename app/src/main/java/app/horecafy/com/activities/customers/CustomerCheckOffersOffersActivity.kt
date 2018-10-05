package app.horecafy.com.activities.customers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import app.horecafy.com.R
import app.horecafy.com.models.Category
import app.horecafy.com.models.Offer
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.OfferService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.Helpers
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_customer_check_offers_offers.*

class CustomerCheckOffersOffersActivity : AppCompatActivity() {

    companion object {
        val INTENT_CATEGORY = "INTENT_REVIEW_OFFERS_LIST"
        val INTENT_DEMAND = "INTENT_DEMAND"
        val REQUEST_OFFER = 1

        fun intent(context: Context, category: Category, demandId: Long): Intent {
            val intent = Intent(context, CustomerCheckOffersOffersActivity::class.java)
            intent.putExtra(INTENT_CATEGORY, category)
            intent.putExtra(INTENT_DEMAND, demandId)
            return intent
        }
    }

    var category: Category? = null
    var demandId: Long? = null
    var offers: MutableList<Offer> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_check_offers_offers)

        // Get values from intent
        if (category == null) {
            category = intent.getSerializableExtra(INTENT_CATEGORY) as Category?
        }

        if (demandId == null) {
            demandId = intent.getSerializableExtra(INTENT_DEMAND) as Long?
        }

        loadOffers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_OFFER && resultCode == AppCompatActivity.RESULT_OK) {
            // Reload demands
            loadOffers()
        }
    }

    private fun loadOffers() {
        // Load demands
        UiHelpers.showProgessBar(window, progressBar)
        OfferService.getByDemandId(this,  AuthService.customer?.hiddenId!!, demandId!!, { status, data, error ->
            UiHelpers.hideProgessBar(window, progressBar)
            if (status && data != null) {
                offers = data.toMutableList()
                setOffersAdapter()
            } else {
                // TODO Error loading demands
                Log.w(Constants.TAG, "Error loading demands: ${error}")
            }
        })
    }

    private fun setOffersAdapter() {
        listOffers.adapter = object: ArrayAdapter<Offer>(this, R.layout.list_view_item_offer, offers) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_view_item_offer, parent, false)

                val textWholesaler = view.findViewById<TextView>(R.id.textWholesaler)
                val textFamilyName = view.findViewById<TextView>(R.id.textFamilyName)
                val textZipCode = view.findViewById<TextView>(R.id.textZipCode)
                val textDate = view.findViewById<TextView>(R.id.textDate)

                val offer = offers[position]

                textWholesaler.text = offer.wholesaler?.hiddenId.toString()
                textFamilyName.text = offer.family?.name
                textZipCode.text = offer.wholesaler?.zipCode
                val date = Helpers.StringToDate(offer.sentToCustomer!!)
                textDate.text = Helpers.DateToString(date)

                // Handle click
                view.setOnClickListener(View.OnClickListener {
                    // Update demand
                    val intent = CustomerOfferActivity.intent(context, category!!,  offer.hiddenId)
                    startActivityForResult(intent, REQUEST_OFFER)
                })

                return view
            }
        }
    }
}
