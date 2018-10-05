package app.horecafy.com.util

import android.view.View
import android.view.Window
import android.view.WindowManager


class UiHelpers {
    companion object {
        fun showProgessBar(window: Window, view: View){
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            view.setVisibility(View.VISIBLE)
        }

        fun hideProgessBar(window: Window, view: View){
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            view.setVisibility(View.GONE)
        }
    }
}