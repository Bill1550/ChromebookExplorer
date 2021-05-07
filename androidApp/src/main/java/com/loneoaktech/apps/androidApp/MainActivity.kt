package com.loneoaktech.apps.androidApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.loneoaktech.apps.androidApp.databinding.ActivityMainBinding
import com.loneoaktech.apps.shared.Greeting

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        title = "Chromebook Explorer"

        viewBinding.platformView.text = greet()

        savedInstanceState ?: let {
            supportFragmentManager.beginTransaction().apply {
                replace( R.id.fragmentContainer, ScreenInfoFragment() )
            }.commit()
        }

    }
}
