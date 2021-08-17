package com.loneoaktech.apps.androidApp

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.loneoaktech.apps.androidApp.databinding.ActivityMainBinding
import com.loneoaktech.apps.androidApp.ui.PopupTrialFragment
import com.loneoaktech.apps.androidApp.ui.ScreenInfoFragment
import com.loneoaktech.apps.platform.utilities.goFullScreen
import com.loneoaktech.apps.shared.Greeting
import timber.log.Timber

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        title = "Chromebook Explorer"

        viewBinding.platformView.text = greet()

        savedInstanceState ?: let {
            supportFragmentManager.beginTransaction().apply {
                replace( R.id.fragmentContainer, PopupTrialFragment() )
            }.commit()
        }
//
//        viewBinding.root.setOnFocusChangeListener { v, hasFocus ->
//            Timber.i("onFocusChange, hasFocus=$hasFocus")
//            if ( hasFocus )
//                goFullScreen(window, v)
//        }
    }

    /**
     * The "standard" place to go full screen.
     * Doesn't work in some cases (i.e. when user maximizes the app)
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Timber.i("onWindowFocusChanged, hasFocus=$hasFocus")
//        if ( hasFocus )
//            goFullScreen( window, window.decorView )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Timber.i("onConfigChanged")
    }
}
