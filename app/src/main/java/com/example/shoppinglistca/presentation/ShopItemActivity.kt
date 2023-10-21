package com.example.shoppinglistca.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppinglistca.R

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel:ShopItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        viewModel.errorInputCount.value
    }
}