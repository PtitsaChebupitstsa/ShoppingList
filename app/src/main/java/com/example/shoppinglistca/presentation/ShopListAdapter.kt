package com.example.shoppinglistca.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglistca.R
import com.example.shoppinglistca.databinding.ItemShopDisabledBinding
import com.example.shoppinglistca.databinding.ItemShopEnabledBinding
import com.example.shoppinglistca.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    companion object {
        const val ENABLE_OBJ = 1
        const val DISABLE_OBJ = 0
        const val MAX_POOL_SIZE = 15
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        val layout = when (viewType) {
            ENABLE_OBJ -> R.layout.item_shop_enabled
            DISABLE_OBJ -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )


        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {

        val shopItem = getItem(position)
        val binding= viewHolder.binding

        when(binding){//явный апкаст
            is ItemShopDisabledBinding->{
                //это не одинаковые строки и тут не дублирование
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }
            is ItemShopEnabledBinding->{
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }
        }

        binding.root.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
            true
        }
        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }

    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled) {
            ENABLE_OBJ
        } else {
            DISABLE_OBJ
        }
    }

}