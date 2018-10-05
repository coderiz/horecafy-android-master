package app.horecafy.com.activities.wholesalers

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_wholesaler_make_offer_category.*

class WholesalerMakeOfferCategoryActivity : AppCompatActivity() {

    companion object {
        val REQUEST_DEMANDS = 1
    }

    var categories: MutableList<Category> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wholesaler_make_offer_category)

        // Configure recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.itemAnimator = DefaultItemAnimator()
        setAdapter()

        // Load mAvailabilityList
        loadCategories()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_DEMANDS && (resultCode == AppCompatActivity.RESULT_OK || resultCode == AppCompatActivity.RESULT_CANCELED)) {
            // Reload demands
            loadCategories()
        }
    }

    private fun loadCategories() {
        // Load mAvailabilityList
        CommonService.categoriesWholesalerListWithFamilyCount(this, AuthService.wholesaler?.hiddenId!!, { status, data ->
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
        val adapter = CategoryRecyclerViewAdapter(this@WholesalerMakeOfferCategoryActivity, categories,0)
        recyclerView.adapter = adapter

        // Handle click
        adapter.onClickListener = View.OnClickListener { v: View? ->
            // Get selected category
            val position = recyclerView.getChildAdapterPosition(v)
            val category = categories[position]

            // Start family activity
            val intent = WholesalerMakeOfferDemandsActivity.intent(this, category)
            startActivityForResult(intent, REQUEST_DEMANDS)
        }
    }
}
