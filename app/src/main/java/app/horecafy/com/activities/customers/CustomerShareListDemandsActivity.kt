package app.horecafy.com.activities.customers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import app.horecafy.com.util.UiHelpers
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_customer_share_list_demands.*


class CustomerShareListDemandsActivity : AppCompatActivity() {

    companion object {
        val INTENT_CATEGORY = "INTENT_REVIEW_OFFERS_LIST"
        val REQUEST_DEMAND = 1
        val REQUEST_THANKS = 2

        fun intent(context: Context, category: Category): Intent {
            val intent = Intent(context, CustomerShareListDemandsActivity::class.java)
            intent.putExtra(INTENT_CATEGORY, category);
            return intent
        }
    }

    var category: Category? = null
    var demands: MutableList<Demand> = mutableListOf()
    var demandsToShare: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_share_list_demands)

        // Get category from intent
        if (category == null) {
            category = intent.getSerializableExtra(CustomerCreateListDemandsActivity.INTENT_CATEGORY) as Category?
        }

        // Bind useful data
        val uri = "${this.getString(R.string.IMAGE_URL)}${category!!.image}"
        Picasso.with(this).load(uri).into(imageCategory)
        textCategory.text = category!!.name

        // Load families in demand for the selected category
        UiHelpers.showProgessBar(window, progressBar)
        loadDemands()

        // Handle click
        buttonShare.setOnClickListener(View.OnClickListener {
            if (demandsToShare.count() > 0) {
                UiHelpers.showProgessBar(window, progressBar)
                var count = 0
                var mStatus = true
                demandsToShare.forEach {
                    DemandService.share(this, it, { status, error ->
                        count++;
                        mStatus = mStatus && status
                        if (count == demandsToShare.count()) {
                            UiHelpers.hideProgessBar(window, progressBar)
                            if (mStatus) {
                                val intent = CustomerShareListThanks.intent(this)
                                startActivityForResult(intent, REQUEST_THANKS)
                            } else {
                                Toast.makeText(this, "Hubo algún error compartiendo las listas.", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            } else {
//                Toast.makeText(this, "Debe seleccionar al menos un elemento", Toast.LENGTH_LONG).show()
                Toast.makeText(this, "por favor, seleccione producto.", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == REQUEST_DEMAND && resultCode == AppCompatActivity.RESULT_OK) ||
            (requestCode == REQUEST_THANKS && resultCode == AppCompatActivity.RESULT_CANCELED)) {
            // Reload demands
            loadDemands()
        }
    }

    private fun loadDemands() {
        // Load demands
        DemandService.getByCustomer(this, AuthService.customer?.hiddenId!!, category!!.id, { status, data, error ->
            UiHelpers.hideProgessBar(window, progressBar)
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
        demandsToShare.clear()
        listDemands.adapter = object: ArrayAdapter<Demand>(this, R.layout.list_view_item_family_checked, demands) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_view_item_family_checked, parent, false)

                val check = view.findViewById<CheckBox>(R.id.checkBox)
                val text = view.findViewById<TextView>(R.id.textView)
                val image = view.findViewById<ImageButton>(R.id.imageButton)

                val demand = demands[position]
                text.text = demand.family?.name

                check.isChecked = true
                demandsToShare.add(demand.id!!)

                // Handle click
                check.setOnClickListener(View.OnClickListener {
                    if (check.isChecked) {
                        demandsToShare.add(demand.id!!)
                    } else {
                        demandsToShare.remove(demand.id!!)
                    }
                })

                text.setOnClickListener(View.OnClickListener {
                    // Update demand
                    val intent = CustomerDemandActivity.intent(context, category!!, demand.family!!.id, demand.hiddenId)
                    startActivityForResult(intent, CustomerShareListDemandsActivity.REQUEST_DEMAND)
                })

                image.setOnClickListener(View.OnClickListener {
                    // Remove demand
                    DemandService.delete(view.context, demand.hiddenId, { status, error ->
                        if (status) {
                            // Remove item locally
                            demands.remove(demand)
                            setDemandsAdapter()
                        } else {
                            // TODO Error deleting a demand
                        }
                    })
                })

                return view
            }
        }
    }
}