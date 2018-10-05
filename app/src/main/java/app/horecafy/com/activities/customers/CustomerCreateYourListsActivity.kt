package app.horecafy.com.activities.customers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.activities.customers.CustomerDemandSuccessActivity.Companion.DEMAND_SUCCESS
import app.horecafy.com.adapters.CreatedListRecyclerViewAdapter
import app.horecafy.com.models.CreateYourListItems
import app.horecafy.com.models.FreeDemand
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_customer_create_your_lists.*
import org.json.JSONArray


class CustomerCreateYourListsActivity : AppCompatActivity() {

    var createList: MutableList<CreateYourListItems> = mutableListOf()

    companion object {
        fun intent(context: Context): Intent {
            val intent = Intent(context, CustomerCreateYourListsActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_create_your_lists)

        // Configure recyclerView
        rvCreateYourList.layoutManager = LinearLayoutManager(this)
        rvCreateYourList.itemAnimator = DefaultItemAnimator()

        newBlankElement()

        setAdpater()

        tvAddProduct.setOnClickListener({ v: View? ->

            newBlankElement()

            setAdpater()
        })

        btnGetOffers.setOnClickListener({ v: View? ->

            getFinalArrayofYourList()
        })
    }

    private fun newBlankElement() {

        val item = CreateYourListItems("")
        createList.add(item)

        rvCreateYourList.scrollToPosition(createList.size - 1)

    }

    private fun setAdpater() {

        // Set the adapter
        val mAdapter = CreatedListRecyclerViewAdapter(createList, this)
        rvCreateYourList.adapter = mAdapter
    }

    fun refreshList() {

        setAdpater()
    }

    fun addValueToList(position: Int, value: String) {

        val item = CreateYourListItems(value)
        createList.removeAt(position)
        createList.add(position, item)
    }

    fun getFinalArrayofYourList() {

        val finalCreateList: MutableList<String> = mutableListOf()

        for (i in createList.indices) {

            if (!createList[i].value.trim().equals("")) {

                finalCreateList.add(createList[i].value)
            }
        }

        val jsArray = JSONArray(finalCreateList)
//        Log.d("String JsonArray 1", "" + jsArray)

        val customerId = AuthService.customer!!.hiddenId!!

        UiHelpers.showProgessBar(window, rlProgressBar)

        if (finalCreateList.size > 0) {

            val freeDemand = FreeDemand(
                    customerId = customerId,
                    demandText = jsArray.toString())

            CustomerService.submitFreeDemandList(this, freeDemand) { status, data, error ->
                UiHelpers.hideProgessBar(window, rlProgressBar)
                if (status) {

                    // Start family activity
                    val intent = CustomerDemandSuccessActivity.intent(this)
                    startActivityForResult(intent, DEMAND_SUCCESS)
                } else {
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            }
        } else {
//            Toast.makeText(this, "Por favor ingrese al menos un valor para continuar.", Toast.LENGTH_LONG).show()
            Toast.makeText(this, "por favor, introduzca productos primero.", Toast.LENGTH_LONG).show()
            UiHelpers.hideProgessBar(window, rlProgressBar)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CustomerDemandSuccessActivity.DEMAND_SUCCESS
                && resultCode == AppCompatActivity.RESULT_CANCELED) {

            createList.clear()

            newBlankElement()

            setAdpater()
        }
    }
}
