package com.example.shoppinglistca.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import com.example.shoppinglistca.data.ShopListRepositoryImpl
import com.example.shoppinglistca.domain.DeleteShopItemUseCase
import com.example.shoppinglistca.domain.EditShopItemUseCase
import com.example.shoppinglistca.domain.GetShopListUseCase
import com.example.shoppinglistca.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()



    fun deleteShopItem(shopItem: ShopItem) {
        scope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun changeEnableState(shopItem: ShopItem) {

        scope.launch {
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            editShopItemUseCase.editShopItem(newItem)
        }


    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}