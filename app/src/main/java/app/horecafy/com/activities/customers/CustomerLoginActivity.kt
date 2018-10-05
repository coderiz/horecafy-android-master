package app.horecafy.com.activities.customers

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.activities.ForgotPasswordActivity
import app.horecafy.com.activities.MainActivity
import app.horecafy.com.models.Login
import app.horecafy.com.services.AuthService
import app.horecafy.com.util.Constants
import app.horecafy.com.util.SharedPreferencesHelper
import app.horecafy.com.util.TypeUser
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_customer_login.*

class CustomerLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_login)
        val preferences = SharedPreferencesHelper()
        // Only for debug
        /*editMail.setText("testapurv@gmail.com")
        editPassword.setText("123456")*/

        buttonLogin.setOnClickListener(View.OnClickListener {

            if (Constants.isNetworkAvailable(this)) {

                if (isValid()) {
                    val email = editMail.text.toString()
                    val password = editPassword.text.toString()
                    UiHelpers.showProgessBar(window, progressBar)

                    val login = Login(email, password, TypeUser.CUSTOMER.value)
                    AuthService.loginAsCustomer(this, login) { status, data, error ->
                        UiHelpers.hideProgessBar(window, progressBar)
                        if (data == null) {
                            Toast.makeText(this, "El correo eletrónico o la contraseña son incorrectos.", Toast.LENGTH_LONG).show()
                        } else {
                            val intent = Intent(applicationContext, CustomerMainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                    }
                }
            } else {
                Toast.makeText(this, Constants.NO_INTERNET_CONNECTION_AVAILABLE, Toast.LENGTH_LONG).show()
            }
        })

        tvForgotPassword.setOnClickListener(View.OnClickListener {

            val userType = TypeUser.CUSTOMER.value

            val intent = ForgotPasswordActivity.intent(this, userType)
            startActivity(intent)

        })

        chRememberme?.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                preferences.setValue(this@CustomerLoginActivity, Constants.REMEMBER, "true")
            }else{
                preferences.setValue(this@CustomerLoginActivity, Constants.REMEMBER, "false")
            }
        }

    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun isValid(): Boolean {
        var isValid = true

        val email = editMail.text.toString()
        val password = editPassword.text.toString()

        if (email.isNullOrEmpty()) {
            editMail.setError("Este campo es obligatorio")
            isValid = false
        }

        if (password.isNullOrEmpty()) {
            editPassword.setError("Este campo es obligatorio")
            isValid = false
        }

        return isValid
    }
}