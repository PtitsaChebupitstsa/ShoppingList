package com.example.shoppinglistca.presentation

import android.app.Application
import android.content.res.Configuration
import com.example.shoppinglistca.data.di.DaggerApplicationComponent

class ShopListApp:Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }


}