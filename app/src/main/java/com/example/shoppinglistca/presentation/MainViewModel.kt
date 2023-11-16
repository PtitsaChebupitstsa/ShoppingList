package com.example.shoppinglistca.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import com.example.shoppinglistca.data.ShopListRepositoryImpl
import com.example.shoppinglistca.domain.DeleteShopItemUseCase
import com.example.shoppinglistca.domain.EditShopItemUseCase
import com.example.shoppinglistca.domain.GetShopListUseCase
import com.example.shoppinglistca.domain.ShopItem

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)
    /*
    Не правильная реализация репозитория связана с тем что нужно изучить инекцию зависимости
    Нарушение заключается в том что Presentation слой не должен ничего занать о Data слое
    но может все знать о Domain слое так как он являет в нашем приложении главным
     */

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()



    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
   val newItem = shopItem.copy(enabled = !shopItem.enabled)
       editShopItemUseCase.editShopItem(newItem)
    }

}