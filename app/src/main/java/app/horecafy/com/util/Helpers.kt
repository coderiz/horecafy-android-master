package app.horecafy.com.util

import java.text.SimpleDateFormat
import java.util.*


class Helpers {
    companion object {
        fun StringToDate(string: String): Date {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            try {
                val date = format.parse(string)
                return date
            } catch (e: Exception){
                return Date()
            }
        }

        fun DateToString(date: Date): String{
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            try {
                val string = dateFormat.format(date)
                return string
            } catch (e: Exception) {
                return ""
            }
        }
    }
}