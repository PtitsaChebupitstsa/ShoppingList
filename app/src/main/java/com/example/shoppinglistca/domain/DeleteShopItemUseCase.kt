package com.example.shoppinglistca.domain

import javax.inject.Inject

class DeleteShopItemUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {

    suspend fun deleteShopItem (shopItem: ShopItem){
       shopListRepository.deleteShopItem(shopItem)
    }
}