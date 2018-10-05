package app.horecafy.com.activities.customers

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.View
import app.horecafy.com.R
import app.horecafy.com.adapters.CategoryRecyclerViewAdapter
import app.horecafy.com.models.Category
import app.horecafy.com.services.CommonService
import app.horecafy.com.util.Constants
import kotlinx.android.synthetic.main.activity_customer_create_list_category.*


class CustomerCreateListCategoryActivity : AppCompatActivity() {

    var categories: MutableList<Category> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_create_list_category)

        // Configure recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.itemAnimator = DefaultItemAnimator()
        setAdapter()

        // Reload mAvailabilityList
        loadCategories()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK
                || resultCode == AppCompatActivity.RESULT_CANCELED) {
            // Reload mAvailabilityList
            loadCategories()
        }
    }

    private fun loadCategories() {
        // Load mAvailabilityList
        CommonService.categories(this, { status, data ->
            if (status && data != null) {
//                mAvailabilityList = data.filter { it.id != -1 }
                categories = data
                categories.removeAt(0)
                setAdapter()
            } else {
                // TODO Error loading mAvailabilityList
            }
        })
    }

    private fun setAdapter() {

        val category = Category(0, Constants.CONSTANT_CREATE_LIST_FIRST_ELEMENT, "", "First", false, 0, 0,0,false)
        categories.add(0, category)

        // Set the adapter
        val adapter = CategoryRecyclerViewAdapter(this@CustomerCreateListCategoryActivity, categories,0)
        recyclerView.adapter = adapter

        // Handle click
        adapter.onClickListener = View.OnClickListener { v: View? ->
            // Get selected category
            val position = recyclerView.getChildAdapterPosition(v)
            val category = categories[position]

            if (category.id.equals(0)) {

                val intent = CustomerCreateYourListsActivity.intent(this)
                startActivity(intent)
            } else {

                val intent = CustomerCreateListDemandsActivity.intent(this, category)
                startActivity(intent)
            }
        }
    }
}
