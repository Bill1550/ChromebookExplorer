package com.loneoaktech.apps.androidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.loneoaktech.apps.shared.Greeting
import android.widget.TextView
import com.loneoaktech.apps.androidApp.databinding.ActivityMainBinding

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.textView.text = greet()
    }
}
