package app.horecafy.com.activities.customers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.models.Category
import app.horecafy.com.models.Offer
import app.horecafy.com.services.OfferService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_customer_offer.*

class CustomerOfferActivity : AppCompatActivity() {

    companion object {

        val INTENT_CATEGORY = "INTENT_REVIEW_OFFERS_LIST"
        val INTENT_OFFER = "INTENT_OFFER"
        val REQUEST_THANKS = 1

        fun intent(context: Context, category: Category, offerId: Long): Intent {
            val intent = Intent(context, CustomerOfferActivity::class.java)
            intent.putExtra(INTENT_CATEGORY, category);
            intent.putExtra(INTENT_OFFER, offerId);
            return intent
        }
    }

    var category: Category? = null
    var offerId: Long? = null
    var offer: Offer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_offer)

        // Get category and offerId and load it from API
        category = intent.getSerializableExtra(INTENT_CATEGORY) as Category?
        offerId = intent.getSerializableExtra(INTENT_OFFER) as Long

        UiHelpers.showProgessBar(window, progressBar)
        OfferService.get(this, offerId!!, { status, data, error ->
            UiHelpers.hideProgessBar(window, progressBar)
            if (status && data != null) {
                offer = data

                // Bind useful data
                val uri = "${this.getString(R.string.IMAGE_URL)}${category!!.image}"
                Picasso.with(this).load(uri).into(imageCategory)

                textCategory.text = offer!!.category!!.name
                textHiddenId.text = offer!!.demand!!.hiddenId.toString()
                textBrand.text = offer!!.demand!!.brand
                textFormat.text = offer!!.demand!!.format

                if (offer!!.demand!!.quantyPerMonth != null) {
                    textQuantyPerMonth.text = offer!!.demand!!.quantyPerMonth.toString()
                }

                if (offer!!.demand!!.targetPrice != null) {
                    textTargetPrice.text = offer!!.demand!!.targetPrice.toString()
                }

                textComments.text = offer!!.demand!!.comments

                textOfferBrand.text = offer!!.brand
                textOfferFormat.text = offer!!.fomat
                textOfferPrice.text = offer!!.offerPrice.toString()
                textOfferComments.text = offer!!.comments
            } else {
                // TODO Error loading demands
                Log.w(Constants.TAG, "Error loading offer: ${error}")
            }

            buttonContact.setOnClickListener(View.OnClickListener {
                UiHelpers.showProgessBar(window, progressBar)
                OfferService.accept(this, offer!!.hiddenId) { status, error ->
                    UiHelpers.hideProgessBar(window, progressBar)
                    if (status) {
                        val intent = CustomerShareListThanks.intent(this)
                        startActivityForResult(intent, REQUEST_THANKS)

                    } else {
                        Toast.makeText(this, "Error desconocido", Toast.LENGTH_LONG).show()
                    }
                }
            })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_THANKS && resultCode == AppCompatActivity.RESULT_CANCELED) {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
