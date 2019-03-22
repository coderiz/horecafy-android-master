package app.horecafy.com.activities.customers

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.activities.MainActivity
import app.horecafy.com.models.Customer
import app.horecafy.com.models.TypeOfBusiness
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CommonService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.UiHelpers
import app.horecafy.com.util.Validators
import kotlinx.android.synthetic.main.activity_customer_register.*

class CustomerRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_register)
                CommonService.typeOfBusiness(this, { status, data ->
            if (status) {
                val adapter = ArrayAdapter<TypeOfBusiness>(this, R.layout.support_simple_spinner_dropdown_item, data?.toTypedArray())
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                spinnerTypeOfBusiness.adapter = adapter
            } else {
                // TODO Error loading typeofbusiness
            }
        })
        val ss = SpannableString("Acepto la Política de privacidad y Términos y Condiciones")

        val terms = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Log.e("OOO", "Click terms")
                val browserIntent = Intent(Intent.ACTION_VIEW)
                browserIntent.data = Uri.parse("http://horecafy.com/acceso-clientes/")
                startActivity(browserIntent)
            }
        }

        val privacy = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Log.e("OOO", "Click privacy")
                val browserIntent = Intent(Intent.ACTION_VIEW)
                browserIntent.data = Uri.parse("http://horecafy.com/politica-de-privacidad/")
                startActivity(browserIntent)
            }
        }


        ss.setSpan(privacy, 10, 32, 0)
        ss.setSpan(terms, 35, 57, 0)
        tvCTerms.setMovementMethod(LinkMovementMethod.getInstance())

        tvCTerms.setText(ss, TextView.BufferType.SPANNABLE)
        var Provincia: ArrayList<String> = Constants.GetProvincia(this@CustomerRegisterActivity)
        editProvince.setOnClickListener(View.OnClickListener {
           if(Provincia.size>0){
               ShowSingleDialog(Provincia,editProvince.text.toString())
           }else{
           Constants.ShowAlertDialog(this)
           }
        })

        buttonSave.setOnClickListener(View.OnClickListener {
            UiHelpers.showProgessBar(window, progressBar)
            if (isValid()) {
                val email = editMail.text.toString()
                val password = editPassword.text.toString()
                val name = editName.text.toString()
                val typeOfBusinessId = (spinnerTypeOfBusiness.selectedItem as TypeOfBusiness).id
                val contactName = editContactName.text.toString()
                val contactMobile = editContactMobile.text.toString()
                val address = editAddress.text.toString()
                val city = editCity.text.toString()
                val zipCode = editZipCode.text.toString()
                val province = editProvince.text.toString()

                val customer = Customer(
                        email = email,
                        password = password,
                        name = name,
                        typeOfBusinessId = typeOfBusinessId,
                        contactName = contactName,
                        contactEmail = "",
                        contactMobile = contactMobile,
                        address = address,
                        city = city,
                        zipCode = zipCode,
                        province = province)

                CustomerService.create(this, customer) { status, data, error ->
                    UiHelpers.hideProgessBar(window, progressBar)
                    if (status) {
                        Toast.makeText(this, "Restaurador creado correctamente", Toast.LENGTH_LONG).show()

                        AuthService.customer = data

                        val intent = Intent(applicationContext, CustomerMainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        when (error) {
                        /* "VAT_DUPLICATED" -> {
                             editVat.setError("VAT ya en uso")
                             requestFocus(editVat, true)
                         }*/
                            "EMAIL_DUPLICATED" -> {
                                editMail.setError("Correo electrónico ya en uso")
                                requestFocus(editMail, true)
                            }
                            else -> Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                UiHelpers.hideProgessBar(window, progressBar)
            }
        })
    }



    fun ShowSingleDialog(provincia: ArrayList<String>, proselected: String) {
         val cs = provincia.toArray(arrayOfNulls<CharSequence>(provincia.size))
         var selectedPosition = 0
         if(proselected.equals("")){
             selectedPosition = 0
         }else{
             cs.indices.forEach { i ->
                 when {
                     cs[i].toString().equals(proselected) -> selectedPosition = i
                 }
             }
         }

        val dialog = AlertDialog.Builder(this).setTitle("Lista de Provincia").setSingleChoiceItems(cs, selectedPosition) { dialog, which ->
            Log.e("00000000000",""+cs[which])
            editProvince.setText(cs[which])
            dialog.dismiss()
        }.create()
        dialog.show()
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun isValid(): Boolean {
        var isValid = true

        //  val vat = editVat.text.toString()
        val email = editMail.text.toString()
        val password = editPassword.text.toString()
        val repeatPassword = editRepeatPassword.text.toString()
        val name = editName.text.toString()
        val typeOfBusiness = spinnerTypeOfBusiness.selectedItem as TypeOfBusiness
        val contactName = editContactName.text.toString()
        val contactMobile = editContactMobile.text.toString()
        val address = editAddress.text.toString()
        val city = editCity.text.toString()
        val zipCode = editZipCode.text.toString()
        val Provice = editProvince.text.toString()
        //  val country = editCountry.text.toString()

        /*   if (vat.isNullOrEmpty()) {
               editVat.setError("Este campo es obligatorio")
               requestFocus(editVat, isValid)
               isValid = false
           }*/

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

        if (password.isNullOrEmpty()) {
            editPassword.setError("Este campo es obligatorio")
            requestFocus(editMail, isValid)
            isValid = false
        }

        if (password != repeatPassword) {
            editPassword.setError("Las contraseñas no son iguales")
            requestFocus(editPassword, isValid)
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

          if (Provice.isNullOrEmpty()) {
              editProvince.setError("Este campo es obligatorio")
              requestFocus(editProvince, isValid)
              isValid = false
          }

        if(!chkCTerms.isChecked){
            Toast.makeText(this, "Por favor, consulte los términos y condiciones", Toast.LENGTH_LONG).show()
            isValid = false
        }

        /*   if (country.isNullOrEmpty()) {
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
