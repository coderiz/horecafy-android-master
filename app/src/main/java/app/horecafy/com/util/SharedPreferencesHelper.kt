package app.horecafy.com.util

import android.content.Context


class SharedPreferencesHelper() {

    private val FILE_KEY = "preferences"

    fun getValue(context: Context, key: String): String {
        val sharedPref = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE)
        return sharedPref.getString(key, "")
    }

    fun setValue(context: Context, key: String, value: String) {
        val sharedPref = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, value);
        editor.commit()
    }

}