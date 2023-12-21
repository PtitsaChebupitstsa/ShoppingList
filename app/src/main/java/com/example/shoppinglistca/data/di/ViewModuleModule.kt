package com.example.shoppinglistca.data.di

import androidx.lifecycle.ViewModel
import com.example.shoppinglistca.presentation.MainViewModel
import com.example.shoppinglistca.presentation.ShopItemViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModuleModule {
    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel:MainViewModel):ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ShopItemViewModel::class)
    fun bindShopItemViewModel(viewModel:ShopItemViewModel):ViewModel
}