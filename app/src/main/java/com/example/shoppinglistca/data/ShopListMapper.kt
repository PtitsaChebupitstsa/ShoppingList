package com.example.shoppinglistca.data

import com.example.shoppinglistca.domain.ShopItem
import javax.inject.Inject

class ShopListMapper @Inject constructor() {
    fun mapEntityToDbModel(shopItem: ShopItem)=ShopItemDbModel(
        name = shopItem.name,
        id = shopItem.id,
        count = shopItem.count,
        enabled = shopItem.enabled
    )
    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel)= ShopItem(
        id = shopItemDbModel.id,
        name = shopItemDbModel.name,
        count = shopItemDbModel.count,
        enabled = shopItemDbModel.enabled
    )
    fun mapListDbModelToEntity(list: List<ShopItemDbModel>)= list.map {
        mapDbModelToEntity(it)
    }

}