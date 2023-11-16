package com.example.shoppinglistca.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoppinglistca.domain.ShopItem

@Entity(tableName = "shop_items")
data class ShopItemDbModel(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    val name:String,
    val count:Int,
    val enabled:Boolean
)