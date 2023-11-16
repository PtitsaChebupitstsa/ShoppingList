package com.example.shoppinglistca.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglistca.data.ShopListRepositoryImpl
import com.example.shoppinglistca.domain.AddShopItemUseCase
import com.example.shoppinglistca.domain.EditShopItemUseCase
import com.example.shoppinglistca.domain.GetShopItemUseCase
import com.example.shoppinglistca.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShopListRepositoryImpl(application)
    private val getShopListUseCase = GetShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen
    private val scope = CoroutineScope(Dispatchers.IO)

    fun getShopItem(shopItemId: Int) {
        scope.launch{
            val item = getShopListUseCase.getShopItem(shopItemId)
            _shopItem.postValue(item) //было value = item
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parsName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        scope.launch{
            if (fieldsValid) {
                val shopItem = ShopItem(name, count, true)
                addShopItemUseCase.addShopItem(shopItem)
                finishWork()
            }
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parsName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        scope.launch{
            if (fieldsValid) {
                shopItem.value?.let {
                    val item = it.copy(name = name, count = count)
                    editShopItemUseCase.editShopItem(item)
                    finishWork()
                }
            }
        }

    }

    private fun parsName(inputName: String?): String {
        return inputName?.trim().toString() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return inputCount?.trim().toString().toIntOrNull() ?: 0
//        val a = try {
//inputCount?.trim()?.toInt() ?: 0
//        }catch (e:Exception){
//0
//        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    public fun resetErrorInputName() {
        _errorInputName.value = false
    }

    public fun resetErrorInputCount() {
        _errorInputCount.value = false
    }


    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}