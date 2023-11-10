package com.example.shoppinglistca.presentation

import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.shoppinglistca.R
import com.google.android.material.textfield.TextInputLayout
import java.lang.Error

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInoutLayout: TextInputLayout,isError: Boolean){
    val message = if (isError) {
        textInoutLayout.context.getString(R.string.error_input_name)
    } else {
        null
    }
    textInoutLayout.error = message
}

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInoutLayout: TextInputLayout,isError: Boolean){
    val message = if (isError) {
        textInoutLayout.context.getString(R.string.error_input_count)
    } else {
        null
    }
    textInoutLayout.error = message
}