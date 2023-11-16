package com.example.shoppinglistca.domain

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {

    suspend fun deleteShopItem (shopItem: ShopItem){
       shopListRepository.deleteShopItem(shopItem)
    }
}