package app.horecafy.com.activities.customers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
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
import kotlinx.android.synthetic.main.activity_customer_families.*


class CustomerFamiliesActivity : AppCompatActivity() {

    companion object {
        val INTENT_CATEGORY = "INTENT_REVIEW_OFFERS_LIST"
        val INTENT_FAMILY = "INTENT_FAMILY"

        fun intent(context: Context, category: Category): Intent {
            val intent = Intent(context, CustomerFamiliesActivity::class.java)
            intent.putExtra(INTENT_CATEGORY, category);
            return intent
        }
    }

    var category: Category? = null
    var families: List<Family>? = null
    var typeOfFormats: List<TypeOfFormat>? = null

    var demand: Demand? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_families)

        val customerId = AuthService.customer!!.hiddenId!!

        // Get category from intent
        if (category == null) {
            category = intent.getSerializableExtra(CustomerCreateListDemandsActivity.INTENT_CATEGORY) as Category?
        }

        // Load families
        CommonService.familiesByCustomerId(this, customerId, category!!.id, { status, data ->
            if (status && data != null) {
                families = data.filter { it.id != -1 }
                if (families!!.isEmpty()) {
                    listFamilies.setVisibility(View.GONE)
                    emptyView.setVisibility(View.VISIBLE)
                } else {
                    listFamilies.setVisibility(View.VISIBLE)
                    emptyView.setVisibility(View.GONE)

                    setFamiliesAdapter()

                    // Handle click
                    listFamilies.setOnItemClickListener { _, _, position, _ ->

                        alertDialogforDemandForm(position)
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

    private fun alertDialogforDemandForm(position: Int) {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@CustomerFamiliesActivity)

        // Set the alert dialog title
//        builder.setTitle("App background color")

        // Display a message on alert dialog
//        builder.setMessage("¿Quieres dar consumo y precio objetivo para que te ofrecen mejor?")
        builder.setMessage("¿Quieres dar el consumo y tu precio objetivo para obtener mejores ofertas?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Sí") { dialog, which ->
            // Do something when user press the positive button
            val intent = Intent()
            intent.putExtra(INTENT_FAMILY, families!![position])
            setResult(Activity.RESULT_OK, intent)
            finish()
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No") { dialog, which ->
            loadTypeOfFormat(position)
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    private fun loadTypeOfFormat(position: Int) {
        CommonService.typeOfFormat(this, { status, data ->
            typeOfFormats = data
            if (status) {

                val selectedTypeOfFormat: Int = typeOfFormats!![0].id

                createDemandApiCall(selectedTypeOfFormat, position)
            } else {
                // TODO Error loading typeofformat
                UiHelpers.hideProgessBar(window, progressBar)
            }
        })
    }

    private fun createDemandApiCall(selectedTypeOfFormat: Int, position: Int) {

        UiHelpers.showProgessBar(window, progressBar)

        val typeOfFormat = selectedTypeOfFormat

        if (demand == null) {

            demand = Demand(customerId = AuthService.customer?.hiddenId!!,
                    familyId = families!![position].id,
                    quantyPerMonth = null,
                    typeOfFormatId = typeOfFormat,
                    targetPrice = null,
                    brand = "",
                    format = "",
                    comments = "",
                    family = null)

            DemandService.create(this, demand!!) { status, data, error ->
                UiHelpers.hideProgessBar(window, progressBar)
                if (status) {
                    setResult(Activity.RESULT_CANCELED, null)
                    finish()
                } else {
                    Toast.makeText(this, "Error desconocido", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
