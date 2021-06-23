package com.loneoaktech.apps.androidApp.ui

import android.app.ActionBar
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.loneoaktech.apps.androidApp.R
import com.loneoaktech.apps.androidApp.databinding.FragmentPopupExplorerBinding
import com.loneoaktech.apps.androidApp.utilities.lazyViewBinding
import com.loneoaktech.apps.androidApp.utilities.withViews
import com.loneoaktech.apps.platform.utilities.goFullScreen

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
        dlg.goFullScreen()  // returns to full screen, but with a noticeable jump.
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
}