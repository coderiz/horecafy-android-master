package app.horecafy.com.activities.customers

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.models.InviteDistributor
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CustomerService
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_customer_invite_distributor.*

class CustomerInviteDistributorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_invite_distributor)

        btnSendInvitation.setOnClickListener(View.OnClickListener { v: View? ->
            if (isValid()) {

                val strFirstName = etFirstName.text.toString()
                val strEmail = etEmail.text.toString()
                val strPhone = etPhone.text.toString()
                val strContact = etContact.text.toString()

                UiHelpers.showProgessBar(window, rlProgressBarInviteDistributor)

                val customerId = AuthService.customer!!.hiddenId!!

                val inviteDistributor = InviteDistributor(customerId, strFirstName, strEmail,
                        strPhone, strContact)

                CustomerService.inviteDistributor(this, inviteDistributor) { status, error ->
                    UiHelpers.hideProgessBar(window, rlProgressBarInviteDistributor)

                    Toast.makeText(this, "Se ha enviado una invitacion a "+strFirstName, Toast.LENGTH_LONG).show()

                    /*val intent = Intent(applicationContext, CustomerMainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)*/
                    finish()
                }
            }
        })
    }

    private fun isValid(): Boolean {
        var isValid = true

        val strFirstName = etFirstName.text.toString()
        val strEmail = etEmail.text.toString()
        val strPhone = etPhone.text.toString()
        val strContact = etContact.text.toString()

        if (strFirstName.isNullOrEmpty()) {
            etEmail.setError("Este campo es obligatorio")
            isValid = false
        }

        if (strEmail.isNullOrEmpty()) {
            etFirstName.setError("Este campo es obligatorio")
            isValid = false
        }

        if (strPhone.isNullOrEmpty()) {
            etPhone.setError("Este campo es obligatorio")
            isValid = false
        }

        if (strContact.isNullOrEmpty()) {
            etContact.setError("Este campo es obligatorio")
            isValid = false
        }

        return isValid
    }
}
