package app.horecafy.com.activities.customers.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.adapters.CustomerAvailabilityListRecyclerViewAdapter
import app.horecafy.com.adapters.CustomerAvailabilityListTitleRecyclerViewAdapter
import app.horecafy.com.models.Availability
import app.horecafy.com.models.CustomerAvailabilityItems
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.fragment_customer_add_availability.*


class CustomerAddAvailabilityFragment : Fragment() {

    var mUserAvailabilityList: MutableList<CustomerAvailabilityItems> = mutableListOf()
    var mAvailabilityListFromServer: MutableList<CustomerAvailabilityItems> = mutableListOf()

    companion object {
        fun newInstance(): CustomerAddAvailabilityFragment {
            val fragment = CustomerAddAvailabilityFragment()
            /*val bundle = Bundle()
            bundle.putString("Text", text)
            fragment.arguments = bundle*/
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_add_availability, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure recyclerView for Days
        rvDays.layoutManager = GridLayoutManager(activity, 7)
        rvDays.itemAnimator = DefaultItemAnimator()
        rvDays.setHasFixedSize(true)

        val daysList: MutableList<String> = mutableListOf()

        daysList.add(Constants.MONDAY)
        daysList.add(Constants.TUESDAY)
        daysList.add(Constants.WEDNESDAY)
        daysList.add(Constants.THURSDAY)
        daysList.add(Constants.FRIDAY)
        daysList.add(Constants.SATURDAY)
        daysList.add(Constants.SUNDAY)

        // Set the adapter
        val daysAdapter = CustomerAvailabilityListTitleRecyclerViewAdapter(daysList)
        rvDays.adapter = daysAdapter

        // Configure recyclerView for Time Slots
        rvTimeSlots.layoutManager = LinearLayoutManager(activity)
        rvTimeSlots.itemAnimator = DefaultItemAnimator()
        rvTimeSlots.setHasFixedSize(true)
        rvTimeSlots.setNestedScrollingEnabled(false)

        val timeSlotsList: MutableList<String> = mutableListOf()

//        timeSlotsList.add(Constants.SLOT_ONE)
//        timeSlotsList.add(Constants.SLOT_TWO)
//        timeSlotsList.add(Constants.SLOT_THREE)
//        timeSlotsList.add(Constants.SLOT_FOUR)
//        timeSlotsList.add(Constants.SLOT_FIVE)
        timeSlotsList.add(Constants.SLOT_SIX)
        timeSlotsList.add(Constants.SLOT_SEVEN)
        timeSlotsList.add(Constants.SLOT_EIGHT)
        timeSlotsList.add(Constants.SLOT_NINE)
        timeSlotsList.add(Constants.SLOT_TEN)
        timeSlotsList.add(Constants.SLOT_ELEVEN)
        timeSlotsList.add(Constants.SLOT_TWELVE)

        // Set the adapter
        val timeSlotsAdapter = CustomerAvailabilityListTitleRecyclerViewAdapter(timeSlotsList)
        rvTimeSlots.adapter = timeSlotsAdapter

        // Configure recyclerView for Complete Grid
        rvCustomerAvailability.layoutManager = GridLayoutManager(activity, 7)
        rvCustomerAvailability.itemAnimator = DefaultItemAnimator()
        rvCustomerAvailability.setHasFixedSize(true)
        rvCustomerAvailability.setNestedScrollingEnabled(false)

        defaultAvailabilityList()

        btnSubmitUpdatedAvailability.setOnClickListener({ v: View? ->

            var strFinalValue: String = ""

            for (i in mUserAvailabilityList) {
                if (i.IsAvailable) {

                    if (strFinalValue.equals(Constants.CONSTANT_EMPTY_STRING)) {

                        strFinalValue = i.Day + " " + i.TimeSlot
                    } else {

                        strFinalValue = strFinalValue + "," + i.Day + " " + i.TimeSlot
                    }
                }
            }

            submitAvailabilty(strFinalValue)
        })

        getAvailabiltyList()
    }

    private fun defaultAvailabilityList() {

        // Slot 00-02
        var item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_ONE, false)
        /*mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_ONE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_ONE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_ONE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_ONE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_ONE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_ONE, false)
        mUserAvailabilityList.add(item)

        // Slot 02-04
        item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_TWO, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_TWO, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_TWO, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_TWO, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_TWO, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_TWO, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_TWO, false)
        mUserAvailabilityList.add(item)

        // Slot 04-06
        item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_THREE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_THREE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_THREE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_THREE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_THREE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_THREE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_THREE, false)
        mUserAvailabilityList.add(item)

        // Slot 06-08
        item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_FOUR, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_FOUR, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_FOUR, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_FOUR, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_FOUR, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_FOUR, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_FOUR, false)
        mUserAvailabilityList.add(item)

        // Slot 08-10
        item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_FIVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_FIVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_FIVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_FIVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_FIVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_FIVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_FIVE, false)
        mUserAvailabilityList.add(item)*/

        // Slot 10-12
        item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_SIX, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_SIX, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_SIX, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_SIX, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_SIX, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_SIX, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_SIX, false)
        mUserAvailabilityList.add(item)

