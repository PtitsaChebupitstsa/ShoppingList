package com.example.shoppinglistca.domain

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {


    fun getShopList(): List<ShopItem> {
      return shopListRepository.getShopList()
    }

}