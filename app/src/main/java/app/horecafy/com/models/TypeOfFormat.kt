package app.horecafy.com.models


data class TypeOfFormat(val id: Int, val name: String, val createdOn: String) {
    override fun toString() = name
}