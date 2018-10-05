package app.horecafy.com.models

import java.io.Serializable


data class Category(val id: Int,
                    var name: String,
                    val createdOn: String,
                    val image: String,
                    val visible: Boolean,
                    val familyCount: Int,
                    val totalSelectedFamilies: Int,
                    val totalFamilies: Int,
                    val isCheck: Boolean) : Serializable