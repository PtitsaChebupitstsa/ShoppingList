package com.example.shoppinglistca.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglistca.data.ShopListRepositoryImpl
import com.example.shoppinglistca.domain.DeleteShopItemUseCase
import com.example.shoppinglistca.domain.EditShopItemUseCase
import com.example.shoppinglistca.domain.GetShopListUseCase
import com.example.shoppinglistca.domain.ShopItem

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl
    /*
    Не правильная реализация репозитория связана с тем что нужно изучить инекцию зависимости
    Нарушение заключается в том что Presentation слой не должен ничего занать о Data слое
    но может все знать о Domain слое так как он являет в нашем приложении главным
     */

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)

    val shopList = MutableLiveData<List<ShopItem>>()

    fun getShopList() {
        val list = getShopListUseCase.getShopList()
        shopList.value =
            list //добавляем обновление в наше LiveData value работает только в главном потоке
    }

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
        getShopList()
    }

    fun changeEnableState(shopItem: ShopItem) {
   val newItem = shopItem.copy(enabled = !shopItem.enabled)
       editShopItemUseCase.editShopItem(newItem)
        getShopList()
    }

}