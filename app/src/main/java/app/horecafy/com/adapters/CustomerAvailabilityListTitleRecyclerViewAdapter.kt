package app.horecafy.com.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.horecafy.com.R


class CustomerAvailabilityListTitleRecyclerViewAdapter(val listItems: MutableList<String>?)
    : RecyclerView.Adapter<CustomerAvailabilityListTitleRecyclerViewAdapter.CategoryViewHolder>() {

    // ViewHolder
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)

        fun bindItem(listItems: String, position: Int) {

            tvTitle.setText(listItems)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recycler_view_item_customer_availability_list_title,
                        parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {

        if (listItems != null) {

            val item = listItems[position]

            holder?.bindItem(item, position)
        }
    }

    override fun getItemCount() = listItems?.size ?: 0
}