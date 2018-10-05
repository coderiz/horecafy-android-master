package app.horecafy.com.activities.customers

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.models.MakeAnOrder
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_customer_make_an_order_details.*
import java.text.SimpleDateFormat
import java.util.*

class CustomerMakeAnOrderDetailsActivity : AppCompatActivity() {

    companion object {
        val INTENT_SELECTED_DISTRIBUTOR = "INTENT_SELECTED_DISTRIBUTOR"
        val INTENT_PRODUCTS = "INTENT_PRODUCTS"

        fun intent(context: Context, selectedDistributor: String, products: String): Intent {
            val intent = Intent(context, CustomerMakeAnOrderDetailsActivity::class.java)
            intent.putExtra(INTENT_SELECTED_DISTRIBUTOR, selectedDistributor);
            intent.putExtra(INTENT_PRODUCTS, products);
            return intent
        }
    }

    var cal = Calendar.getInstance()

    var strSelectedDistributor: String = ""
    var strProducts: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_make_an_order_details)

        // Get strSelectedDistributor from intent
        if (strSelectedDistributor.equals("")) {
            strSelectedDistributor = intent.getStringExtra(CustomerMakeAnOrderDetailsActivity.INTENT_SELECTED_DISTRIBUTOR)
        }

        // Get strProducts from intent
        if (strProducts.equals("")) {
            strProducts = intent.getStringExtra(CustomerMakeAnOrderDetailsActivity.INTENT_PRODUCTS)
        }

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        etDeliveryDate.setOnClickListener(View.OnClickListener { v: View? ->
            DatePickerDialog(this@CustomerMakeAnOrderDetailsActivity,
                    R.style.DialogTheme,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()

        })

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            updateTimeInView()
        }

        etDeliveryTime.setOnClickListener(View.OnClickListener { v: View? ->

            TimePickerDialog(this@CustomerMakeAnOrderDetailsActivity,
                    R.style.DialogTheme,
                    timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
                    false).show()

        })

        btnSendOrder.setOnClickListener(View.OnClickListener { v: View? ->
            if (isValid()) {

                val strDeliveryDate = etDeliveryDate.text.toString()
                val strDeliveryTime = etDeliveryTime.text.toString()
                val strComments = etComments.text.toString()

                UiHelpers.showProgessBar(window, rlProgressBarMakeAnOrderDetails)

                val customerId = AuthService.customer!!.hiddenId!!

                val requestProduct = MakeAnOrder(customerId, strSelectedDistributor.toLong(), strDeliveryDate,
                        strProducts, strDeliveryTime, strComments)

                CustomerService.makeAnOrder(this, requestProduct) { status, error ->
                    UiHelpers.hideProgessBar(window, rlProgressBarMakeAnOrderDetails)

                    if (status) {
//                        Toast.makeText(this, "su pedido se ha entregado.", Toast.LENGTH_LONG).show()
                        Toast.makeText(this, "su pedido se ha enviado correctamente.", Toast.LENGTH_LONG).show()

                        val intent = Intent(applicationContext, CustomerMainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                }
            }
        })
    }

    private fun updateDateInView() {
//        val myFormat = "MM/dd/yyyy" // mention the format you need
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        val selectedDate = sdf.format(cal.getTime())

        etDeliveryDate.setText(selectedDate)
    }

    private fun updateTimeInView() {

//        val selectedTime = SimpleDateFormat("hh:mm a").format(cal.time)
        val selectedTime = SimpleDateFormat("HH:mm:ss").format(cal.time)

        etDeliveryTime.setText(selectedTime)
    }

    private fun isValid(): Boolean {
        var isValid = true

        val deliveryDate = etDeliveryDate.text.toString()

        if (deliveryDate.isNullOrEmpty()) {
            etDeliveryDate.setError("Este campo es obligatorio")
            isValid = false
        }

        return isValid
    }
}
