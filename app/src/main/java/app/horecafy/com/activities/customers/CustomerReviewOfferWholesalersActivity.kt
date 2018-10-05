package app.horecafy.com.activities.customers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import app.horecafy.com.R
import app.horecafy.com.adapters.ReviewOfferWholesalersListRecyclerViewAdapter
import app.horecafy.com.models.ReviewOfferItems
import app.horecafy.com.models.ReviewOffersProductDetails
import app.horecafy.com.models.ReviewOffersWholesalerDetails
import kotlinx.android.synthetic.main.activity_customer_review_offer_wholesalers.*
import java.util.*

class CustomerReviewOfferWholesalersActivity : AppCompatActivity() {

    companion object {
        val INTENT_REVIEW_OFFERS_LIST = "INTENT_REVIEW_OFFERS_LIST"

        fun intent(context: Context, category: ArrayList<ReviewOfferItems>): Intent {
            val intent = Intent(context, CustomerReviewOfferWholesalersActivity::class.java)
            intent.putParcelableArrayListExtra(INTENT_REVIEW_OFFERS_LIST, category);
            return intent
        }
    }

    var mReviewOffersParentList: ArrayList<ReviewOfferItems>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_review_offer_wholesalers)

        // Get category from intent
        if (mReviewOffersParentList == null) {
            mReviewOffersParentList = intent.getParcelableArrayListExtra(INTENT_REVIEW_OFFERS_LIST)
        }
        SetData()
    }

    private fun SetData() {
        val distributorsNamesList: MutableList<ReviewOffersWholesalerDetails> = mutableListOf()
        val productsList: MutableList<ReviewOffersProductDetails> = mutableListOf()
        // add elements to distributorsNamesList, including duplicates

        mReviewOffersParentList!!.forEach {
            val item = ReviewOffersWholesalerDetails(it.WholeSalerHiddenId, it.WholeSalerId,
                    it.WholeSalerName.toString(), 0)
            val itemproduct = ReviewOffersProductDetails(it.ProductId.toString(),
                    it.ProductName.toString())
            productsList.add(itemproduct)
            distributorsNamesList.add(item)

        }

        val uniqueProducts = HashSet<ReviewOffersProductDetails>(productsList)
        val uniqueProductsList: MutableList<ReviewOffersProductDetails> = mutableListOf()
        uniqueProducts.forEach {
            Log.e("" + it.name, "" + Collections.frequency(productsList, it))
            val item = ReviewOffersProductDetails(it.id.toString(), it.name.toString())
            uniqueProductsList.add(item)
        }

        val unique = HashSet<ReviewOffersWholesalerDetails>(distributorsNamesList)

        val wholesalersList: MutableList<ReviewOffersWholesalerDetails> = mutableListOf()

        unique.forEach {
            //                    Log.e("" + it.name, "" + Collections.frequency(distributorsNamesList, it))
            val item = ReviewOffersWholesalerDetails(it.hiddenId, it.id,
                    it.name.toString(), Collections.frequency(distributorsNamesList, it))
            wholesalersList.add(item)
        }
        //Log.e("Wholesalers List size", "" + wholesalersList.size)
        // Configure recyclerView
        rvReviewOfferWholesalersList.layoutManager = LinearLayoutManager(this)
        rvReviewOfferWholesalersList.itemAnimator = DefaultItemAnimator()

        // Set the adapter
        val mAdapter = ReviewOfferWholesalersListRecyclerViewAdapter(wholesalersList, this)
        rvReviewOfferWholesalersList.adapter = mAdapter

        tvSharedProductsCount.setText(uniqueProductsList!!.size.toString())
        tvProductsOfferedCount.setText(wholesalersList.size.toString())
    }

    fun showDetailScreen(item: ReviewOffersWholesalerDetails) {

        val intent = CustomerReviewOfferWholesalersDetailsActivity.intent(this, item,
                mReviewOffersParentList as ArrayList<ReviewOfferItems>)

        startActivityForResult(intent,200)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home ->
                onBackPressed()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("latestData","00   " + requestCode + " 99  " + data!!.extras)

            if(requestCode==200) {
                Log.e("LatestValue", "111   " + mReviewOffersParentList!!.size)
                Log.e("LatestValue", "111   " + data!!.getParcelableArrayExtra("latestData"))

              //  mReviewOffersParentList= data!!.getParcelableArrayExtra("latestData") as ArrayList<ReviewOfferItems>
                mReviewOffersParentList = data!!.getParcelableArrayListExtra<ReviewOfferItems>("latestData")
                SetData()

            }
    }
}