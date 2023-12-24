package com.example.shoppinglistca.presentation

import android.accessibilityservice.AccessibilityService.SoftKeyboardController.OnShowModeChangedListener
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView


import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistca.R
import com.example.shoppinglistca.databinding.ActivityMainBinding
import com.example.shoppinglistca.databinding.ItemShopEnabledBinding
import com.example.shoppinglistca.domain.ShopItem

import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), ShopItemFragment.Companion.OnEditingFinishListener {
    private lateinit var viewModel: MainViewModel
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var shopListAdapter: ShopListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (application as ShopListApp).component
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
        binding.buttonAddShopItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddMItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
        //обращение к нашему Content Provider
        thread {

            val cursor = contentResolver.query(
                Uri.parse("content://com.kerugeru.shoppinglist/shop_items"),
                null,
                null,
                null,
                null,
                null
            )
            while (cursor?.moveToNext() == true) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val count = cursor.getInt(cursor.getColumnIndexOrThrow("count"))
                val enabled = cursor.getInt(cursor.getColumnIndexOrThrow("enabled")) > 0
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )
                Log.d("MainActivity22",shopItem.toString())
        }
            //курсор обязательно нужно закрывать
            cursor?.close()
    }
}


private fun isOnePaneMode(): Boolean {
    return binding.shopItemContainer == null
}

private fun launchFragment(fragment: Fragment) {
    supportFragmentManager.popBackStack()
    supportFragmentManager.beginTransaction()
        .replace(R.id.shop_item_container, fragment)
        .addToBackStack(null)
        .commit()
}

private fun setupRecyclerView() {
    with(binding.rvShopList) {
        shopListAdapter = ShopListAdapter()
        adapter = shopListAdapter
        recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.ENABLE_OBJ,
            ShopListAdapter.MAX_POOL_SIZE
        )
        recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.ENABLE_OBJ,
            ShopListAdapter.MAX_POOL_SIZE
        )
    }
    setupLongClickListener()
    setupClickListener()
    setupSwipeListener(binding.rvShopList)
}

private fun setupSwipeListener(rvShopList: RecyclerView) {
    val callback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val item = shopListAdapter.currentList[viewHolder.adapterPosition]
            viewModel.deleteShopItem(item)
        }
    }
    val itemTouchHelper = ItemTouchHelper(callback)
    itemTouchHelper.attachToRecyclerView(rvShopList)
}

private fun setupClickListener() {
    shopListAdapter.onShopItemClickListener = {
        if (isOnePaneMode()) {
            val intent = ShopItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        } else {
            launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
        }
    }
}

private fun setupLongClickListener() {
    shopListAdapter.onShopItemLongClickListener = {
        viewModel.changeEnableState(it)
    }
}

override fun onEditingFinish() {
    Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
    supportFragmentManager.popBackStack()
}
}