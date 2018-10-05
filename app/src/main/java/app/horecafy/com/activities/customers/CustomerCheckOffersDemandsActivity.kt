package app.horecafy.com.activities.customers

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.horecafy.com.R
import app.horecafy.com.models.Category
import app.horecafy.com.models.Demand
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.DemandService
import app.horecafy.com.util.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_customer_check_offers_demands.*

class CustomerCheckOffersDemandsActivity : AppCompatActivity() {

    companion object {
        val INTENT_CATEGORY = "INTENT_REVIEW_OFFERS_LIST"
        val REQUEST_OFFERS = 1

        fun intent(context: Context, category: Category): Intent {
            val intent = Intent(context, CustomerCheckOffersDemandsActivity::class.java)
            intent.putExtra(INTENT_CATEGORY, category);
            return intent
        }
    }

    var category: Category? = null
    var demands: MutableList<Demand> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_check_offers_demands)

        // Get category from intent
        if (category == null) {
            category = intent.getSerializableExtra(INTENT_CATEGORY) as Category?
        }

        // Bind useful data
        val uri = "${this.getString(R.string.IMAGE_URL)}${category!!.image}"
        Picasso.with(this).load(uri).into(imageCategory)
        textCategory.text = category!!.name

        // Load families in demand for the selected category
        loadDemands()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_OFFERS && (resultCode == AppCompatActivity.RESULT_OK || resultCode == AppCompatActivity.RESULT_CANCELED)) {
            // Reload demands
            loadDemands()
        }
    }

    private fun loadDemands() {
        // Load demands
        DemandService.getByCustomerWithOffers(this, AuthService.customer?.hiddenId!! ,category!!.id, { status, data, error ->
            if (status && data != null) {
                demands = data.toMutableList()
                setDemandsAdapter()
            } else {
                // TODO Error loading demands
                Log.w(Constants.TAG, "Error loading demands: ${error}")
            }
        })
    }

    private fun setDemandsAdapter() {
        listDemands.adapter = object: ArrayAdapter<Demand>(this, R.layout.list_view_item_family, demands) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_view_item_family, parent, false)

                val text = view.findViewById<TextView>(R.id.textView)
                val image = view.findViewById<ImageButton>(R.id.imageButton)

                val demand = demands[position]
                text.text = demand.family?.name

                image.visibility = View.INVISIBLE

                // Handle click
                view.setOnClickListener(View.OnClickListener {
                    // Update demand
                    val intent = CustomerCheckOffersOffersActivity.intent(context, category!!, demand.hiddenId!!)
                    startActivityForResult(intent, REQUEST_OFFERS)
                })

                return view
            }
        }
    }
}
