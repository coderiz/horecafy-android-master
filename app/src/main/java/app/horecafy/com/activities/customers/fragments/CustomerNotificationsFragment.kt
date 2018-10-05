package app.horecafy.com.activities.customers.fragments


import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.*
import app.horecafy.com.FullScreenMediaController
import app.horecafy.com.R
import app.horecafy.com.adapters.CustomerNotificationsRecyclerViewAdapter
import app.horecafy.com.models.NotificationsDetails
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.fragment_customer_notifications.*
import app.horecafy.com.adapters.SlidingImage_Adapter


class CustomerNotificationsFragment : Fragment() {

    companion object {
        fun newInstance(): CustomerNotificationsFragment {
            val fragment = CustomerNotificationsFragment()
            /*val bundle = Bundle()
            bundle.putString("Text", text)
            fragment.arguments = bundle*/
            return fragment
        }
    }

    var mNotificationslist: MutableList<NotificationsDetails> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_notifications, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure recyclerView
        rvCustomerNotificationsList.layoutManager = LinearLayoutManager(activity)
        rvCustomerNotificationsList.itemAnimator = DefaultItemAnimator()

        getCustomersNotificationsList()
    }

    private fun getCustomersNotificationsList() {

        UiHelpers.showProgessBar(activity.window, rlProgressBarCustomersNotifications)

        val customerId = AuthService.customer!!.id!!

        CustomerService.getCustomersNotifications(activity, customerId) { status, data, error ->

            UiHelpers.hideProgessBar(activity.window, rlProgressBarCustomersNotifications)

            if (status) {

                mNotificationslist = data.toMutableList()

                if (mNotificationslist.size > 0) {

                    rvCustomerNotificationsList.setVisibility(View.VISIBLE)
                    rlNoNotificationsAvailable.setVisibility(View.GONE)

                    // Set the adapter
                    val mAdapter = CustomerNotificationsRecyclerViewAdapter(mNotificationslist, this)
                    rvCustomerNotificationsList.adapter = mAdapter
                } else {
                    rvCustomerNotificationsList.setVisibility(View.GONE)
                    rlNoNotificationsAvailable.setVisibility(View.VISIBLE)
                }
            } else {
                Toast.makeText(activity, "" + Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun acceptNotification(notificationId: String) {

        CustomerService.acceptNotification(activity, notificationId) { status, error ->

            UiHelpers.hideProgessBar(activity.window, rlProgressBarCustomersNotifications)

            if (status) {

                getCustomersNotificationsList()
            } else {
                Toast.makeText(activity, "" + Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun rejectNotification(notificationId: String) {

        CustomerService.rejectNotification(activity, notificationId) { status, error ->

            UiHelpers.hideProgessBar(activity.window, rlProgressBarCustomersNotifications)

            if (status) {

                getCustomersNotificationsList()
            } else {
                Toast.makeText(activity, "" + Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun VideoPopup(VideoURL: String) {
      /*  Log.e("VIDEO","VideoURL : : "+VideoURL)
        val intent = Intent(activity, FullScreenVideoActivity::class.java)
        intent.putExtra("URL",VideoURL)
        startActivity(intent)*/
        var mediaController: MediaController? = null
        val dialogS = Dialog(activity)
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
        val videoUri = Uri.parse(getString(R.string.ImageBaseURL) + VideoURL)

        Log.e("LLLLLLLL", "videoUri : : " + videoUri.toString() + " : " + R.string.IMAGE_URL + VideoURL)
        videoView.setVideoURI(videoUri)

        mediaController = FullScreenMediaController(activity)
        mediaController.setAnchorView(videoView)

        videoView.setMediaController(mediaController)
        videoView.start()
        tv_g_cancel.setOnClickListener { dialogS.dismiss() }

        dialogS.show()
    }

    fun ImagePopup(ImageURL: String) {

        val ImagesArray: List<String> = ImageURL!!.split(",").map { it.trim() }
        Log.e("IMAGE","ImageURL : : "+ImagesArray.toString())

      //  ImagePagerAdapter(activity,result)
        val mPager: ViewPager
        var currentPage = 0
        val dialogS = Dialog(activity)
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
        mPager.adapter = SlidingImage_Adapter(activity, ImagesArray)
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

   /* private fun VideoPopup() {
        val dialogS = Dialog(activity)
        dialogS.setContentView(R.layout.popup_video_show)

        val videoView = dialogS.findViewById(R.id.videoView) as VideoView
        val tv_g_cancel = dialogS.findViewById(R.id.tv_close) as TextView

        tv_g_cancel.setOnClickListener { dialogS.dismiss() }

        dialogS.show()
    }*/

}