package com.loneoaktech.apps.platform.utilities

import android.app.AlertDialog
import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import timber.log.Timber

fun Fragment.goFullScreen() {

   goFullScreen( activity?.window, view )

//    activity?.window?.goFullScreen()
}

fun AlertDialog.goFullScreen() {
//    window?.goFullScreen()

}

fun goFullScreen(window: Window?, rootView: View?) {

//       window?.decorView?.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
//            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
//            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//            View.SYSTEM_UI_FLAG_FULLSCREEN

    Timber.i("goFullScreen")
    window?.let { w ->
        rootView?.let { v ->
            WindowCompat.setDecorFitsSystemWindows(w, false)
            WindowInsetsControllerCompat(w,v).let { ctlr ->
                ctlr.hide( WindowInsetsCompat.Type.systemBars() )
                ctlr.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }?: Timber.e("Root view not found")
    } ?: Timber.e("Window not found")

}