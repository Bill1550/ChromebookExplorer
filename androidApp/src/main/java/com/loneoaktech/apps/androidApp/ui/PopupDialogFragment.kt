package com.loneoaktech.apps.androidApp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.loneoaktech.apps.androidApp.databinding.DialogPopupTestBinding
import com.loneoaktech.apps.androidApp.utilities.lazyViewBinding
import com.loneoaktech.apps.androidApp.utilities.withViews
import com.loneoaktech.apps.platform.utilities.goFullScreen

class PopupDialogFragment : DialogFragment() {

    private val bindingHolder = lazyViewBinding { DialogPopupTestBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindingHolder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingHolder.withViews {
            dismissButton.setOnClickListener { dismiss() }
        }

//        dialog?.window?.goFullScreen()
//        activity?.window?.goFullScreen() -- didn't work
    }

//    override fun onStart() {
//        super.onStart()
//        activity?.window?.goFullScreen()
//    }


}