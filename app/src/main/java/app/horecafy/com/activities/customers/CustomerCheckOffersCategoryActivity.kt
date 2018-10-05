package app.horecafy.com.activities.customers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.View
import app.horecafy.com.R
import app.horecafy.com.adapters.CategoryRecyclerViewAdapter
import app.horecafy.com.models.Category
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CommonService
import kotlinx.android.synthetic.main.activity_customer_check_offers_category.*


class CustomerCheckOffersCategoryActivity : AppCompatActivity() {

    var categories: MutableList<Category> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_check_offers_category)

        // Configure recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.itemAnimator = DefaultItemAnimator()
        setAdapter()

        // Load mAvailabilityList
        CommonService.categoriesOfferWithFamilyCount(this, AuthService.customer?.hiddenId!!, { status, data ->
            if (status && data != null) {
                categories = data

                if (categories.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    setAdapter()
                }
            } else {
                // TODO Error loading mAvailabilityList
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        })
    }

    private fun setAdapter() {
        // Set the adapter
        val adapter = CategoryRecyclerViewAdapter(this@CustomerCheckOffersCategoryActivity, categories,0)
        recyclerView.adapter = adapter

        // Handle click
        adapter.onClickListener = View.OnClickListener { v: View? ->
            // Get selected category
            val position = recyclerView.getChildAdapterPosition(v)
            val category = categories[position]

            // Start family activity
            val intent = CustomerCheckOffersDemandsActivity.intent(this, category)
            startActivity(intent)
        }
    }

}