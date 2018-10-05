package app.horecafy.com.models

import java.util.*


data class TypeOfBusiness(val id: Int, val name: String, val createdOn: String) {
    override fun toString() = name
}