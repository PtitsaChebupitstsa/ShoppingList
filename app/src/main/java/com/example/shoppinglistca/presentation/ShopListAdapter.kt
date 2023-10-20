package com.example.shoppinglistca.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistca.R
import com.example.shoppinglistca.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {
    var count = 0

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onShopItemLongClickListener: ((ShopItem)->Unit)? = null
    var onShopItemClickListener:((ShopItem)->Unit)?=null

    companion object {
        const val ENABLE_OBJ = 1
        const val DISABLE_OBJ = 0
        const val MAX_POOL_SIZE = 15

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("ShopListAdapter", "onCreateViewHolder,count:${++count}")
        val layout = when (viewType) {
            ENABLE_OBJ -> R.layout.item_shop_enabled
            DISABLE_OBJ -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        viewHolder.tvName.text = "${shopItem.name}"
        viewHolder.tvCount.text = shopItem.count.toString()

        viewHolder.view.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
            true
        }
        viewHolder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }


    }

    override fun onViewRecycled(viewHolder: ShopItemViewHolder) {
        super.onViewRecycled(viewHolder)
    }

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) {
            ENABLE_OBJ
        } else {
            DISABLE_OBJ
        }
    }

    //    override fun getItemViewType(position: Int): Int {
//        return position
    // проверяет позицию обьекта и уже имеющегося вьюхолдера и они конечно не совпадают и создается
    //новый...по этому position не используй...а так используется где то 16 так как 8 на создается сразу
    //и еще 8 создаются с красным цветом
//    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }



}