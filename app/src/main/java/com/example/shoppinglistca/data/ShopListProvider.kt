package com.example.shoppinglistca.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log

class ShopListProvider:ContentProvider() {
    override fun onCreate(): Boolean {
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
       Log.d("ShopListProvider","query $uri")
        return null
    }

    override fun getType(uri: Uri): String? {
        //позволяет опредилить тип данных
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        //позволяет вставлять данные
        TODO("Not yet implemented")
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
}