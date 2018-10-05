package app.horecafy.com.activities.customers

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import app.horecafy.com.FullScreenMediaController
import app.horecafy.com.R
import app.horecafy.com.adapters.ReviewOfferProductsListRecyclerViewAdapter
import app.horecafy.com.models.ContactOffer
import app.horecafy.com.models.ReviewOfferItems
import app.horecafy.com.models.ReviewOffersProductDetails
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.adapters.SlidingImage_Adapter
import app.horecafy.com.services.OfferService
import app.horecafy.com.util.UiHelpers
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_customer_offer.*
import kotlinx.android.synthetic.main.activity_customer_review_offer_products.*
import java.util.*

class CustomerReviewOfferProductsActivity : AppCompatActivity() {

    companion object {
        val INTENT_REVIEW_OFFERS_LIST = "INTENT_REVIEW_OFFERS_LIST"
        val REQUEST_THANKS = 1
        fun intent(context: Context, list: ArrayList<ReviewOfferItems>): Intent {
            val intent = Intent(context, CustomerReviewOfferProductsActivity::class.java)
            intent.putParcelableArrayListExtra(INTENT_REVIEW_OFFERS_LIST, list)
            return intent
        }
    }

    var mReviewOffersParentList: ArrayList<ReviewOfferItems>? = null
    var mAdapter : ReviewOfferProductsListRecyclerViewAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_review_offer_products)

        if (mReviewOffersParentList == null) {
            mReviewOffersParentList = intent.getParcelableArrayListExtra(CustomerReviewOfferWholesalersActivity.INTENT_REVIEW_OFFERS_LIST)
        }

        val productsList: MutableList<ReviewOffersProductDetails> = mutableListOf()
        // add elements to productsList, including duplicates

        mReviewOffersParentList!!.forEach {
            val item = ReviewOffersProductDetails(it.ProductId.toString(),
                    it.ProductName.toString())
            productsList.add(item)
        }

        val uniqueProducts = HashSet<ReviewOffersProductDetails>(productsList)

        val uniqueProductsList: MutableList<ReviewOffersProductDetails> = mutableListOf()

        uniqueProducts.forEach {

            Log.e("" + it.name, "" + Collections.frequency(productsList, it))
            val item = ReviewOffersProductDetails(it.id.toString(), it.name.toString())
            uniqueProductsList.add(item)
        }

        // Configure recyclerView
        rvReviewOfferProductsList.layoutManager = LinearLayoutManager(this)
        rvReviewOfferProductsList.itemAnimator = DefaultItemAnimator()
        rvReviewOfferProductsList.setHasFixedSize(true)
        rvReviewOfferProductsList.setNestedScrollingEnabled(false)

        // Set the adapter
        mAdapter = ReviewOfferProductsListRecyclerViewAdapter(uniqueProductsList,
                this@CustomerReviewOfferProductsActivity, mReviewOffersParentList)
        rvReviewOfferProductsList.adapter = mAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home ->
                onBackPressed()
        }
        return true
    }

    fun contactDistributor(offerHiddenid: Long,pos: Int) {
        UiHelpers.showProgessBar(window, rlProgressBarReviewOfferProducts)
        OfferService.accept(this, offerHiddenid) { status, error ->
            UiHelpers.hideProgessBar(window, rlProgressBarReviewOfferProducts)
            if (status) {
                mAdapter!!.notifyDataSetChanged()
                val intent = CustomerShareListThanks.intent(this)
                startActivityForResult(intent, CustomerReviewOfferProductsActivity.REQUEST_THANKS)

            } else {
                Toast.makeText(this, "Error desconocido", Toast.LENGTH_LONG).show()
            }
        }
    }

    /*fun contactDistributor(wholeSalerId: String) {

        UiHelpers.showProgessBar(window, rlProgressBarReviewOfferProducts)

        val customerId = AuthService.customer!!.id!!

        val inputModel = ContactOffer(customerId, wholeSalerId)

        CustomerService.contactOffer(this, inputModel) { status, error ->

            UiHelpers.hideProgessBar(window, rlProgressBarReviewOfferProducts)

            if (status) {
                startActivity(Intent(this, CustomerReviewOffersThanks::class.java))
            }
        }
    }*/

    fun OfferDeclien(OfferID: String,pos: Int) {

        UiHelpers.showProgessBar(window, rlProgressBarReviewOfferProducts)
        CustomerService.contactOfferDecline(this, OfferID) { status, error ->

            UiHelpers.hideProgessBar(window, rlProgressBarReviewOfferProducts)

            if (status) {
                mReviewOffersParentList!!.removeAt(pos)
                mAdapter!!.notifyDataSetChanged()
              //  startActivity(Intent(this, CustomerReviewOffersThanks::class.java))
            }
        }
    }

    fun VideoPreview(VideoPath: String){
        var mediaController: MediaController? = null
        val dialogS = Dialog(this)
        dialogS.setContentView(R.layout.popup_video_show)

        dialogS.setCanceledOnTouchOutside(false)
        dialogS.setCancelable(false)
        val lp = WindowManager.LayoutParams()

        lp.copyFrom(dialogS.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialogS.show()
        dialogS.getWindow()!!.setAttributes(lp)

        dialogS.getWindow()!!.setGravity(Gravity.CENTER)
        dialogS.getWindow()!!.setBackgroundDrawable(ColorDrawable(0))


        val videoView = dialogS.findViewById(R.id.videoView) as VideoView
        val tv_g_cancel = dialogS.findViewById(R.id.tv_close) as ImageView
        val videoUri = Uri.parse(getString(R.string.ImageBaseURL) + VideoPath)

        Log.e("LLLLLLLL", "videoUri : : " + videoUri.toString() + " : " + R.string.IMAGE_URL + VideoPath)
        videoView.setVideoURI(videoUri)

        mediaController = FullScreenMediaController(this)
        mediaController.setAnchorView(videoView)

        videoView.setMediaController(mediaController)
        videoView.start()
        tv_g_cancel.setOnClickListener { dialogS.dismiss() }

        dialogS.show()
    }

    fun ImagePreview(ImagePath: String){
        val ImagesArray: List<String> = ImagePath!!.split(",").map { it.trim() }
        Log.e("IMAGE","ImageURL : : "+ImagesArray.toString())

        //  ImagePagerAdapter(activity,result)
        val mPager: ViewPager
        var currentPage = 0
        val dialogS = Dialog(this)
        dialogS.setContentView(R.layout.popup_image_show)
        dialogS.setCanceledOnTouchOutside(false)
        dialogS.setCancelable(false)
        val lp = WindowManager.LayoutParams()

        lp.copyFrom(dialogS.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialogS.show()
        dialogS.getWindow()!!.setAttributes(lp)

        dialogS.getWindow()!!.setGravity(Gravity.CENTER)
        dialogS.getWindow()!!.setBackgroundDrawable(ColorDrawable(0))

        mPager = dialogS.findViewById(R.id.pager) as ViewPager
        mPager.adapter = SlidingImage_Adapter(this, ImagesArray)
        val indicator = dialogS.findViewById(R.id.indicator) as CirclePageIndicator
        indicator.setViewPager(mPager)

        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })
        val tv_g_cancel = dialogS.findViewById(R.id.tv_close) as ImageView

        tv_g_cancel.setOnClickListener { dialogS.dismiss() }

        dialogS.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CustomerReviewOfferProductsActivity.REQUEST_THANKS && resultCode == AppCompatActivity.RESULT_CANCELED) {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}