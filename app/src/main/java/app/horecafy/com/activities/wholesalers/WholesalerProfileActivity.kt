package app.horecafy.com.activities.wholesalers

import android.content.DialogInterface
import android.opengl.ETC1.isValid
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.models.TypeOfBusiness
import app.horecafy.com.models.Wholesaler
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.WholesalerService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import app.horecafy.com.util.Validators
import kotlinx.android.synthetic.main.activity_wholesaler_profile.*


class WholesalerProfileActivity : AppCompatActivity() {

    var typeOfBusiness: List<TypeOfBusiness>? = null
    var ProvinciaSelected: ArrayList<String> = ArrayList()
    var selectedArray: List<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wholesaler_profile)

        bindWholesaler()

        var Provincia: ArrayList<String> = Constants.GetProvincia(this@WholesalerProfileActivity)
        editProvince.setOnClickListener(View.OnClickListener {
            selectedArray= editProvince.text.toString()!!.split(",").map { it.trim() }
            if(Provincia.size>0) {
                ShowDialog(Provincia, selectedArray)
            }else{
                Constants.ShowAlertDialog(this)
            }
        })

        buttonSave.setOnClickListener(View.OnClickListener {
            if (isValid()) {
                val vat = editVat.text.toString()
                val email = editMail.text.toString()
                val name = editName.text.toString()
                val typeOfBusinessId = 1
                val contactName = editContactName.text.toString()
                val contactMobile = editContactMobile.text.toString()
                val address = editAddress.text.toString()
                val city = editCity.text.toString()
                val zipCode = editZipCode.text.toString()
                val province = editProvince.text.toString()
              //  val country = editCountry.text.toString()

                val customer = Wholesaler(hiddenId = AuthService.wholesaler?.hiddenId!!,
                        id = AuthService.wholesaler?.id!!,
                        VAT = vat,
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
                WholesalerService.update(this, customer) { status, data, error ->
                    UiHelpers.hideProgessBar(window, progressBar)
                    if (status) {
                        Toast.makeText(this, "Distribuidor actualizado correctamente", Toast.LENGTH_LONG).show()
                        AuthService.wholesaler = data!!
                        finish()
                    } else {
                        when (error) {
                            "VAT_DUPLICATED" -> editVat.setError("VAT ya en uso")
                            "EMAIL_DUPLICATED" -> editMail.setError("Correo electrónico ya en uso")
                            else -> Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }
    private fun ShowDialog(provincia: ArrayList<String>,selectedArr: List<String>) {
        ProvinciaSelected.clear()
        val checkedItems = booleanArrayOf(false,false,false,false,false,false,false,false,
                false,false,false,false,false,false,false,false,false,false,false,false,
                false,false,false,false,false,false,false,false,false,false,false,false,
                false,false,false,false,false,false,false,false,false,false,false,false,
                false,false,false,false,false,false,false,false)

        val cs = provincia.toArray(arrayOfNulls<CharSequence>(provincia.size))
        if (selectedArr.size>0) {
            for (i in cs.indices) {
                Log.e("TestData", "00  " + checkedItems.get(i));
                if (selectedArr.contains(cs[i])) {
                    Log.e("SIZE", "True")
                    checkedItems.set(i, true)
                    ProvinciaSelected!!.add(cs[i].toString())
                    Log.e("TestData", "111  " + checkedItems.get(i));
                }
            }
        }
        val dialog = AlertDialog.Builder(this)
                .setTitle("Lista de Provincia")
                .setMultiChoiceItems(cs, checkedItems, DialogInterface.OnMultiChoiceClickListener { dialog, indexSelected, isChecked ->
                    if (isChecked) {
                        // If the user checked the item, add it to the selected items
                        checkedItems.set(indexSelected,true)
                        ProvinciaSelected!!.add(cs[indexSelected].toString())
                    } else if (ProvinciaSelected!!.contains(cs[indexSelected].toString())) {
                        // Else, if the item is already in the array, remove it
                        ProvinciaSelected!!.remove(cs[indexSelected].toString())
                        checkedItems.set(indexSelected,false)
                    }
                }).setPositiveButton("De acuerdo", DialogInterface.OnClickListener { dialog, id ->
                      editProvince.setText(TextUtils.join(",", ProvinciaSelected))
                    Log.e("EEEEEEEEEEE",""+ProvinciaSelected.toString())
                }).setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, id ->
                }).create()
        dialog.show()
    }

    private fun bindWholesaler() {
        var wholesaler = AuthService.wholesaler!!

        editVat.setText(wholesaler.VAT)
        editMail.setText(wholesaler.email)
        editName.setText(wholesaler.name)
        editContactName.setText(wholesaler.contactName)
        editContactMobile.setText(wholesaler.contactMobile)
        editAddress.setText(wholesaler.address)
        editCity.setText(wholesaler.city)
        editZipCode.setText(wholesaler.zipCode)
        editProvince.setText(wholesaler.province)
        //editCountry.setText(wholesaler.country)
    }

    private fun isValid(): Boolean {
        var isValid = true

        val vat = editVat.text.toString()
        val email = editMail.text.toString()
        val name = editName.text.toString()
        val contactName = editContactName.text.toString()
        val contactMobile = editContactMobile.text.toString()
        val address = editAddress.text.toString()
        val city = editCity.text.toString()
        val zipCode = editZipCode.text.toString()
        val province = editProvince.text.toString()
       // val country = editCountry.text.toString()

        if (vat.isNullOrEmpty()) {
            editVat.setError("Este campo es obligatorio")
            requestFocus(editVat, isValid)
            isValid = false
        }

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

      /*  if (country.isNullOrEmpty()) {
            editCountry.setError("Este campo es obligatorio")
            requestFocus(editCountry, isValid)
            isValid = false
        }*/

        return isValid
    }

    private fun requestFocus(view: View, setFocus: Boolean) {
        if (setFocus) {
            view.requestFocus()
        }
    }
}
