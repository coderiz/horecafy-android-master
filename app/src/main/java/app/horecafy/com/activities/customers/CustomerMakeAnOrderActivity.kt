package app.horecafy.com.activities.customers

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.adapters.MakeOrdersListRecyclerViewAdapter
import app.horecafy.com.models.AddProductItems
import app.horecafy.com.models.Wholesaler
import app.horecafy.com.services.CustomerService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_customer_make_an_order.*

class CustomerMakeAnOrderActivity : AppCompatActivity() {

    var mWholesalerslist: MutableList<Wholesaler> = mutableListOf()
    var mWholesalersNameslist: MutableList<String> = mutableListOf()
    var mSelectedWholesalerId: String = ""
    var mOrdersProductsList: MutableList<AddProductItems> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_make_an_order)

        hideDetails()

        mWholesalersNameslist.add(Constants.CONSTANT_SELECT_DISTRIBUTOR)

        if (acsWholesalersList != null) {
            acsWholesalersList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    if (mWholesalersNameslist[position].equals(Constants.CONSTANT_SELECT_DISTRIBUTOR)) {
                        hideDetails()
                        mOrdersProductsList.clear()
                    } else {

                        mSelectedWholesalerId = mWholesalerslist[position - 1].hiddenId.toString()

                        showDetails()

                        newBlankElement()

                        setAdpater()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    mOrdersProductsList.clear()
                    hideDetails()
                }
            }
        }

        loadWholesalersLists()

        tvInviteDistributor.setOnClickListener(View.OnClickListener { v: View? ->
            hideDetails()
            acsWholesalersList.setSelection(0);
            mOrdersProductsList.clear()

            startActivity(Intent(this@CustomerMakeAnOrderActivity, CustomerInviteDistributorActivity::class.java))
        })

        // Configure recyclerView
        rvProductOrdersList.layoutManager = LinearLayoutManager(this)
        rvProductOrdersList.itemAnimator = DefaultItemAnimator()
        rvProductOrdersList.setHasFixedSize(true)

        tvAddMakeOrderProduct.setOnClickListener({ v: View? ->

            newBlankElement()

            setAdpater()

            rvProductOrdersList.scrollToPosition(mOrdersProductsList.size - 1)
        })

        btnFollowing.setOnClickListener({ v: View? ->

            if (IsValid()) {

                val finalOrderProductsList: MutableList<AddProductItems> = mutableListOf()

                for (i in mOrdersProductsList.indices) {

                    if (!mOrdersProductsList[i].Name.trim().equals("")
                            && !mOrdersProductsList[i].Quantity.trim().equals("")) {

                        val item = AddProductItems(mOrdersProductsList[i].Name, mOrdersProductsList[i].Quantity)

                        finalOrderProductsList.add(item)
                    }
                }

                if (finalOrderProductsList.size > 0) {

                    var products: String = ""

                    for (i in finalOrderProductsList.indices) {

                        if (products.trim().length == 0) {

                            products = finalOrderProductsList[i].Name.trim() + ":" +
                                    finalOrderProductsList[i].Quantity
                        } else {
                            products = products + "#" + finalOrderProductsList[i].Name.trim() + ":" +
                                    finalOrderProductsList[i].Quantity
                        }
                    }

                    val intent = CustomerMakeAnOrderDetailsActivity.intent(this,
                            mSelectedWholesalerId, products)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@CustomerMakeAnOrderActivity,
                            "Por favor ingrese al menos un valor para continuar.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun IsValid(): Boolean {

        var isValid = true

        if (mOrdersProductsList.size > 0
                && mOrdersProductsList[0].Name.equals(Constants.CONSTANT_EMPTY_STRING)
                && mOrdersProductsList[0].Quantity.equals(Constants.CONSTANT_EMPTY_STRING)) {

            isValid = false
            Toast.makeText(this@CustomerMakeAnOrderActivity,
                    "Por favor ingrese al menos un valor para continuar.", Toast.LENGTH_SHORT).show()
        } else if (mOrdersProductsList.size > 0) {

            for (i in mOrdersProductsList.indices) {
                if (mOrdersProductsList[i].Name.equals("") && !mOrdersProductsList[i].Quantity.equals("")) {

                    Toast.makeText(this@CustomerMakeAnOrderActivity,
                            "Por favor ingrese un nombre válido para el producto " + (i + 1) + ".", Toast.LENGTH_SHORT).show()
                    isValid = false
                    break
                } else if (!mOrdersProductsList[i].Name.equals("") && mOrdersProductsList[i].Quantity.equals("")) {

                    Toast.makeText(this@CustomerMakeAnOrderActivity,
                            "Por favor ingrese una cantidad válida para el producto " + (i + 1) + ".", Toast.LENGTH_SHORT).show()
                    isValid = false
                    break
                } else {
                    isValid = true
                }
            }
        } else {
            isValid = false
            Toast.makeText(this@CustomerMakeAnOrderActivity,
                    "Por favor ingrese al menos un valor para continuar.", Toast.LENGTH_SHORT).show()
        }

        return isValid
    }

    private fun hideDetails() {

        viewDividerOne.setVisibility(View.GONE);
        llTitleBar.setVisibility(View.GONE);
        viewDividerTwo.setVisibility(View.GONE);
        rvProductOrdersList.setVisibility(View.GONE);
        llAddProductOrder.setVisibility(View.GONE);
        btnFollowing.setVisibility(View.GONE);
    }

    private fun showDetails() {

        viewDividerOne.setVisibility(View.VISIBLE);
        llTitleBar.setVisibility(View.VISIBLE);
        viewDividerTwo.setVisibility(View.VISIBLE);
        rvProductOrdersList.setVisibility(View.VISIBLE);
        llAddProductOrder.setVisibility(View.VISIBLE);
        btnFollowing.setVisibility(View.VISIBLE);
    }

    fun loadWholesalersLists() {
        UiHelpers.showProgessBar(window, rlProgressBarMakeAnOrder)
        CustomerService.getWholesalersList(this) { status, data, error ->
            UiHelpers.hideProgessBar(window, rlProgressBarMakeAnOrder)
            if (status) {

                mWholesalerslist = data.toMutableList()

                for (i in mWholesalerslist.indices) {

                    mWholesalersNameslist.add(mWholesalerslist[i].name)
                }

                if (mWholesalersNameslist.isEmpty()) {
                    Log.e("List", "Empty")
                } else {
                    Log.e("List Size", "" + mWholesalersNameslist.size)
                    val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mWholesalersNameslist)
                    acsWholesalersList.adapter = arrayAdapter as SpinnerAdapter?
                }
            }
        }
    }

    private fun newBlankElement() {

        val item = AddProductItems(Constants.CONSTANT_EMPTY_STRING, Constants.CONSTANT_EMPTY_STRING)
        mOrdersProductsList.add(item)
    }

    fun addValueToList(position: Int, name: String, qty: String) {

        val item = AddProductItems(name, qty)
        mOrdersProductsList.removeAt(position)
        mOrdersProductsList.add(position, item)
    }

    private fun setAdpater() {

        // Set the adapter
        val mAdapter = MakeOrdersListRecyclerViewAdapter(mOrdersProductsList, this)
        rvProductOrdersList.adapter = mAdapter
    }

    fun refreshList() {

        setAdpater()
    }
}