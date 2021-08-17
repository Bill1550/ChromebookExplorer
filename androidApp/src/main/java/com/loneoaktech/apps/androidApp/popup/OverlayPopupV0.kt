package com.loneoaktech.apps.androidApp.popup

import android.graphics.Point
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import timber.log.Timber

class OverlayPopupV0(
    private val activity: ComponentActivity,
    @LayoutRes private val layoutId: Int,
    private val anchorView: View?,
    private val dismissOnOutsideClick: Boolean = false,
    private val doOnShow: ((OverlayPopupV0, View)->Unit)? = null,
    private val binder: ((OverlayPopupV0, View)->Unit)?
) {
    private var overlayView: View? = null

    private val lifecycleObserver = LifecycleEventObserver { _, event ->
        if ( event == Lifecycle.Event.ON_DESTROY) {
            Timber.i("dismissing on parent onDestroy")
            dismiss()
        }
    }

    fun show() {
        Timber.i("show, layoutId=$layoutId")
        removeOverlay() // make sure not currently showing

        activity.lifecycle.addObserver(lifecycleObserver)


        val rootLoc = IntArray(2).apply {
            getActivityRoot()?.getLocationOnScreen(this)
        }

        val anchorLoc = IntArray(2).apply {
            anchorView?.getLocationOnScreen(this)
        }

        val upperLeft = anchorView?.let { av ->
            Point(
                anchorLoc[0]-rootLoc[0] + av.width/2,
                anchorLoc[1]-rootLoc[1] + av.height/2
            )
        } ?: Point(0,0)


        overlayView = FrameLayout(activity).apply {
            addView(
                createContents(this),
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
//                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
//                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    leftMargin = upperLeft.x
                    topMargin = upperLeft.y
                    gravity = Gravity.TOP or Gravity.START
                }
            )

            setOnClickListener {
                Timber.i("overlay outside click")
                if (dismissOnOutsideClick)
                    dismiss()
            }


            getActivityRoot()?.addView(this)
            Timber.i("requesting focus to overlay=${requestFocus()}")
            doOnShow?.invoke( this@OverlayPopupV0, this)
        }
    }

    fun dismiss() {
        getActivityRoot()?.removeView(overlayView)
        overlayView = null
    }

    private fun createContents( container: ViewGroup): View {
        Timber.i("creating contents")
        val contents = LayoutInflater.from(activity).inflate(layoutId, container, false)
        binder?.invoke( this, contents)
        return contents
    }

    private fun removeOverlay() {
        overlayView?.let {
            getActivityRoot()?.removeView(overlayView)
            activity.lifecycle.removeObserver(lifecycleObserver)
        }
        overlayView = null
    }

    private fun getActivityRoot(): ViewGroup? = activity.findViewById(android.R.id.content) as? ViewGroup

}