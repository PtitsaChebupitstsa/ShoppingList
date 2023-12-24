package com.example.shoppinglistca.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.shoppinglistca.domain.ShopItem
import com.example.shoppinglistca.presentation.ShopListApp
import javax.inject.Inject

class ShopListProvider : ContentProvider() {
    private val component by lazy {
        (context as ShopListApp).component
    }

    @Inject
    lateinit var shopListDao: ShopListDao
    @Inject
    lateinit var mapper: ShopListMapper

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.kerugeru.shoppinglist", "shop_items", GET_SHOP_ITEMS_QUERY)
        addURI("com.kerugeru.shoppinglist", "shop_items/#", GET_SHOP_BY_ID_QUERY)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        //позволяет запросить список каких то данных
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                shopListDao.getShopListCursor()
            }
            else -> {
                null
            }
        }
    }

    override fun getType(uri: Uri): String? {
        //позволяет опредилить тип данных
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        //позволяет вставлять данные
            when (uriMatcher.match(uri)) {
                GET_SHOP_ITEMS_QUERY -> {
                    if (values == null) return null
                    val id = values.getAsInteger("id")
                    val name = values.getAsString("name")
                    val count = values.getAsInteger("count")
                    val enabled = values.getAsBoolean("enabled")
                    val shopItem = ShopItem(
                        id = id,
                        name = name,
                        count = count,
                        enabled = enabled
                    )
                    shopListDao.addShopItemSync(mapper.mapEntityToDbModel(shopItem))
                }
            }
            return null
        }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        //добавляет данные
        TODO("Not yet implemented")
    }

    override fun update(
        //обновляет данные
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    companion object {
        const val GET_SHOP_ITEMS_QUERY = 100
        const val GET_SHOP_BY_ID_QUERY = 101
    }
}