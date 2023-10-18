package com.example.shoppinglistca.data

import com.example.shoppinglistca.domain.ShopItem
import com.example.shoppinglistca.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()

    private var autoIncrementId = 0

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
            shopList.add(shopItem)
        }
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId }
            ?: throw RuntimeException("Эллемент с таким id не найден")
    }

    override fun editShopItem(shopItem: ShopItem) {
       val oldShopItem = getShopItem(shopItem.id)
        deleteShopItem(oldShopItem)
        addShopItem(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }
}