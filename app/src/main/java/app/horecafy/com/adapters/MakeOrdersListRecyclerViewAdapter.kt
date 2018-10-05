package app.horecafy.com.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import app.horecafy.com.R
import app.horecafy.com.activities.customers.CustomerCreateYourListsActivity
import app.horecafy.com.activities.customers.CustomerMakeAnOrderActivity
import app.horecafy.com.models.AddProductItems


class MakeOrdersListRecyclerViewAdapter(val listItems: MutableList<AddProductItems>?,
                                        val context: Context)
    : RecyclerView.Adapter<MakeOrdersListRecyclerViewAdapter.CategoryViewHolder>() {

    val mContext = context

    // ViewHolder
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCount = itemView.findViewById<TextView>(R.id.tvCount)
        val etProductName = itemView.findViewById<EditText>(R.id.etProductName)
        val etProductQty = itemView.findViewById<EditText>(R.id.etProductQty)
        val tvRemove = itemView.findViewById<TextView>(R.id.tvRemove)

        @SuppressLint("SetTextI18n")
        fun bindItem(listItems: AddProductItems, position: Int) {

            tvCount.text = (position + 1).toString() + "."
            etProductName.setText(listItems.Name)
            etProductQty.setText(listItems.Quantity)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recycler_view_item_distributors_products_list,
                        parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {

        if (listItems != null) {

            val item = listItems[position]

            holder?.bindItem(item, position)

            holder!!.etProductName.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(p0: Editable?) {

                    if (mContext is CustomerMakeAnOrderActivity) {
                        mContext.addValueToList(position, p0.toString(), holder.etProductQty.getText().toString())
//                        holder.etProductName.setSelection(mList[position].Name.length)
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })

            holder!!.etProductQty.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(p0: Editable?) {

                    if (mContext is CustomerMakeAnOrderActivity) {
                        mContext.addValueToList(position, holder.etProductName.getText().toString(), p0.toString())
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })

            holder!!.tvRemove.setOnClickListener({ v: View? ->

                listItems.removeAt(position)

                if (mContext is CustomerMakeAnOrderActivity) {
                    mContext.refreshList()
                }
            })
        }
    }

    override fun getItemCount() = listItems?.size ?: 0
}