package com.example.shoppinglistca.presentation

import android.accessibilityservice.AccessibilityService.SoftKeyboardController.OnShowModeChangedListener
import android.content.Intent
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

import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() ,ShopItemFragment.Companion.OnEditingFinishListener{

private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }



        binding.root.setOnClickListener {
             if (isOnePaneMode()) {
                 val intent = ShopItemActivity.newIntentAddMItem(this)
                 startActivity(intent)
             }else{
                 launchFragment(ShopItemFragment.newInstanceAddItem())
             }


        }//
    }


    private fun isOnePaneMode():Boolean{
        return  binding.shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()//удалит из бекстека 1 фрагмент а если ничего нет то ничего не делает
        supportFragmentManager.beginTransaction().replace( R.id.shop_item_container,fragment).addToBackStack(null).commit()
            //addToBackStack добавит фрагмент в стек
    }








    private fun setupRecyclerView() {
       with(binding.rvShopList) {
               shopListAdapter = ShopListAdapter()
           adapter=shopListAdapter
               recycledViewPool.setMaxRecycledViews(
                   ShopListAdapter.ENABLE_OBJ,
                   ShopListAdapter.MAX_POOL_SIZE
               )
              recycledViewPool.setMaxRecycledViews(
                   ShopListAdapter.DISABLE_OBJ,
                   ShopListAdapter.MAX_POOL_SIZE
               )

       }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(binding.rvShopList)
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

    override fun onEditingFinish() {
        Toast.makeText(this@MainActivity,"Success",Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }


}


