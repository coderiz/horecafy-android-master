package app.horecafy.com.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.R.id.tvContactDistributor
import app.horecafy.com.activities.customers.CustomerReviewOfferProductsActivity
import app.horecafy.com.activities.customers.CustomerReviewOfferWholesalersDetailsActivity
import app.horecafy.com.models.ReviewOfferItems


class ReviewOfferWholesalersDetailsRecyclerViewAdapter(val mList: MutableList<ReviewOfferItems>?,
                                                       val context: Context)
    : RecyclerView.Adapter<ReviewOfferWholesalersDetailsRecyclerViewAdapter.CategoryViewHolder>() {

    // ViewHolder
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvProductName = itemView.findViewById<TextView>(R.id.tvProductName)
        val tvDeniedofferOne = itemView.findViewById<TextView>(R.id.tvDeniedofferOne)
        val tvContactDistributor = itemView.findViewById<TextView>(R.id.tvContactDistributor)
        val tvBrand = itemView.findViewById<TextView>(R.id.tvBrand)
        val tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)
        val tvFormat = itemView.findViewById<TextView>(R.id.tvFormat)
        val tvComments = itemView.findViewById<TextView>(R.id.tvComments)
        val tvPreviewimg = itemView.findViewById<TextView>(R.id.tvPreviewimg)
        val tvPreviewvideo = itemView.findViewById<TextView>(R.id.tvPreviewvideo)

        fun bindItem(listItems: ReviewOfferItems, position: Int) {

            tvProductName.setText(listItems.ProductName)
            tvBrand.setText(listItems.brand)
            tvPrice.setText(listItems.offerPrice.toString())
            tvFormat.setText(listItems.fomat)
            tvComments.setText(listItems.comments)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recycler_view_item_review_offer_wholesalers_detail,
                        parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {

        if (mList != null) {

            val item = mList[position]

            holder?.bindItem(item, position)

           // Log.e("REVIEW", "IMAGES : : " + item.images)
           // Log.e("REVIEW", "VIDEO : : " + item.video)

            if (!item.images.equals("") && !item.video.equals("")) {
                holder!!.tvPreviewimg.visibility = View.VISIBLE
                holder!!.tvPreviewvideo.visibility = View.VISIBLE
            } else if (!item.images.equals("") && item.video.equals("")) {
                holder!!.tvPreviewimg.visibility = View.VISIBLE
                holder!!.tvPreviewvideo.visibility = View.GONE
            } else if (item.images.equals("") && !item.video.equals("")) {
                holder!!.tvPreviewimg.visibility = View.GONE
                holder!!.tvPreviewvideo.visibility = View.VISIBLE
            } else {
                holder!!.tvPreviewimg.visibility = View.GONE
                holder!!.tvPreviewvideo.visibility = View.GONE
            }

            if(item.approvedByCustomer.equals("")){
                holder!!.tvContactDistributor.visibility = View.VISIBLE
            }else{
                holder!!.tvContactDistributor.visibility = View.GONE
            }


            holder!!.tvPreviewvideo.setOnClickListener(View.OnClickListener {
                if (context is CustomerReviewOfferWholesalersDetailsActivity) {
                    context.VideoPreview(item.video)
                }
            })

            holder!!.tvPreviewimg.setOnClickListener(View.OnClickListener {
                if (context is CustomerReviewOfferWholesalersDetailsActivity) {
                    context.ImagePreview(item.images)
                }
            })

            holder!!.tvDeniedofferOne.setOnClickListener { v: View? ->

                if (context is CustomerReviewOfferWholesalersDetailsActivity) {
                    context.OfferDeclien(item.id.toString(), position)
                }

            }
            holder!!.tvContactDistributor.setOnClickListener { v: View? ->

                if (context is CustomerReviewOfferWholesalersDetailsActivity) {
                    context.OfferContactDistributor(item.hiddenId!!, position)
                }

            }
        }
    }

    override fun getItemCount() = mList?.size ?: 0
}