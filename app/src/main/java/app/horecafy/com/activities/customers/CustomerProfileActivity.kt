package app.horecafy.com.activities.customers

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.models.Customer
import app.horecafy.com.models.TypeOfBusiness
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CommonService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import app.horecafy.com.util.Validators
import kotlinx.android.synthetic.main.activity_customer_profile.*

class CustomerProfileActivity : AppCompatActivity() {

    var typeOfBusiness: List<TypeOfBusiness>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile)

        CommonService.typeOfBusiness(this, { status, data ->
            if (status) {
                typeOfBusiness = data
                val adapter = ArrayAdapter<TypeOfBusiness>(this, R.layout.support_simple_spinner_dropdown_item, data?.toTypedArray())
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                spinnerTypeOfBusiness.adapter = adapter

                bindCustomer()
            } else {
                // TODO Error loading typeofbusiness
            }
        })
        var Provincia: ArrayList<String> = Constants.GetProvincia(this@CustomerProfileActivity)

        editProvince.setOnClickListener(View.OnClickListener {
            if(Provincia.size>0) {
                ShowSingleDialog(Provincia, editProvince.text.toString())
            }else{
                Constants.ShowAlertDialog(this)
            }
        })

        buttonSave.setOnClickListener(View.OnClickListener {
            if (isValid()) {
                val email = editMail.text.toString()
                val name = editName.text.toString()
                val typeOfBusinessId = (spinnerTypeOfBusiness.selectedItem as TypeOfBusiness).id
                val contactName = editContactName.text.toString()
                val contactMobile = editContactMobile.text.toString()
                val address = editAddress.text.toString()
                val city = editCity.text.toString()
                val zipCode = editZipCode.text.toString()
                val province = editProvince.text.toString()

                val customer = Customer(hiddenId = AuthService.customer?.hiddenId!!,
                        id = AuthService.customer?.id!!,
                        email = email,
                        password = "",
                        name = name,
                        typeOfBusinessId = typeOfBusinessId,
                        contactName = contactName,
                        contactEmail = "",
                        contactMobile = contactMobile,
                        address = address,
                        city = city,
                        zipCode = zipCode,
                        province = province
                )

                UiHelpers.showProgessBar(window, progressBar)
                CustomerService.update(this, customer) { status, data, error ->
                    UiHelpers.hideProgessBar(window, progressBar)
                    if (status) {
                        Toast.makeText(this, "Distribuidor actualizado correctamente", Toast.LENGTH_LONG).show()
                        AuthService.customer = data!!
                        finish()
                    } else {
                        when (error) {
                            "EMAIL_DUPLICATED" -> {
                                editMail.setError("Correo electrónico ya en uso")
                                requestFocus(editMail, true)
                            }
                            else -> Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }

    fun ShowSingleDialog(provincia: ArrayList<String>, proselected: String) {

        val cs = provincia.toArray(arrayOfNulls<CharSequence>(provincia.size))
        var selectedPosition = 0
        cs.indices.forEach { i ->
            when {
                cs[i].toString().equals(proselected) -> selectedPosition = i
            }
        }

        val dialog = AlertDialog.Builder(this).setTitle("Lista de Provincia").setSingleChoiceItems(cs, selectedPosition) { dialog, which ->
            Log.e("00000000000",""+cs[which])
            editProvince.setText(cs[which])
            dialog.dismiss()
        }.create()
        dialog.show()
    }

    private fun bindCustomer() {
        var customer = AuthService.customer!!
        editMail.setText(customer.email)
        editName.setText(customer.name)
        val typeOfBusinessIndex = typeOfBusiness!!.indexOfFirst { it.id == customer.typeOfBusinessId }
        if (typeOfBusinessIndex != null) {
            spinnerTypeOfBusiness.setSelection(typeOfBusinessIndex)
        }
        editContactName.setText(customer.contactName)
        editContactMobile.setText(customer.contactMobile)
        editAddress.setText(customer.address)
        editCity.setText(customer.city)
        editZipCode.setText(customer.zipCode)
        editProvince.setText(customer.province)
    }

    private fun isValid(): Boolean {
        var isValid = true

        val email = editMail.text.toString()
        val name = editName.text.toString()
        val typeOfBusiness = spinnerTypeOfBusiness.selectedItem as TypeOfBusiness
        val contactName = editContactName.text.toString()
        val contactMobile = editContactMobile.text.toString()
        val address = editAddress.text.toString()
        val city = editCity.text.toString()
        val zipCode = editZipCode.text.toString()
        val province = editProvince.text.toString()

        if (email.isNullOrEmpty()) {
            editMail.setError("Este campo es obligatorio")
            requestFocus(editMail, isValid)
            isValid = false
        }

        if (!Validators.isEmail(email)) {
            editMail.setError("El correo es incorrecto")
            requestFocus(editMail, isValid)
            isValid = false
        }

        if (name.isNullOrEmpty()) {
            editName.setError("Este campo es obligatorio")
            requestFocus(editName, isValid)
            isValid = false
        }

        if (typeOfBusiness.id == -1) {
            val errorText = spinnerTypeOfBusiness.getSelectedView() as TextView
            errorText.error = "Este campo es obligatorio"
            errorText.setTextColor(Color.RED)
            errorText.text = "Este campo es obligatorio"

            requestFocus(errorText, isValid)
            isValid = false
        }

        if (contactName.isNullOrEmpty()) {
            editContactName.setError("Este campo es obligatorio")
            requestFocus(editContactName, isValid)
            isValid = false
        }

        if (contactMobile.isNullOrEmpty()) {
            editContactMobile.setError("Este campo es obligatorio")
            requestFocus(editContactMobile, isValid)
            isValid = false
        }

        if (address.isNullOrEmpty()) {
            editAddress.setError("Este campo es obligatorio")
            requestFocus(editAddress, isValid)
            isValid = false
        }

        if (city.isNullOrEmpty()) {
            editCity.setError("Este campo es obligatorio")
            requestFocus(editCity, isValid)
            isValid = false
        }

        if (zipCode.isNullOrEmpty()) {
            editZipCode.setError("Este campo es obligatorio")
            requestFocus(editZipCode, isValid)
            isValid = false
        }

        if (province.isNullOrEmpty()) {
            editProvince.setError("Este campo es obligatorio")
            requestFocus(editProvince, isValid)
            isValid = false
        }

        return isValid
    }

    private fun requestFocus(view: View, setFocus: Boolean) {
        if (setFocus) {
            view.requestFocus()
        }
    }
}
