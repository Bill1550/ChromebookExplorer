package com.loneoaktech.apps.androidApp.popup

import android.graphics.Point
import android.util.Size
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import timber.log.Timber
import java.lang.ref.WeakReference

typealias OverlayPopupHandler = (OverlayPopup, View?)->Unit

class OverlayPopup(

    /**
     * View to display
     */
    private val popupView: View,

    /**
     * Width in . null ==> LayoutParams.WRAP_CONTENT
     */
    private val width: Int,

    /**
     * Height in pixels, null ==> LayoutParams.WRAP_CONTENT
     */
    private val height: Int
    // always focusable

) {

    var dismissOnOutsideTouch: Boolean = true

    private val activityRef = WeakReference(
        (popupView.context as? ComponentActivity) ?:
            throw IllegalArgumentException("View context must be from an activity")
    )

    private val activity: ComponentActivity
        get() = activityRef.get() ?: throw IllegalStateException("Popup is detached from activity")

    private var onDismissHandler: OverlayPopupHandler? = null

    fun showAtLocation(
        anchorView: View,
        gravity: Int,
        offset: Size,
        onShowHandler: OverlayPopupHandler? = null,
        onDismissHandler: OverlayPopupHandler? = null
    ) {
        Timber.i("Show at offset: $offset")

        removeOverlay() // make sure we aren't nesting
        this.onDismissHandler = onDismissHandler

        overlayView = FrameLayout(activity).apply {
            addView(popupView, createLayoutParams( computePopupLoc(anchorView, gravity, offset)) )

            setOnClickListener {
                if (dismissOnOutsideTouch)
                    dismiss()
            }

            popupView.doOnPreDraw { onShowHandler?.invoke(this@OverlayPopup, popupView) }

            getActivityRoot()?.addView(this) // TODO add layout params
        }

    }

    fun dismiss() {
        if ( removeOverlay() ) {
            onDismissHandler?.invoke(this, null)
            onDismissHandler = null
        }
    }

    private var overlayView: View? = null

    private val lifecycleObserver = LifecycleEventObserver { _, event ->
        if ( event == Lifecycle.Event.ON_DESTROY) {
            Timber.i("dismissing on parent onDestroy")
            dismiss()
        }
    }

    private fun removeOverlay(): Boolean {
        return overlayView?.let {
            getActivityRoot()?.removeView(overlayView)
            activity.lifecycle.removeObserver(lifecycleObserver)
            overlayView = null
            true
        } ?: false

    }

    private fun getActivityRoot(): ViewGroup? = activity.findViewById(android.R.id.content) as? ViewGroup

    private fun computePopupLoc( anchor: View, gravity: Int, offset: Size): Point {
        val rootLoc = getActivityRoot()?.screenLocation ?: return Point(0,0)
        val anchorLoc = anchor.screenLocation

        return Point(
            anchorLoc.x - rootLoc.x + offset.width,
            anchorLoc.y - rootLoc.y + offset.height
        )
    }

    private fun createLayoutParams( loc: Point ): FrameLayout.LayoutParams {
        return FrameLayout.LayoutParams(height, width).apply {
            leftMargin = loc.x
            topMargin = loc.y
            gravity = Gravity.TOP or Gravity.START
        }
    }

    private val View.screenLocation: Point
        get() {
            val a = IntArray(2)
            getLocationOnScreen(a)
            return Point(a[0], a[1])
        }
}