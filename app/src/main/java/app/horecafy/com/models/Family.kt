package app.horecafy.com.models

import java.io.Serializable


data class Family(val id: Int,
                  val name: String,
                  val categoryId: Int,
                  val createdOn: String,
                  val visible: Boolean) : Serializable {

    override fun toString() = name
}