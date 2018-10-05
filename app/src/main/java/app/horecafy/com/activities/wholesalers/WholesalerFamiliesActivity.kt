package app.horecafy.com.activities.wholesalers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import app.horecafy.com.R
import app.horecafy.com.models.Category
import app.horecafy.com.models.Family
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CommonService
import app.horecafy.com.util.Constants
import kotlinx.android.synthetic.main.activity_wholesaler_families.*


class WholesalerFamiliesActivity : AppCompatActivity() {

    companion object {
        val INTENT_CATEGORY = "INTENT_REVIEW_OFFERS_LIST"
        val INTENT_FAMILY = "INTENT_FAMILY"

        fun intent(context: Context, category: Category): Intent {
            val intent = Intent(context, WholesalerFamiliesActivity::class.java)
            intent.putExtra(INTENT_CATEGORY, category);
            return intent
        }
    }

    var category: Category? = null
    var families: List<Family>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wholesaler_families)

        val wholesalerId = AuthService.wholesaler!!.hiddenId!!

        // Get category from intent
        if (category == null) {
            category = intent.getSerializableExtra(INTENT_CATEGORY) as Category?
        }

        // Load families
        CommonService.familiesByWholesalerId(this, wholesalerId, category!!.id, { status, data ->
            if (status && data != null) {
                families =  data.filter { it.id != -1 }
                if (families!!.isEmpty()) {
                    listFamilies.setVisibility(View.GONE)
                    emptyView.setVisibility(View.VISIBLE)
                } else {
                    listFamilies.setVisibility(View.VISIBLE)
                    emptyView.setVisibility(View.GONE)

                    setFamiliesAdapter()

                    // Handle click
                    listFamilies.setOnItemClickListener { _, _, position, _ ->
                        val intent = Intent()
                        intent.putExtra(INTENT_FAMILY, families!![position])
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            } else {
                // TODO Error loading families
                listFamilies.setVisibility(View.VISIBLE)
                emptyView.setVisibility(View.GONE)
            }
        })
    }

    private fun setFamiliesAdapter() {
        listFamilies.adapter = ArrayAdapter<Family>(this, android.R.layout.simple_list_item_1, families)
    }
}
