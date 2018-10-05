package app.horecafy.com.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.models.ResetPassword
import app.horecafy.com.services.PasswordRecoveryService
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    companion object {
        val INTENT_TYPE_USER = "INTENT_TYPE_USER"
        val INTENT_EMAIL_ID = "INTENT_EMAIL_ID"

        fun intent(context: Context, typeUser: String, emailId: String): Intent {
            val intent = Intent(context, ResetPasswordActivity::class.java)
            intent.putExtra(INTENT_TYPE_USER, typeUser);
            intent.putExtra(INTENT_EMAIL_ID, emailId);
            return intent
        }
    }

    var strTypeUser: String = ""
    var strEmailId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        // Get strTypeUser from intent
        if (strTypeUser.equals("")) {
            strTypeUser = intent.getStringExtra(ResetPasswordActivity.INTENT_TYPE_USER)
        }

        // Get strEmailId from intent
        if (strEmailId.equals("")) {
            strEmailId = intent.getStringExtra(ResetPasswordActivity.INTENT_EMAIL_ID)
        }

        btnSubmitResetPassword.setOnClickListener(View.OnClickListener {
            if (isValid()) {
                val token = etToken.text.toString()
                val password = etNewPassword.text.toString()

                UiHelpers.showProgessBar(window, rlProgressBarResetPassword)

                val forgotPassword = ResetPassword(strEmailId, strTypeUser, token, password)
                PasswordRecoveryService.resetPassword(this, forgotPassword) { status, data, error ->
                    UiHelpers.hideProgessBar(window, rlProgressBarResetPassword)
                    if (data == null) {
                        Toast.makeText(this, "Algo salió mal. Inténtalo de nuevo.", Toast.LENGTH_LONG).show()
                    } else {
                        /*val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)*/
                        Toast.makeText(this, "Tu contraseña ha cambiado con éxito.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        })
    }

    private fun isValid(): Boolean {

        var isValid = true

        val token = etToken.text.toString()
        val newPassword = etNewPassword.text.toString()
        val repeatNewPassword = etRepeatNewPassword.text.toString()

        if (token.isNullOrEmpty()) {
            etToken.setError("Este campo es obligatorio")
            isValid = false
        }

        if (newPassword.isNullOrEmpty()) {
            etNewPassword.setError("Este campo es obligatorio")
            isValid = false
        }

        if (repeatNewPassword.isNullOrEmpty()) {
            etRepeatNewPassword.setError("Este campo es obligatorio")
            isValid = false
        }

        if (!newPassword.equals(repeatNewPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }
}