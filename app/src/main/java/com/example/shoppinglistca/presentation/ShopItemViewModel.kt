package com.example.shoppinglistca.presentation

import androidx.lifecycle.ViewModel
import com.example.shoppinglistca.data.ShopListRepositoryImpl
import com.example.shoppinglistca.domain.AddShopItemUseCase
import com.example.shoppinglistca.domain.EditShopItemUseCase
import com.example.shoppinglistca.domain.GetShopItemUseCase
import com.example.shoppinglistca.domain.ShopItem

class ShopItemViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl
    private val getShopListUseCase = GetShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)


    fun getShopItem (shopItemId: Int){
       val item =getShopListUseCase.getShopItem(shopItemId)
    }
    fun addShopItem(inputName:String?,inputCount:String?){
        val name =  parsName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name,count)
        if (fieldsValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
        }
    }
    fun editShopItem(inputName:String?,inputCount:String?){
        val name = parsName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid){
        editShopItemUseCase.editShopItem(ShopItem(name,count,true))}
    }

    private fun parsName(inputName:String?):String{
        return inputName?.trim().toString() ?: ""
    }

    private fun parseCount(inputCount:String?):Int{
        return inputCount?.trim().toString().toIntOrNull() ?: 0
//        val a = try {
//inputCount?.trim()?.toInt() ?: 0
//        }catch (e:Exception){
//0
//        }
    }
    private fun validateInput(name:String,count:Int):Boolean{
        var result = true
        if (name.isBlank()) {
            //TODO: Show error input name
            result = false
        }
        if (count<=0 ){
            //TODO: Show error input count
            result =false
        }
        return result
    }
}