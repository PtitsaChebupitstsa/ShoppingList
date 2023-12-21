package com.example.shoppinglistca.data.di

import android.app.Application
import com.example.shoppinglistca.data.AppDatabase
import com.example.shoppinglistca.data.ShopListDao
import com.example.shoppinglistca.data.ShopListRepositoryImpl
import com.example.shoppinglistca.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindShopListRepository(impl:ShopListRepositoryImpl):ShopListRepository

    companion object{
        @Provides
        @ApplicationScope
        fun provideShopListDao (application: Application):ShopListDao{
            return AppDatabase.getInstance(application).shopListDao()
        }

    }
}