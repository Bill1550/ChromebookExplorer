package com.loneoaktech.apps.androidApp

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.loneoaktech.apps.androidApp.databinding.ViewScreenInfoBinding

class ScreenInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val viewBinding = ViewScreenInfoBinding.inflate(LayoutInflater.from(context), this)

    @SuppressLint("SetTextI18n")
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        with(viewBinding){
            orientationValue.text = when( val or=resources.configuration.orientation ){
                Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
                Configuration.ORIENTATION_PORTRAIT -> "Portrait"
                Configuration.ORIENTATION_UNDEFINED -> "Undefined"
                else -> "Not recognized: $or"
            }

            heightValue.text = "${resources.configuration.screenHeightDp} dp"
            widthValue.text = "${resources.configuration.screenWidthDp} dp"
        }
    }
}