        //Slot 12-14
        item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_SEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_SEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_SEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_SEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_SEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_SEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_SEVEN, false)
        mUserAvailabilityList.add(item)

        // Slot 14-16
        item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_EIGHT, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_EIGHT, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_EIGHT, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_EIGHT, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_EIGHT, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_EIGHT, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_EIGHT, false)
        mUserAvailabilityList.add(item)

        // Slot 16-18
        item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_NINE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_NINE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_NINE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_NINE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_NINE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_NINE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_NINE, false)
        mUserAvailabilityList.add(item)

        // Slot 18-20
        item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_TEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_TEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_TEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_TEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_TEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_TEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_TEN, false)
        mUserAvailabilityList.add(item)

        //Slot 20-22
        item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_ELEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_ELEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_ELEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_ELEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_ELEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_ELEVEN, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_ELEVEN, false)
        mUserAvailabilityList.add(item)

        // Slot 22-00
        item = CustomerAvailabilityItems(Constants.MONDAY, Constants.SLOT_TWELVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.TUESDAY, Constants.SLOT_TWELVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.WEDNESDAY, Constants.SLOT_TWELVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.THURSDAY, Constants.SLOT_TWELVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.FRIDAY, Constants.SLOT_TWELVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SATURDAY, Constants.SLOT_TWELVE, false)
        mUserAvailabilityList.add(item)

        item = CustomerAvailabilityItems(Constants.SUNDAY, Constants.SLOT_TWELVE, false)
        mUserAvailabilityList.add(item)

        setAdapter()
    }

    private fun setAdapter() {

        // Set the adapter
        val mAdapter = CustomerAvailabilityListRecyclerViewAdapter(mUserAvailabilityList, this)
        rvCustomerAvailability.adapter = mAdapter
    }

    fun refreshList(item: CustomerAvailabilityItems, position: Int) {

        mUserAvailabilityList.removeAt(position)

        var updatedItem: CustomerAvailabilityItems
        if (item.IsAvailable) {
            updatedItem = CustomerAvailabilityItems(item.Day, item.TimeSlot, false)
        } else {
            updatedItem = CustomerAvailabilityItems(item.Day, item.TimeSlot, true)
        }
        mUserAvailabilityList.add(position, updatedItem)

        setAdapter()
    }

    private fun getAvailabiltyList() {

        UiHelpers.showProgessBar(activity.window, rlProgressBarCustomerAvailability)

        val customerId = AuthService.customer!!.id!!

        CustomerService.getCustomerAvailability(activity, customerId) { status, data, error ->

            UiHelpers.hideProgessBar(activity.window, rlProgressBarCustomerAvailability)

            if (status) {

//                Log.e("Availability Response", "" + data)

                val strAvailability = data.availability

                if (!strAvailability.isNullOrEmpty()) {

//                    Log.e("Availability ", "" + strAvailability)
                    val result: List<String> = strAvailability!!.split(",").map { it.trim() }

                    result.forEachIndexed { index, s ->
                        val strDay: String
                        val strTimeSlot: String
                        if(s.contains("Mier") || s.contains("Vier")){
                            strDay = s.substring(0, 4).trim()
                            strTimeSlot = s.substring(5, 18).trim()
                        }else{
                            strDay = s.substring(0, 3).trim()
                            strTimeSlot = s.substring(4, 17).trim()
                        }
                        val item = CustomerAvailabilityItems(strDay, strTimeSlot, false)
                        mAvailabilityListFromServer.add(item)
                    }

                    Log.e("Server List Size ", "" + mAvailabilityListFromServer.size)

                    val postionList: MutableList<Int> = mutableListOf()

                    mUserAvailabilityList.forEachIndexed { index, customerAvailabilityItems ->

                        val dDay = customerAvailabilityItems.Day
                        val dTime = customerAvailabilityItems.TimeSlot
                        val dAvailable = customerAvailabilityItems.IsAvailable

                        mAvailabilityListFromServer.forEach {

                            if (dDay.equals(it.Day)
                                    && dTime.equals(it.TimeSlot)
                                    && dAvailable.equals(it.IsAvailable)) {

//                                Log.e("Contains Item ", "" + index)
                                if (!postionList.contains(index)) {
                                    postionList.add(index)
                                }
                                return@forEach
                            }
                        }
                    }

                    mUserAvailabilityList.clear()

                    defaultAvailabilityList()

                    if (postionList.size > 0) {
                        postionList.forEach {

                            val day = mUserAvailabilityList[it].Day
                            val time = mUserAvailabilityList[it].TimeSlot

                            val item = CustomerAvailabilityItems(day, time, true)

                            mUserAvailabilityList.removeAt(it)
                            mUserAvailabilityList.add(it, item)
                        }
                    }

                    Log.e("Avail Before List Size ", "" + mUserAvailabilityList.size)

                    setAdapter()
                }

            } else {
                Toast.makeText(activity, "" + Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitAvailabilty(strFinalValue: String) {

        UiHelpers.showProgessBar(activity.window, rlProgressBarCustomerAvailability)

        val item = Availability(strFinalValue)
        val customerId = AuthService.customer!!.id!!

        CustomerService.submitCustomerAvailability(activity, customerId, item) { status, error ->

            UiHelpers.hideProgessBar(activity.window, rlProgressBarCustomerAvailability)

            if (status) {
//                (context as CustomerCommercialVisitsActivity).setupViewPager(vpCustomerCommercialVisits)
                Toast.makeText(activity, "Su disponibilidad está actualizada.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "" + Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
            }
        }
    }
}