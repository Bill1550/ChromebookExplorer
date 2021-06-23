package com.loneoaktech.apps.platform.utilities

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment

fun Fragment.goFullScreen() {
    // TODO API 30 version

    activity?.window?.goFullScreen()
}

fun AlertDialog.goFullScreen() {
    window?.goFullScreen()
}

fun Window.goFullScreen() {
    decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
        View.SYSTEM_UI_FLAG_FULLSCREEN
}