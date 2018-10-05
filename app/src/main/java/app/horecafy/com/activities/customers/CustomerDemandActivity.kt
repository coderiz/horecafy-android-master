package app.horecafy.com.activities.customers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.models.Category
import app.horecafy.com.models.Demand
import app.horecafy.com.models.Family
import app.horecafy.com.models.TypeOfFormat
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CommonService
import app.horecafy.com.services.DemandService
import app.horecafy.com.util.UiHelpers
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_customer_demand.*


class CustomerDemandActivity : AppCompatActivity() {

    companion object {
        val INTENT_CATEGORY = "INTENT_REVIEW_OFFERS_LIST"
        val INTENT_FAMILY = "INTENT_FAMILY"
        val INTENT_DEMAND = "INTENT_DEMAND"

        fun intent(context: Context, category: Category, familyId: Int, demandId: Long?): Intent {
            val intent = Intent(context, CustomerDemandActivity::class.java)
            intent.putExtra(INTENT_CATEGORY, category);
            intent.putExtra(INTENT_FAMILY, familyId);
            intent.putExtra(INTENT_DEMAND, demandId);
            return intent
        }
    }

    var category: Category? = null
    var familyId: Int? = null
    var demandId: Long? = null

    var families: List<Family>? = null
    var typeOfFormats: List<TypeOfFormat>? = null
    var demand: Demand? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_demand)

        // Get demandId from intent and load it from API
        category = intent.getSerializableExtra(INTENT_CATEGORY) as Category
        familyId = intent.getSerializableExtra(INTENT_FAMILY) as Int?
        demandId = intent.getSerializableExtra(INTENT_DEMAND) as Long?

        // Bind useful data
        val uri = "${this.getString(R.string.IMAGE_URL)}${category!!.image}"
        Picasso.with(this).load(uri).into(imageCategory)
        textCategory.text = category!!.name

        UiHelpers.showProgessBar(window, progressBar)
        loadFamilies()

        buttonSave.setOnClickListener(View.OnClickListener {
            UiHelpers.showProgessBar(window, progressBar)
            val brand = editBrand.text.toString()
            val typeOfFormat = spinnerTypeOfFormat.selectedItem as TypeOfFormat
            var quantityPerMonth: Int? = null
            if (!editQuantityPerMonth.text.isNullOrEmpty()) {
                quantityPerMonth = editQuantityPerMonth.text.toString().toInt()
            }
            var targetPrice: Double? = null
            if (!editTargetPrice.text.isNullOrEmpty()) {
                targetPrice = editTargetPrice.text.toString().toDouble()
            }
            val format = editFormat.text.toString()
            val comments = editComments.text.toString()

            if (demand == null) {
                demand = Demand(customerId = AuthService.customer?.hiddenId!!,
                familyId = familyId!!,
                quantyPerMonth = quantityPerMonth,
                typeOfFormatId = typeOfFormat.id,
                targetPrice = targetPrice,
                brand = brand,
                format = format,
                comments = comments, family = null)

                DemandService.create(this, demand!!) { status, data, error ->
                    UiHelpers.hideProgessBar(window, progressBar)
                    if (status) {
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Error desconocido", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                demand!!.quantyPerMonth = quantityPerMonth
                demand!!.typeOfFormatId = typeOfFormat.id
                demand!!.targetPrice = targetPrice
                demand!!.brand = brand
                demand!!.format = format
                demand!!.comments = comments

                DemandService.update(this, demand!!) { status, data, error ->
                    UiHelpers.hideProgessBar(window, progressBar)
                    if (status) {
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Error desconocido", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun loadFamilies() {
        CommonService.families(this, category!!.id, { status, data ->
            if (status) {
                families = data

                val familyIndex = families!!.indexOfFirst { it.id == familyId }
                if (familyIndex != null) {
                    textFamily.text = families!![familyIndex].name
                }

                loadTypeOfFormat()
            } else {
                // TODO Error loading families
                UiHelpers.hideProgessBar(window, progressBar)
            }
        })
    }

    private fun loadTypeOfFormat() {
        CommonService.typeOfFormat(this, { status, data ->
            typeOfFormats = data
            if (status) {
                val adapter = ArrayAdapter<TypeOfFormat>(this, R.layout.support_simple_spinner_dropdown_item, data?.toTypedArray())
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                spinnerTypeOfFormat.adapter = adapter

                if (demandId != null) {
                    loadDemand()
                } else {
                    UiHelpers.hideProgessBar(window, progressBar)
                }

            } else {
                // TODO Error loading typeofformat
                UiHelpers.hideProgessBar(window, progressBar)
            }
        })
    }

    private fun loadDemand() {
        DemandService.get(this, demandId!!, { status, data, error ->
            if (status) {
                demand = data
                bindDemand(demand!!)
            } else {
                // TODO Error loading demand
                UiHelpers.hideProgessBar(window, progressBar)
            }
        })
    }

    private fun bindDemand(demand: Demand) {
        // Bind demand
        editBrand.setText(demand.brand)
        if (demand.quantyPerMonth != null) {
            editQuantityPerMonth.setText(demand.quantyPerMonth.toString())
        }
        if (demand.targetPrice != null) {
            editTargetPrice.setText(demand.targetPrice.toString())
        }
        editFormat.setText(demand.format)
        editComments.setText(demand.comments)

        // Bind typeofformat
        val typeOfFormatIndex = typeOfFormats!!.indexOfFirst { it.id == demand.typeOfFormatId }
        if (typeOfFormatIndex != null) {
            spinnerTypeOfFormat.setSelection(typeOfFormatIndex)
        }

        // Disable progress bar
        UiHelpers.hideProgessBar(window, progressBar)
    }
}