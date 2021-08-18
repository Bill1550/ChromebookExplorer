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

typealias OverlayPopupHandler = (FocusablePopup, View?)->Unit


/**
 * Displays a popup view that is focusable, without causing ghe main window to exit full
 * screen, like the standard PopupWindow does.
 *
 * Creates an invisible view that covers the current activity to capture all external clicks
 * and then renders the supplied popup view on top of that window.
 */
class FocusablePopup(

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

    /**
     * S
     */
    var dismissOnOutsideTouch: Boolean = true

    private val activityRef = WeakReference(
        (popupView.context as? ComponentActivity) ?:
            throw IllegalArgumentException("View context must be from an activity")
    )

    private val activity: ComponentActivity
        get() = activityRef.get() ?: throw IllegalStateException("Popup is detached from activity")

    private var onDismissHandler: OverlayPopupHandler? = null

    /**
     * Displays the popup relative to the parent window.
     * Gravity specifies which side is the anchor.
     * Offset is from the anchor side.
     */
    fun showAtLocation(
        gravity: Int,
        offset: Size,
        onShowHandler: OverlayPopupHandler? = null,
        onDismissHandler: OverlayPopupHandler? = null
    ) {
        Timber.i("Show at offset: $offset")

        removeOverlay() // make sure we aren't nesting
        this.onDismissHandler = onDismissHandler

        overlayView = FrameLayout(activity).apply {
            addView(
                popupView,
                createLayoutParams( computeAtLocationGravity(gravity)).apply {
                    x = offset.width.toFloat()
                    y = offset.height.toFloat()
                }
            )

            setOnClickListener {
                if (dismissOnOutsideTouch)
                    dismiss()
            }

            popupView.doOnPreDraw { onShowHandler?.invoke(this@FocusablePopup, popupView) }

            getActivityRoot()?.addView(this) // TODO add layout params
        }
    }


    fun showRelative( home: View, anchorGravity: Int, popupGravity: Int, offset: Size ) {
        // TODO
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

    private fun computePopupLoc( anchor: View, offset: Size): Point {
        val rootLoc = getActivityRoot()?.screenLocation ?: return Point(0,0)
        val anchorLoc = anchor.screenLocation

        return Point(
            anchorLoc.x - rootLoc.x + offset.width,
            anchorLoc.y - rootLoc.y + offset.height
        )
    }

    private fun computeAtLocationGravity( gravityParam: Int): Int =
        if ( gravityParam == Gravity.NO_GRAVITY) Gravity.TOP or Gravity.START else gravityParam

    private fun createLayoutParams( gravity: Int ): FrameLayout.LayoutParams {
        return FrameLayout.LayoutParams(height, width).apply {
            this.gravity = gravity
        }
    }

    private val View.screenLocation: Point
        get() {
            val a = IntArray(2)
            getLocationOnScreen(a)
            return Point(a[0], a[1])
        }
}