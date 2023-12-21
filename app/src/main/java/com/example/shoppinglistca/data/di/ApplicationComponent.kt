package com.example.shoppinglistca.data.di

import android.app.Application
import com.example.shoppinglistca.presentation.MainActivity
import com.example.shoppinglistca.presentation.ShopItemActivity
import com.example.shoppinglistca.presentation.ShopItemFragment
import com.example.shoppinglistca.presentation.ShopListApp
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class,ViewModuleModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: ShopItemFragment)


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}