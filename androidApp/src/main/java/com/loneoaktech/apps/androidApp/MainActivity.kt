package com.loneoaktech.apps.androidApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.loneoaktech.apps.androidApp.databinding.ActivityMainBinding
import com.loneoaktech.apps.androidApp.ui.PopupTrialFragment
import com.loneoaktech.apps.androidApp.ui.ScreenInfoFragment
import com.loneoaktech.apps.shared.Greeting

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

    }
}
