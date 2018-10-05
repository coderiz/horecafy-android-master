package app.horecafy.com.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import app.horecafy.com.R
import app.horecafy.com.activities.customers.fragments.CustomerNotificationsFragment
import app.horecafy.com.activities.wholesalers.fragments.WholesalerNotificationsFragment
import app.horecafy.com.models.NotificationsDetails
import app.horecafy.com.util.Constants
import app.horecafy.com.util.Constants.Companion.FlipDateFormat


class WholesalerNotificationsRecyclerViewAdapter(val mNotificationsList: MutableList<NotificationsDetails>?,
                                                 val mFragment: WholesalerNotificationsFragment)
    : RecyclerView.Adapter<WholesalerNotificationsRecyclerViewAdapter.CategoryViewHolder>() {

    // ViewHolder
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRestaurantsName = itemView.findViewById<TextView>(R.id.tvRestaurantsName)
        val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        val btnSetTime = itemView.findViewById<Button>(R.id.btnSetTime)
        val tvSelectedTime = itemView.findViewById<TextView>(R.id.tvSelectedTime)
        val tvAccepted = itemView.findViewById<TextView>(R.id.tvAccepted)
        val tvPreviewimg = itemView.findViewById<TextView>(R.id.tvPreviewimg)
        val tvPreviewvideo = itemView.findViewById<TextView>(R.id.tvPreviewvideo)
        fun bindItem(listItems: NotificationsDetails, position: Int) {

            tvRestaurantsName.text = listItems.Customer!!.name
            tvDescription.text = listItems.comments
            if (!listItems.timeslot.isNullOrEmpty()) {
                var inputDateStr: String = listItems.visitDate!!.substring(0,10)
                tvSelectedTime.setText(FlipDateFormat(inputDateStr) +" "+listItems.timeslot!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recycler_view_item_wholesaler_notifications_layout,
                        parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {

        if (mNotificationsList != null) {

            val item = mNotificationsList[position]

            holder?.bindItem(item, position)

            if (mFragment is WholesalerNotificationsFragment) {
                if (!item.images.equals("") && !item.video.equals("")){
                    holder!!.tvPreviewimg.visibility = View.VISIBLE
                    holder!!.tvPreviewvideo.visibility = View.VISIBLE
                } else if (item.images.equals("") && !item.video.equals("")) {
                    holder!!.tvPreviewimg.visibility = View.GONE
                    holder!!.tvPreviewvideo.visibility = View.VISIBLE

                } else if (!item.images.equals("") && item.video.equals("")) {
                    holder!!.tvPreviewimg.visibility = View.VISIBLE
                    holder!!.tvPreviewvideo.visibility = View.GONE
                } else {
                    holder!!.tvPreviewimg.visibility = View.GONE
                    holder!!.tvPreviewvideo.visibility = View.GONE

                }
            }

            holder!!.tvPreviewimg.setOnClickListener(View.OnClickListener {
                Log.e("VIDEO","ImageURL : : "+item.images!!)
                mFragment.ImagePopup(item.images!!)
            })

            holder!!.tvPreviewvideo.setOnClickListener(View.OnClickListener {
                Log.e("VIDEO","VideoURL : : "+item.video!!)
                mFragment.VideoPopup(item.video!!)
            })

            if (item.timeslot.isNullOrEmpty()) {

                holder!!.btnSetTime.setVisibility(View.VISIBLE)
                holder!!.tvSelectedTime.setVisibility(View.GONE)
            } else {

                holder!!.btnSetTime.setVisibility(View.GONE)
                holder!!.tvSelectedTime.setVisibility(View.VISIBLE)
            }

            holder!!.btnSetTime.setOnClickListener({ v: View? ->

                if (mFragment is WholesalerNotificationsFragment) {
//                    mFragment.setTimeAsPerAvailability(item.Customer!!.id.toString())

                    mFragment.getCustomerAvailabilityList(item.Customer!!.id.toString(), item.id.toString())
                }
            })
        }
    }

    override fun getItemCount() = mNotificationsList?.size ?: 0
}