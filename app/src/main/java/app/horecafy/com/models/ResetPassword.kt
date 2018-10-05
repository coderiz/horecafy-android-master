package app.horecafy.com.models


data class ResetPassword(val email: String, val typeUser: String,
                         val token: String, val password: String)