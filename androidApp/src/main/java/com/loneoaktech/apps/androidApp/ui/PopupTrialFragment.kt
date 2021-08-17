package com.loneoaktech.apps.androidApp.ui

import android.app.ActionBar
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.loneoaktech.apps.androidApp.R
import com.loneoaktech.apps.androidApp.databinding.FragmentPopupExplorerBinding
import com.loneoaktech.apps.androidApp.popup.OverlayPopup
import com.loneoaktech.apps.androidApp.utilities.lazyViewBinding
import com.loneoaktech.apps.androidApp.utilities.withViews
import com.loneoaktech.apps.platform.utilities.goFullScreen
import timber.log.Timber

class PopupTrialFragment : Fragment() {

    private val bindingHolder = lazyViewBinding { FragmentPopupExplorerBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = bindingHolder.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingHolder.withViews {
            showDialogButton.setOnClickListener { v -> showDialog(v) }
            showPopupButton.setOnClickListener { v -> showPopup(v)  }
            showDialogFragmentButton.setOnClickListener { v -> showDialogFragment(v) }
            showManualPopup.setOnClickListener { v -> showOverlayPopup() }
        }

    }

    override fun onStart() {
        super.onStart()
        goFullScreen()
    }

    override fun onStop() {
        super.onStop()
        //TODO exitFullScreen()
    }

    private fun showDialog( anchor: View) {
        val dlg = AlertDialog.Builder(requireContext())
            .setTitle("A Generic Alert")
            .setMessage("This should pop up without exiting full screen")
            .setPositiveButton("OK"){ d, _ -> d.dismiss()}
            .create()

        dlg.show()          // causes activity to exit full screen
//        dlg.goFullScreen()  // returns to full screen, but with a noticeable jump.
//        anchor.post {
//            dlg.goFullScreen()
//        }
    }

    private fun showPopup( anchor: View ) {
        val popupView = layoutInflater.inflate(R.layout.layout_popup_test,null)
        val popup = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupView.findViewById<Button>(R.id.dismissButton).setOnClickListener { v ->
            popup.dismiss()
        }

        popup.showAsDropDown(anchor)
    }

    private fun showDialogFragment( anchor: View ) {
         PopupDialogFragment().show(childFragmentManager, "popup")
    }


    private fun showManualPopup() {
        Timber.i("Show manual popup")
        (view as? ViewGroup)?.let { root ->
            val anchor = root.findViewById<View>(R.id.popupAnchor)
            Timber.i("anchor loc: ${anchor.x}, ${anchor.y}")

            val popup = layoutInflater.inflate(R.layout.layout_trial_popup, root, false)

            Timber.i("Root view: ${root.javaClass.simpleName}, kids=${root.childCount}")
            Timber.i("popup size=${popup.width},${popup.height}")
            val lp = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
//                setMargins( 100, 50, 0 ,0)
//                leftMargin = 400
//                topMargin = 400
//                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
//                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
//                topMargin = 400
//                leftMargin = 600
            }
//            val lp = WindowManager.LayoutParams().apply {  <<< using the window manager causes the Full Screen exit.
//                gravity = Gravity.TOP
//                type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
//                token = popup.windowToken
//                height = WindowManager.LayoutParams.WRAP_CONTENT
//                width = WindowManager.LayoutParams.WRAP_CONTENT
//            }

            root.addView(popup, -1, lp )
//            context?.getSystemService<WindowManager>()?.addView(popup, lp)

            popup.elevation = 20f  // Kludge?
//            popup.left = 400
//            popup.top = 400

            popup.findViewById<Button>(R.id.dismissButton)?.run {
                setOnClickListener {
                    Timber.i("click!")
                    val found = root.findViewById<View>(R.id.manualPopup)
                    Timber.i("popup view=$popup, found by id=$found")
                    root.removeView(popup)
//                    context.getSystemService<WindowManager>()?.removeView(popup)
                }

//                popup.postDelayed(250) {
                popup.doOnPreDraw {
//                    popup.left = 400
//                    popup.top = 400

                    Timber.i("Is button visible: $isVisible")
                    Timber.i("Request focus=${requestFocus()}")
                }

            } ?: Timber.e("Button not found")

            popup.x = anchor.x
            popup.y = anchor.y

            root.invalidate()
        }


    }

    private fun showOverlayPopupTest() {
        Timber.i("Show Overlay Popup")
        (view as? ViewGroup)?.let { root ->
            val anchor = root.findViewById<View>(R.id.popupAnchor)
            Timber.i("anchor loc: ${anchor.x}, ${anchor.y}")

            val overlay = layoutInflater.inflate(R.layout.layout_full_screen_overlay_popup, root, false)
            val appRoot = findAppRootView()
            Timber.i("app root view = ${appRoot}")

            overlay.findViewById<Button>(R.id.dismissButton)?.run {
                setOnClickListener {
                    Timber.i("click!")
                    val found = root.findViewById<View>(R.id.manualPopup)
                    Timber.i("popup view=$overlay, found by id=$found")
                    appRoot?.removeView( overlay )
//                    context.getSystemService<WindowManager>()?.removeView(popup)
                }

                overlay.doOnPreDraw {
                    Timber.i("Is button visible: $isVisible")
                    Timber.i("Request focus=${requestFocus()}")
                }
            } ?: Timber.e("Button not found")

//            overlay.elevation = 20f

            overlay.setOnClickListener {
                Timber.i("overlay clicked")
            }

            appRoot?.addView( overlay )

            overlay.findViewById<View>(R.id.popupContainer).apply {
                x = anchor.x
                y = anchor.y
            }

        }
    }

    private fun findAppRootView(): ViewGroup? {
        return activity?.findViewById(android.R.id.content) as? ViewGroup
//        return view?.findViewById(android.R.id.content) as? ViewGroup
    }

    private fun showOverlayPopup() {

        val overlayPopup = OverlayPopup(
            activity = requireActivity(),
            layoutId = R.layout.layout_popup_test,
            anchorView = view?.findViewById<View>(R.id.popupAnchor),
            dismissOnOutsideClick = false,
            doOnShow = { o, v ->
                v.findViewById<Button>(R.id.dismissButton)?.apply {
                    Timber.i("requesting focus to dismiss btn=${requestFocus()}")
                }
            }
        ) { popup, v ->
            v.findViewById<Button>(R.id.dismissButton)?.setOnClickListener {
                Timber.i("calling dismiss on overlay")
                popup.dismiss()
            }
        }

        overlayPopup.show()

    }
}