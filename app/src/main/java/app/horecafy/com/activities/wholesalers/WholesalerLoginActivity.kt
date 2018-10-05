package app.horecafy.com.activities.wholesalers

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
import kotlinx.android.synthetic.main.activity_wholesaler_login.*

class WholesalerLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wholesaler_login)
        val preferences = SharedPreferencesHelper()

        // Only for debug
        /*editMailWholesaler.setText("apurv@gmail.com")
        editPasswordWholesaler.setText("123456")*/

        buttonLoginWholesaler.setOnClickListener(View.OnClickListener {
            if (Constants.isNetworkAvailable(this)) {
                if (isValid()) {
                    val email = editMailWholesaler.text.toString()
                    val password = editPasswordWholesaler.text.toString()

                    UiHelpers.showProgessBar(window, progressBarWholesaler)

                    val login = Login(email, password, TypeUser.WHOLESALER.value)
                    AuthService.loginAsWholesaler(this, login) { status, data, error ->
                        UiHelpers.hideProgessBar(window, progressBarWholesaler)
                        if (data == null) {
                            Toast.makeText(this, "El correo eletrónico o la contraseña son incorrectos.", Toast.LENGTH_LONG).show()
                        } else {
                            val intent = Intent(applicationContext, WholesalerMainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                    }
                }
            } else {
                Toast.makeText(this, Constants.NO_INTERNET_CONNECTION_AVAILABLE, Toast.LENGTH_LONG).show()
            }
        })

        tvForgotPasswordWholesaler.setOnClickListener(View.OnClickListener {

            val userType = TypeUser.WHOLESALER.value

            val intent = ForgotPasswordActivity.intent(this, userType)
            startActivity(intent)

        })

        chRemembermeWholesaler?.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                preferences.setValue(this@WholesalerLoginActivity, Constants.REMEMBER, "true")
            }else{
                preferences.setValue(this@WholesalerLoginActivity, Constants.REMEMBER, "false")
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

        val email = editMailWholesaler.text.toString()
        val password = editPasswordWholesaler.text.toString()

        if (email.isNullOrEmpty()) {
            editMailWholesaler.setError("Este campo es obligatorio")
            isValid = false
        }

        if (password.isNullOrEmpty()) {
            editPasswordWholesaler.setError("Este campo es obligatorio")
            isValid = false
        }

        return isValid
    }
}