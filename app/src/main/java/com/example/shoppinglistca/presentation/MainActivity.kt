package com.example.shoppinglistca.presentation

import android.accessibilityservice.AccessibilityService.SoftKeyboardController.OnShowModeChangedListener
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView


import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistca.R

import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private var shopItemContainer :FragmentContainerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //
        shopItemContainer = findViewById(R.id.shop_item_container)
        //

        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }

        val buttonAddItem = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
      //
        buttonAddItem.setOnClickListener {
             if (isOnePaneMode()) {
                 val intent = ShopItemActivity.newIntentAddMItem(this)
                 startActivity(intent)
             }else{
                 launchFragment(ShopItemFragment.newInstanceAddItem())
             }


        }//
    }


    private fun isOnePaneMode():Boolean{
        return  shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()//удалит из бекстека 1 фрагмент а если ничего нет то ничего не делает
        supportFragmentManager.beginTransaction().replace( R.id.shop_item_container,fragment).addToBackStack(null).commit()
            //addToBackStack добавит фрагмент в стек
    }








    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(rvShopList) {
            shopListAdapter = ShopListAdapter()
            rvShopList.adapter = shopListAdapter
            rvShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ENABLE_OBJ,
                ShopListAdapter.MAX_POOL_SIZE
            )
            rvShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.DISABLE_OBJ,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)
    }


    private fun setupSwipeListener(rvShopList: RecyclerView?) {
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

    private fun setupClickListener() {//
        shopListAdapter.onShopItemClickListener = {
          if (isOnePaneMode()) {
              val intent = ShopItemActivity.newIntentEditItem(this, it.id)
              startActivity(intent)
          }else{
              launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
          }
        }//
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

}


