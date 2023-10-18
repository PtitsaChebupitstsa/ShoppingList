package com.example.shoppinglistca.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun getShopList(): LiveData<List<ShopItem>>

    fun getShopItem(shopItemId: Int):ShopItem

    fun editShopItem(shopItem: ShopItem)

    fun deleteShopItem (shopItem: ShopItem)
}