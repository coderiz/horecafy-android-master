package app.horecafy.com.activities.customers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.models.Customer
import app.horecafy.com.models.CustomerOffer
import app.horecafy.com.models.ReviewOfferItems
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_customer_review_offers_category.*
import java.util.*


class CustomerReviewOffersCategoryActivity : AppCompatActivity() {

    var mReviewOffersParentList: MutableList<ReviewOfferItems> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_review_offers_category)
        //first
        clReviewCategoryDistributorButton.setOnClickListener(View.OnClickListener {
            val intent = CustomerReviewOfferWholesalersActivity.intent(this,
                    mReviewOffersParentList as ArrayList<ReviewOfferItems>)
            startActivity(intent)
        })
        //second
        clReviewCategoryProductButton.setOnClickListener(View.OnClickListener {
            val intent = CustomerReviewOfferProductsActivity.intent(this,
                    mReviewOffersParentList as ArrayList<ReviewOfferItems>)
            startActivity(intent)
        })

       // getReviewOffersList()
    }

     fun getReviewOffersList() {
         mReviewOffersParentList.clear()
        val customerId = AuthService.customer!!.hiddenId!!
        val inputObject = CustomerOffer(customerId)

        UiHelpers.showProgessBar(window,rlProgressBarCustomerReviewOffer)

        CustomerService.getReviewOffersList(this@CustomerReviewOffersCategoryActivity, inputObject) { status, data, error ->

            UiHelpers.hideProgessBar(window, rlProgressBarCustomerReviewOffer)

            if (status) {

                val dataList = data

                if (dataList.size > 0) {

                    dataList.forEach {

                        val item = ReviewOfferItems(it.hiddenId,
                                it.id,
                                it.WholeSaler.hiddenId,
                                it.WholeSaler.id,
                                it.WholeSaler.name,
                                it.quantyPerMonth,
                                it.TypeOfFormat.id,
                                it.TypeOfFormat.name,
                                it.Product.id,
                                it.Product.name,
                                it.offerPrice,
                                it.brand,
                                it.images,
                                it.video,
                                it.approvedByCustomer,
                                it.fomat,
                                it.comments)

                        mReviewOffersParentList.add(item)
                    }
                } else {
                    Toast.makeText(this, "" + Constants.NO_DATA_AVAILABLE, Toast.LENGTH_SHORT).show()
                }

//                Log.e("Parent List size", "" + mReviewOffersParentList.size)
            } else {
                Toast.makeText(this, "" + Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getReviewOffersList()
    }
}
