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
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import app.horecafy.com.FullScreenMediaController
import app.horecafy.com.R
import app.horecafy.com.adapters.ReviewOfferWholesalersDetailsRecyclerViewAdapter
import app.horecafy.com.models.ContactOffer
import app.horecafy.com.models.ReviewOfferItems
import app.horecafy.com.models.ReviewOffersWholesalerDetails
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.adapters.SlidingImage_Adapter
import app.horecafy.com.models.CustomerOffer
import app.horecafy.com.services.CustomerService.getReviewOffersList
import app.horecafy.com.services.OfferService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_customer_review_offer_products.*
import kotlinx.android.synthetic.main.activity_customer_review_offer_wholesalers_details.*
import kotlinx.android.synthetic.main.activity_customer_review_offers_category.*
import java.util.*


class CustomerReviewOfferWholesalersDetailsActivity : AppCompatActivity() {

    companion object {
        val INTENT_REVIEW_OFFERS_LIST = "INTENT_REVIEW_OFFERS_LIST"
        val INTENT_SELECTED_WHOLESALER = "INTENT_SELECTED_WHOLESALER"
        val REQUEST_THANKS = 1
        fun intent(context: Context, selectedItem: ReviewOffersWholesalerDetails, list: ArrayList<ReviewOfferItems>): Intent {
            val intent = Intent(context, CustomerReviewOfferWholesalersDetailsActivity::class.java)
            intent.putExtra(INTENT_SELECTED_WHOLESALER, selectedItem)
            intent.putParcelableArrayListExtra(INTENT_REVIEW_OFFERS_LIST, list)
            return intent
        }
    }
    val wholesalerDetailList: MutableList<ReviewOfferItems> = mutableListOf()
    var mReviewOffersParentList: ArrayList<ReviewOfferItems>? = null
    var mSelectedWholesaler: ReviewOffersWholesalerDetails? = null
    var mAdapter : ReviewOfferWholesalersDetailsRecyclerViewAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_review_offer_wholesalers_details)

        if (mReviewOffersParentList == null) {
            mReviewOffersParentList = intent.getParcelableArrayListExtra(INTENT_REVIEW_OFFERS_LIST)
        }

        if (mSelectedWholesaler == null) {
            mSelectedWholesaler = intent.getSerializableExtra(INTENT_SELECTED_WHOLESALER) as ReviewOffersWholesalerDetails?
        }

        tvWholesalerName.setText(mSelectedWholesaler!!.name)

      //  val wholesalerDetailList: MutableList<ReviewOfferItems> = mutableListOf()

        if (mSelectedWholesaler!!.hiddenId != null) {
            mReviewOffersParentList!!.forEach {
                if (it.WholeSalerHiddenId!!.toString().equals(mSelectedWholesaler!!.hiddenId.toString())) {
                    wholesalerDetailList.add(it)
                }
            }
        }

        // Configure recyclerView
        rvReviewOfferWholesalersDetailList.layoutManager = LinearLayoutManager(this)
        rvReviewOfferWholesalersDetailList.itemAnimator = DefaultItemAnimator()

        // Set the adapter
        mAdapter = ReviewOfferWholesalersDetailsRecyclerViewAdapter(wholesalerDetailList, this)
        rvReviewOfferWholesalersDetailList.adapter = mAdapter

/*
        tvContactDistributor.setOnClickListener { v: View? ->

            UiHelpers.showProgessBar(window, rlProgressBarReviewOfferWholesalersDetails)

            val customerId = AuthService.customer!!.id!!

            val inputModel = ContactOffer(customerId, mSelectedWholesaler!!.id.toString())

            CustomerService.contactOffer(this, inputModel) { status, error ->

                UiHelpers.hideProgessBar(window, rlProgressBarReviewOfferWholesalersDetails)

                if (status) {
                    startActivity(Intent(this, CustomerReviewOffersThanks::class.java))
                }
            }
        }
*/

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==android.R.id.home){
            var intent=intent
            intent.putParcelableArrayListExtra("latestData",mReviewOffersParentList)
            setResult(RESULT_OK,intent)
            Log.e("LatestValue","555   " + mReviewOffersParentList!!.size)
            finish()
            onBackPressed()
        }
        return true
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

    fun OfferContactDistributor(offerHiddenid: Long,pos:Int) {

        UiHelpers.showProgessBar(window, rlProgressBarReviewOfferWholesalersDetails)
        OfferService.accept(this, offerHiddenid) { status, error ->
            UiHelpers.hideProgessBar(window, rlProgressBarReviewOfferWholesalersDetails)
            if (status) {
                mAdapter!!.notifyDataSetChanged()
                val intent = CustomerShareListThanks.intent(this)
                startActivityForResult(intent, CustomerReviewOfferWholesalersDetailsActivity.REQUEST_THANKS)
            } else {
                Toast.makeText(this, "Error desconocido", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun OfferDeclien(OfferID: String,pos:Int) {

        UiHelpers.showProgessBar(window, rlProgressBarReviewOfferWholesalersDetails)
        CustomerService.contactOfferDecline(this, OfferID) { status, error ->
            UiHelpers.hideProgessBar(window, rlProgressBarReviewOfferWholesalersDetails)
            if (status) {
                mAdapter!!.notifyDataSetChanged()
                wholesalerDetailList!!.removeAt(pos)
                for(i in mReviewOffersParentList!!.indices){
                    if (OfferID.equals(mReviewOffersParentList!!.get(i).id)){
                        var delID = mReviewOffersParentList!!.get(i)
                        mReviewOffersParentList!!.removeAt(i)
                        Log.e("LatestValue","OfferID : " +OfferID+ ", origional : "+delID.id)
                    break
                    }
                }
            }
        }

    }

    override fun onBackPressed() {

        var intent=intent
        intent.putParcelableArrayListExtra("latestData",mReviewOffersParentList)
        setResult(RESULT_OK,intent)
        Log.e("LatestValue","555   " + mReviewOffersParentList!!.size)
        finish()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CustomerReviewOfferWholesalersDetailsActivity.REQUEST_THANKS && resultCode == AppCompatActivity.RESULT_CANCELED) {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
