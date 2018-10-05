package app.horecafy.com.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.horecafy.com.OnClickWithCheckListener
import app.horecafy.com.R
import app.horecafy.com.activities.customers.CustomerCreateListCategoryActivity
import app.horecafy.com.activities.wholesalers.WholesalerCreateListCategoryActivity
import app.horecafy.com.models.Category
import com.squareup.picasso.Picasso


class CategoryRecyclerViewAdapter(val mContext: Context, val categories: MutableList<Category>?, isType: Int) : RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder>() {
    var isSelect = isType

    var onClickListener: View.OnClickListener? = null
    var onClickWithCheckListener: OnClickWithCheckListener? = null

    fun onClickPositionListener(onClickWithCheckListener: OnClickWithCheckListener) {
        this.onClickWithCheckListener = onClickWithCheckListener
    }

    // ViewHolder
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.image)
        val name = itemView.findViewById<TextView>(R.id.text)
        val tv_categories_check = itemView.findViewById<CheckBox>(R.id.tv_categories_check)
        val totalFamiliesCount = itemView.findViewById<TextView>(R.id.tv_total_families_count)

        fun bindDish(category: Category) {
            if (category.image.equals("First")) {
                Picasso.with(itemView.context).load(R.drawable.ic_create_your_lists).noFade().resize(150,150).into(image)
                totalFamiliesCount.visibility = View.GONE
                tv_categories_check.visibility = View.GONE
            } else {
                val uri = "${itemView.context.getString(R.string.IMAGE_URL)}${category.image}"
                Picasso.with(itemView.context).load(uri).noFade().resize(150,150).into(image)
                if (mContext is CustomerCreateListCategoryActivity) {
                    tv_categories_check.visibility = View.GONE
                 //   totalFamiliesCount.visibility = View.VISIBLE
                } else if (mContext is WholesalerCreateListCategoryActivity) {

                    if (isSelect == 0){
                        tv_categories_check.visibility = View.GONE
                    }else if (isSelect == 1){
                        tv_categories_check.visibility = View.VISIBLE
                    }
                //    totalFamiliesCount.visibility = View.VISIBLE
                } else {
                    tv_categories_check.visibility = View.GONE
                    totalFamiliesCount.visibility = View.GONE
                }
            }
            if(category.totalSelectedFamilies >0){
                tv_categories_check.isChecked = true
            }else if(category.totalSelectedFamilies == 0){
                tv_categories_check.isChecked = false
            }else{
                tv_categories_check.isChecked = false
            }

            if (category.familyCount == 0) {
                name.text = category.name
            } else {
                name.text = "${category.familyCount} solicitudes"
            }

            totalFamiliesCount.text = category.totalFamilies.toString()

            tv_categories_check.setOnCheckedChangeListener {buttonView, isChecked ->

                if (isChecked){
                    onClickWithCheckListener!!.onClick(position, true)
                    Log.e("AAAAAAAAAAA", "Checked : : "+position)
                }else{
                    onClickWithCheckListener!!.onClick(position, false)
                    Log.e("AAAAAAAAAAA", " UnChecked : : "+position)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.recycler_view_item_category,
                parent, false)
        view.setOnClickListener(onClickListener)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {
        if (categories != null) {
            val category = categories[position]
            if (category.id != -1) {
                holder?.bindDish(category)
            }
        }


    }

    override fun getItemCount() = categories?.size ?: 0
}