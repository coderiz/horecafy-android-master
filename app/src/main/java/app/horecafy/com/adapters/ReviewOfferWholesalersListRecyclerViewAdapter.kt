package app.horecafy.com.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import app.horecafy.com.R
import app.horecafy.com.activities.customers.CustomerReviewOfferWholesalersActivity
import app.horecafy.com.models.ReviewOffersWholesalerDetails


class ReviewOfferWholesalersListRecyclerViewAdapter(val listItems: MutableList<ReviewOffersWholesalerDetails>?,
                                                    val context: Context)
    : RecyclerView.Adapter<ReviewOfferWholesalersListRecyclerViewAdapter.CategoryViewHolder>() {

    val mContext = context

    // ViewHolder
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val llReviewOfferWholesalerParent = itemView.findViewById<LinearLayout>(R.id.llReviewOfferWholesalerParent)
        val tvDistributorName = itemView.findViewById<TextView>(R.id.tvDistributorName)
        val tvProductsOffered = itemView.findViewById<TextView>(R.id.tvProductsOffered)

        fun bindItem(listItems: ReviewOffersWholesalerDetails, position: Int) {

            tvDistributorName.setText(listItems.name)
            tvProductsOffered.setText(listItems.productsOffered.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recycler_view_item_review_offer_wholesalers,
                        parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {

        if (listItems != null) {

            val item = listItems[position]

            holder?.bindItem(item, position)

            holder!!.llReviewOfferWholesalerParent.setOnClickListener({ v: View? ->

                if (mContext is CustomerReviewOfferWholesalersActivity) {
                    mContext.showDetailScreen(item)
                }
            })
        }
    }

    override fun getItemCount() = listItems?.size ?: 0
}