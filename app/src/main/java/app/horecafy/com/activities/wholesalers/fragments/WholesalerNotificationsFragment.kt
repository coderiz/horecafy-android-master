package app.horecafy.com.activities.wholesalers.fragments


import android.app.Dialog
import android.content.DialogInterface
import android.content.SearchRecentSuggestionsProvider
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import app.horecafy.com.FullScreenMediaController
import app.horecafy.com.R
import app.horecafy.com.adapters.SlidingImage_Adapter
import app.horecafy.com.adapters.WholesalerNotificationsRecyclerViewAdapter
import app.horecafy.com.models.NotificationsDetails
import app.horecafy.com.models.TimeSlotItems
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.WholesalerService
import app.horecafy.com.services.WholesalerService.selectCustomerAvailabilityTimeslot
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.fragment_wholesaler_notifications.*
import java.util.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class WholesalerNotificationsFragment : Fragment() {

    companion object {
        fun newInstance(): WholesalerNotificationsFragment {
            val fragment = WholesalerNotificationsFragment()
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
        return inflater.inflate(R.layout.fragment_wholesaler_notifications, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure recyclerView
        rvWholesalerNotificationsList.layoutManager = LinearLayoutManager(activity)
        rvWholesalerNotificationsList.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?

        getWholesalerNotificationsList()
    }

    private fun getWholesalerNotificationsList() {

        UiHelpers.showProgessBar(activity.window, rlProgressBarWholesalerNotifications)

        val wholesalerId = AuthService.wholesaler!!.id!!

        WholesalerService.getWholesalerNotifications(activity, wholesalerId) { status, data, error ->

            UiHelpers.hideProgessBar(activity.window, rlProgressBarWholesalerNotifications)

            if (status && data != null) {

                mNotificationslist = data.toMutableList()

                if (mNotificationslist.size > 0) {

                    rvWholesalerNotificationsList.setVisibility(View.VISIBLE)
                    rlNoWholesalerNotificationsAvailable.setVisibility(View.GONE)

                    // Set the adapter
                    val mAdapter = WholesalerNotificationsRecyclerViewAdapter(mNotificationslist, this)
                    rvWholesalerNotificationsList.adapter = mAdapter
                } else {

                    rvWholesalerNotificationsList.setVisibility(View.GONE)
                    rlNoWholesalerNotificationsAvailable.setVisibility(View.VISIBLE)
                }
            } else {
                Toast.makeText(activity, "" + Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getCustomerAvailabilityList(customerId: String, notificationId: String) {

        UiHelpers.showProgessBar(activity.window, rlProgressBarWholesalerNotifications)

        WholesalerService.getCustomerAvailability(activity, customerId) { status, data, error ->

            UiHelpers.hideProgessBar(activity.window, rlProgressBarWholesalerNotifications)

            if (status) {

//                Log.e("Availability Response", "" + data)
                val strAvailability = data.availability
                setTimeAsPerAvailability(notificationId, strAvailability.toString())
            } else {
                Toast.makeText(activity, "" + Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setTimeAsPerAvailability(notificationId: String, strAvailability: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_wholesaler_set_availability_time, null)
        dialogBuilder.setView(dialogView)

        val rlProgressBarSetAvailability = dialogView.findViewById<View>(R.id.rlProgressBarSetAvailability) as RelativeLayout
        val acsAvailableTimeSlot = dialogView.findViewById<View>(R.id.acsAvailableTimeSlot) as AppCompatSpinner
        val acsAvailableDates = dialogView.findViewById<View>(R.id.acsAvailableDates) as AppCompatSpinner
        val tvNoTimeslotsAvailable = dialogView.findViewById<View>(R.id.tvNoTimeslotsAvailable) as TextView
        var strSelectedValue: String = ""
        var strSelecteddate: String = ""
        Log.e("Availability ", "454334  " + strAvailability)
        if (!TextUtils.isEmpty(strAvailability) && !strAvailability.equals("null")) {
            tvNoTimeslotsAvailable.setVisibility(View.GONE)
            acsAvailableTimeSlot.setVisibility(View.VISIBLE)
            acsAvailableDates.setVisibility(View.VISIBLE)

            Log.e("Availability ", "" + strAvailability)

            val result: List<String> = strAvailability!!.split(",").map { it.trim() }
            var resultTimesloteArray: ArrayList<String> = ArrayList()
            var filterR: ArrayList<String> = ArrayList()

            for (i in result.indices) {
                //result.get(i).substring(0,3)
                var threeS: String
                var threeE: String = ""

                if(result.get(i).contains("Mier") || result.get(i).contains("Vier")){
                    threeS = result.get(i).substring(0, 4)
                }else{
                    threeS = result.get(i).substring(0, 3)
                }
                if (threeS.equals("Lun")) {
                    threeE = "Mon"
                } else if (threeS.equals("Mar")) {
                    threeE = "Tue"
                } else if (threeS.equals("Mier")) {
                    threeE = "Wed"
                } else if (threeS.equals("Jue")) {
                    threeE = "Thu"
                } else if (threeS.equals("Vier")) {
                    threeE = "Fri"
                } else if (threeS.equals("Sab")) {
                    threeE = "Sat"
                } else if (threeS.equals("Dom")) {
                    threeE = "Sun"
                }
                if (!filterR.contains(threeE)) {
                    filterR.add(threeE)
                }
            }
            Log.e("AAAAAAAAAAAAAAA", "Date filter: " + filterR)



            val cal = Calendar.getInstance()
            val cal1 = Calendar.getInstance()
            cal1.add(Calendar.DATE, 0)
            cal.add(Calendar.DATE, 31)
            var d1 = cal1.getTime()
            var d = cal.getTime()


            val resultD: List<String> = getDatesBetweenUsingJava7(d1, d, filterR)
            val arrayAdapterD = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, resultD)

            acsAvailableDates.adapter = arrayAdapterD as SpinnerAdapter?

            acsAvailableDates.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    resultTimesloteArray.clear()
                    strSelecteddate = parent.selectedItem.toString().trim()
                    for(i in result.indices){
                        if(strSelecteddate.length>=3 && result.get(i).length>=3){
                            Log.e("strSelecteddate", " "+strSelecteddate +" result.get(i) : "+result.get(i))
                            if (strSelecteddate.substring(0,3).contains(result.get(i).substring(0,3))){
                                resultTimesloteArray.add(result.get(i))
                            }
                        }
                    }
                    val arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, resultTimesloteArray)
                    acsAvailableTimeSlot.adapter = arrayAdapter as SpinnerAdapter?
//                Toast.makeText(activity, "" + strSelectedValue, Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }


            acsAvailableTimeSlot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    strSelectedValue = parent.selectedItem.toString().trim()

//                Toast.makeText(activity, "" + strSelectedValue, Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        } else {

            tvNoTimeslotsAvailable.setVisibility(View.VISIBLE)
            acsAvailableTimeSlot.setVisibility(View.GONE)
            acsAvailableDates.setVisibility(View.GONE)

        }
        dialogBuilder.setTitle("Seleccionar el intervalo de tiempo disponible")

        if (!strAvailability.isEmpty()
                && strAvailability.trim().length > 0 ) {
            dialogBuilder.setPositiveButton("Enviar", { dialog, whichButton ->

                //            Toast.makeText(activity, "Submit", Toast.LENGTH_SHORT).show()

                selectCustomerAvailabilityTimeslot(notificationId, strSelectedValue,strSelecteddate.substring(4), rlProgressBarSetAvailability, dialog)
//            dialog.dismiss()
            })
        }

        dialogBuilder.setNegativeButton("Cancelar", { dialog, whichButton ->
            val c = Calendar.getInstance().time
/*
            val df = SimpleDateFormat("dd-MMM-yyyy")
            val formattedDate = df.format(c)
            var date1 = SimpleDateFormat("dd/MM/yyyy").parse(formattedDate)
           */


            //dialog.dismiss()
        })

        val b = dialogBuilder.create()
        b.setCanceledOnTouchOutside(false)
        b.show()
    }

    fun getDatesBetweenUsingJava7(startDate: Date, endDate: Date, filterArray: List<String>): List<String> {
        var datesInRange: ArrayList<String> = ArrayList()
        val calendar = GregorianCalendar()
        calendar.time = startDate

        val endCalendar = GregorianCalendar()
        endCalendar.time = endDate

        while (calendar.before(endCalendar)) {
            val result = calendar.time

            if (filterArray.contains(result.toString().substring(0, 3))) {

                val df = SimpleDateFormat("EEE dd-MM-yyyy")
                var finalR: String = ""
                if (df.format(result).contains("Mon")) {
                    finalR = df.format(result).replace("Mon","Lun")
                }else if (df.format(result).contains("Tue")) {
                    finalR = df.format(result).replace("Tue","Mar")
                }else if (df.format(result).contains("Wed")) {
                    finalR = df.format(result).replace("Wed","Mier")
                }else if (df.format(result).contains("Thu")) {
                    finalR = df.format(result).replace("Thu","Jue")
                }else if (df.format(result).contains("Fri")) {
                    finalR = df.format(result).replace("Fri","Vier")
                }else if (df.format(result).contains("Sat")) {
                    finalR = df.format(result).replace("Sat","Sab")
                }else if (df.format(result).contains("Sun")) {
                    finalR = df.format(result).replace("Sun","Dom")
                }
                //val finalR: String = df.format(result)

                datesInRange.add(finalR)
            }
            calendar.add(Calendar.DATE, 1)
        }
        return datesInRange
    }

    private fun selectCustomerAvailabilityTimeslot(notificationId: String, strSelectedValue: String,strSelectedDate: String,
                                                   rlProgressBarSetAvailability: RelativeLayout, dialog: DialogInterface) {

        UiHelpers.showProgessBar(activity.window, rlProgressBarSetAvailability)

        val inputStrTimeslot = TimeSlotItems(strSelectedValue,Constants.FlipDateFormatinvers(strSelectedDate))

        WholesalerService.selectCustomerAvailabilityTimeslot(activity, notificationId, inputStrTimeslot) { status, error ->

            UiHelpers.hideProgessBar(activity.window, rlProgressBarSetAvailability)

            if (status) {

                dialog.dismiss()

                getWholesalerNotificationsList()
            } else {
                Toast.makeText(activity, "" + Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun VideoPopup(VideoURL: String) {
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
}