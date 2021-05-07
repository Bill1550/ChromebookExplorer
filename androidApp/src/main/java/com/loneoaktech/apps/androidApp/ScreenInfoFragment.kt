package com.loneoaktech.apps.androidApp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.loneoaktech.apps.androidApp.databinding.FragmentScreenInfoBinding
import com.loneoaktech.apps.androidApp.utilities.lazyViewBinding
import com.loneoaktech.apps.androidApp.utilities.withViews
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ScreenInfoFragment : Fragment() {

    private val bindingHolder = lazyViewBinding { FragmentScreenInfoBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = bindingHolder.root

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            while( isActive ) {
                val time = LocalDateTime.now()
                bindingHolder.withViews {
                    timeView.text = time.truncatedTo( ChronoUnit.SECONDS )
                        .format( DateTimeFormatter.ISO_LOCAL_TIME)
                }

                delay( 1_000L - time.nano/1_000_000 )
            }
        }
    }
}