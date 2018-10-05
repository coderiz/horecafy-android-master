package app.horecafy.com.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.models.ForgotPassword
import app.horecafy.com.services.PasswordRecoveryService
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    companion object {
        val INTENT_TYPE_USER = "INTENT_TYPE_USER"

        fun intent(context: Context, typeUser: String): Intent {
            val intent = Intent(context, ForgotPasswordActivity::class.java)
            intent.putExtra(INTENT_TYPE_USER, typeUser);
            return intent
        }
    }

    var strTypeUser: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Get strTypeUser from intent
        if (strTypeUser.equals("")) {
            strTypeUser = intent.getStringExtra(INTENT_TYPE_USER)
        }

        btnSubmit.setOnClickListener(View.OnClickListener {
            if (isValid()) {
                val email = editMailId.text.toString()

                UiHelpers.showProgessBar(window, rlProgressBarForgotPassword)

                val forgotPassword = ForgotPassword(email, strTypeUser)
                PasswordRecoveryService.forgotPassword(this, forgotPassword) { status, data, error ->
                    UiHelpers.hideProgessBar(window, rlProgressBarForgotPassword)
                    if (data == null) {
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                    } else {
                        val intent = ResetPasswordActivity.intent(this, strTypeUser, email)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        })
    }

    private fun isValid(): Boolean {
        var isValid = true

        val email = editMailId.text.toString()

        if (email.isNullOrEmpty()) {
            editMailId.setError("Este campo es obligatorio")
            isValid = false
        }

        return isValid
    }
}
