package com.example.shoppinglistca.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglistca.R
import com.example.shoppinglistca.domain.ShopItem.Companion.UNDEFINED_ID
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {
//    lateinit var tilName: TextInputLayout
//    lateinit var tilCount: TextInputLayout
//    lateinit var etName: TextInputEditText
//    lateinit var etCount: TextInputEditText
//    lateinit var saveButton: Button
//    lateinit var viewModel: ShopItemViewModel
//
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
//        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
//        initViews()
//        addTextChangeListener()
        launchRightMode()
//        observeViewModel()
    }
//
//private fun observeViewModel(){
//    viewModel.errorInputCount.observe(this) {//обработка ошибки
//        val message = if (it) {
//            getString(R.string.error_input_count)
//        } else {
//            null
//        }
//        tilCount.error = message
//    }
//    viewModel.errorInputName.observe(this) {//обработка ошибки
//        val message = if (it) {
//            getString(R.string.error_input_name)
//        } else {
//            null
//        }
//        tilName.error = message
//    }
//    viewModel.shouldCloseScreen.observe(this) {
//        finish()
//    }}
//
    private fun launchRightMode(){//выбор мода

      val fragment =   when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
          else-> throw RuntimeException("Unknown screen mode $screenMode")
        }
    //установка фрагмента в контейнер
    supportFragmentManager.beginTransaction()
        .add(R.id.shop_item_container,fragment)
        .commit()
    }
//    private fun addTextChangeListener() {
// etName.addTextChangedListener(object :TextWatcher{
//     override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//     }
//
//     override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//         viewModel.resetErrorInputName()
//     }
//
//     override fun afterTextChanged(s: Editable?) {
//
//     }
//
// })
//        etCount.addTextChangedListener(object : TextWatcher {
//            //скрываем осообщение об ошибке при наборе текста
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                viewModel.resetErrorInputCount()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//        })
//
//    }
//
//    private fun launchAddMode() {
//        saveButton.setOnClickListener {
//            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
//    }
//
//    private fun launchEditMode() {
//        viewModel.getShopItem(shopItemId)
//        viewModel.shopItem.observe(this) {
//            etName.setText(it.name)
//            etCount.setText(it.count.toString())
//        }
//        saveButton.setOnClickListener {
//            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
//    }
//
    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shopItemId is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, UNDEFINED_ID)
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""


        fun newIntentAddMItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent

        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent


        }
    }
//
//    private fun initViews() {
//        tilName = findViewById(R.id.til_name)
//        tilCount = findViewById(R.id.til_count)
//        etName = findViewById(R.id.et_name)
//        etCount = findViewById(R.id.et_count)
//        saveButton = findViewById(R.id.save_button)
//    }
}