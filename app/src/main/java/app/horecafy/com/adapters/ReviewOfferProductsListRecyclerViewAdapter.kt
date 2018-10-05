package app.horecafy.com.adapters

import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.horecafy.com.R
import app.horecafy.com.models.ReviewOfferItems
import app.horecafy.com.models.ReviewOffersProductDetails
import java.util.*


class ReviewOfferProductsListRecyclerViewAdapter(val mProductsList: MutableList<ReviewOffersProductDetails>?,
                                                 val mContext: Context,
                                                 val mReviewOffersParentList: ArrayList<ReviewOfferItems>?)
    : RecyclerView.Adapter<ReviewOfferProductsListRecyclerViewAdapter.CategoryViewHolder>() {

    // ViewHolder
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val llReviewOfferProductParent = itemView.findViewById<LinearLayout>(R.id.llReviewOfferProductParent)
        val tvProductName = itemView.findViewById<TextView>(R.id.tvProductName)
        val ivProductCollapse = itemView.findViewById<ImageView>(R.id.ivProductCollapse)
        val ivProductExpand = itemView.findViewById<ImageView>(R.id.ivProductExpand)

        val llProductsDistributorList = itemView.findViewById<LinearLayout>(R.id.llProductsDistributorList)
        val rvReviewOfferProductDistributorsList = itemView.findViewById<RecyclerView>(R.id.rvReviewOfferProductDistributorsList)

        fun bindItem(listItems: ReviewOffersProductDetails) {

            tvProductName.setText(listItems.name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recycler_view_item_review_offer_products,
                        parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {

        if (mProductsList != null) {

            val item = mProductsList[position]

            holder?.bindItem(item)



            holder!!.llReviewOfferProductParent.setOnClickListener({ v: View? ->

                if (holder.ivProductCollapse.visibility == View.VISIBLE) {
                    holder.ivProductCollapse.setVisibility(View.GONE)
                    holder.llProductsDistributorList.setVisibility(View.GONE)
                    holder.ivProductExpand.setVisibility(View.VISIBLE)
                } else {
                    holder.ivProductCollapse.setVisibility(View.VISIBLE)
                    holder.llProductsDistributorList.setVisibility(View.VISIBLE)
                    holder.ivProductExpand.setVisibility(View.GONE)

                    val wholesalerDetailList: MutableList<ReviewOfferItems> = mutableListOf()

                    mReviewOffersParentList!!.forEach {
                        if (it.ProductId!!.toString().equals(item.id)) {
                            wholesalerDetailList.add(it)
                        }
                    }

                    /*wholesalerDetailList.forEach {
                        Log.e("WholeSalerName", "" + it.WholeSalerName)
                    }*/

                    // Configure recyclerView
                    holder.rvReviewOfferProductDistributorsList.layoutManager = LinearLayoutManager(mContext)
                    holder.rvReviewOfferProductDistributorsList.itemAnimator = DefaultItemAnimator()
                    holder.rvReviewOfferProductDistributorsList.setHasFixedSize(true)
                    holder.rvReviewOfferProductDistributorsList.setNestedScrollingEnabled(false)

                    // Set the adapter
                    val mAdapter = ReviewOfferProductDistributorsListRecyclerViewAdapter(wholesalerDetailList, mContext)
                    holder.rvReviewOfferProductDistributorsList.adapter = mAdapter
                }
            })

            if (position == 0) {
                holder.llReviewOfferProductParent.performClick()
            }
        }
    }

    override fun getItemCount() = mProductsList?.size ?: 0
}