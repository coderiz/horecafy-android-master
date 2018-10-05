package app.horecafy.com.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import app.horecafy.com.R
import app.horecafy.com.activities.customers.fragments.CustomerAddAvailabilityFragment
import app.horecafy.com.models.CustomerAvailabilityItems


class CustomerAvailabilityListRecyclerViewAdapter(val listItems: MutableList<CustomerAvailabilityItems>?,
                                                  val mFragment: CustomerAddAvailabilityFragment)
    : RecyclerView.Adapter<CustomerAvailabilityListRecyclerViewAdapter.CategoryViewHolder>() {

    val mContext = mFragment

    // ViewHolder
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llCustomerAvailabilityParent = itemView.findViewById<LinearLayout>(R.id.llCustomerAvailabilityParent)
        fun bindItem(listItems: CustomerAvailabilityItems, position: Int) {

            if (listItems.IsAvailable) {
                llCustomerAvailabilityParent.setBackgroundColor(ContextCompat.getColor(mFragment.activity, R.color.colorPrimary))
            } else {

                llCustomerAvailabilityParent.setBackgroundColor(ContextCompat.getColor(mFragment.activity, R.color.dividerColor))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recycler_view_item_customer_availability_list,
                        parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {

        if (listItems != null) {

            val item = listItems[position]

            holder?.bindItem(item, position)

            holder!!.llCustomerAvailabilityParent.setOnClickListener({ v: View? ->

                // Toast.makeText(mFragment.activity, "Selected : " + position + " " + item.Day + "\n Time : " + item.TimeSlot, Toast.LENGTH_SHORT).show()
                if (mContext is CustomerAddAvailabilityFragment) {
                    mContext.refreshList(item, position)
                }
            })
        }
    }

    override fun getItemCount() = listItems?.size ?: 0
}