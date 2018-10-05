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
import app.horecafy.com.models.CreateYourListItems


class CreatedListRecyclerViewAdapter(val yourListItems: MutableList<CreateYourListItems>?,
                                     val context: Context)
    : RecyclerView.Adapter<CreatedListRecyclerViewAdapter.CategoryViewHolder>() {

    val mContext = context

    // ViewHolder
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCount = itemView.findViewById<TextView>(R.id.tvCount)
        val etItemValue = itemView.findViewById<EditText>(R.id.etItemValue)
        val tvRemove = itemView.findViewById<TextView>(R.id.tvRemove)

        @SuppressLint("SetTextI18n")
        fun bindItem(listItems: CreateYourListItems, position: Int) {

            tvCount.text = (position + 1).toString() + "."
            etItemValue.setText(listItems.value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recycler_view_item_create_your_lists,
                        parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {

        if (yourListItems != null) {

            val item = yourListItems[position]

            holder?.bindItem(item, position)

            holder!!.etItemValue.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(p0: Editable?) {

                    if (mContext is CustomerCreateYourListsActivity) {
                        mContext.addValueToList(position, p0.toString())

                        holder.etItemValue.setSelection(yourListItems[position].value.length)
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })

            holder!!.tvRemove.setOnClickListener({ v: View? ->

                yourListItems.removeAt(position)

                if (mContext is CustomerCreateYourListsActivity) {
                    mContext.refreshList()
                }
            })
        }
    }

    override fun getItemCount() = yourListItems?.size ?: 0
}