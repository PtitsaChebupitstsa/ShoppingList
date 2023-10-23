package com.example.shoppinglistca.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglistca.R
import com.example.shoppinglistca.domain.ShopItem
import com.example.shoppinglistca.domain.ShopItem.Companion.UNDEFINED_ID
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment(   private val screenMode:String = MODE_UNKNOWN, private val shopItemId:Int = UNDEFINED_ID):Fragment() {

    lateinit var viewModel: ShopItemViewModel

    lateinit var tilName: TextInputLayout
    lateinit var tilCount: TextInputLayout
    lateinit var etName: TextInputEditText
    lateinit var etCount: TextInputEditText
    lateinit var saveButton: Button

//    private var screenMode = MODE_UNKNOWN
//    private var shopItemId = UNDEFINED_ID
    override fun onCreateView(
        //onCreateView нужен для того что бы из макета создать view
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return  inflater.inflate(R.layout.fragment_shop_item,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //момент в который мы будем уверены что это view точно создано(onViewCreated вызывается когда view уже точно будет создано и начиная с этого метода можно начинать работать с view )
        //без каких либо ограничений
        super.onViewCreated(view, savedInstanceState)
        parseParams()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        addTextChangeListener()
        launchRightMode()
        observeViewModel()
    }



private fun observeViewModel(){
    viewModel.errorInputCount.observe(viewLifecycleOwner) {//обработка ошибки
        val message = if (it) {
            getString(R.string.error_input_count)
        } else {
            null
        }
        tilCount.error = message
    }
    viewModel.errorInputName.observe(viewLifecycleOwner) {//обработка ошибки
        val message = if (it) {
            getString(R.string.error_input_name)
        } else {
            null
        }
        tilName.error = message
    }
    viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
        finish()
    }}

    private fun parseParams() {
        //это бывший метод parseIntent и так делать не стоит
        //фрагмен не актививити и что бы показать фрагмент intent не используется
        if (screenMode!= MODE_EDIT&&screenMode!= MODE_ADD){
            throw RuntimeException("Param screen mode is absent")
        }
        if (screenMode == MODE_EDIT&&shopItemId== ShopItem.UNDEFINED_ID) {
            throw RuntimeException("Param shopItemId is absent")
        }
    }

    private fun launchRightMode(){//выбор мода
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }
    private fun addTextChangeListener() {
 etName.addTextChangedListener(object : TextWatcher {
     override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

     }

     override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
         viewModel.resetErrorInputName()
     }

     override fun afterTextChanged(s: Editable?) {

     }

 })
        etCount.addTextChangedListener(object : TextWatcher {
            //скрываем осообщение об ошибке при наборе текста
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    private fun launchAddMode() {
        saveButton.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchEditMode() {
        // в нутри фрагмента в место this " viewModel.shopItem.observe(this)" нужно передать viewLifecycleOwner
        // это происходит вот по какой причине: обычно в нутре observe мы работаем с какими то view эллементамми и если вдруг view больше не существует
        // то необходимо отписатся от этой LiveData благодаря viewLifecycleOwner это происходит автоматически (ибо может быт так что view уже умерла а фрагмент все еще жив)
        // и из за этого у нас может быть краш и по этому теперь пердается в место this используется viewLifecycleOwner так как она использует жизненый цикл фрагмента а не вью
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        saveButton.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
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

    private fun initViews(view: View) {
        //у фрагмента нет метода findViewById
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        saveButton = view.findViewById(R.id.save_button)
    }

}