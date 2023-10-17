package com.example.shoppinglistca.domain

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun getShopList(): List<ShopItem>

    fun getShopItem(shopItemId: Int):ShopItem

    fun editShopItem(shopItem: ShopItem)

    fun deleteShopItem (shopItem: ShopItem)
}