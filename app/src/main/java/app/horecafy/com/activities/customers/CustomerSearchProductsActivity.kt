package app.horecafy.com.activities.customers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.models.RequestProduct
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_customer_search_products.*

class CustomerSearchProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_search_products)

        btnSendtoHorecafy.setOnClickListener(View.OnClickListener {
            if (isValid()) {

                val strProductDescription = etProductDescription.text.toString()
                val strBrand = etBrand.text.toString()
                val strConsumptionMonth = etConsumptionMonth.text.toString()
                val strTargetPrice = etTargetPrice.text.toString()

                val success: String

                if (rbYes.isChecked) {
                    success = "Yes"
                } else {
                    success = "No"
                }

                UiHelpers.showProgessBar(window, rlProgressBarSearchProduct)

                val customerId = AuthService.customer!!.hiddenId!!

                val requestProduct = RequestProduct(customerId, strProductDescription, strBrand,
                        strConsumptionMonth, strTargetPrice, success)

                CustomerService.requestProduct(this, requestProduct) { status, data, error ->
                    UiHelpers.hideProgessBar(window, rlProgressBarSearchProduct)
                    if (data == null) {
                        Toast.makeText(this, Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_LONG).show()
                    } else {
                        val intent = CustomerSearchProductsSuccessActivity.intent(this)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        })
    }

    private fun isValid(): Boolean {
        var isValid = true

        val productDescription = etProductDescription.text.toString()

        if (productDescription.isNullOrEmpty()) {
            etProductDescription.setError("Este campo es obligatorio")
            isValid = false
        }

        return isValid
    }
}
