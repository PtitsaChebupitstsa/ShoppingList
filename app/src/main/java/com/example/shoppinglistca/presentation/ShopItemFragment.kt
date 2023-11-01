package com.example.shoppinglistca.presentation

import android.content.Context
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

import com.example.shoppinglistca.domain.ShopItem.Companion.UNDEFINED_ID
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment() : Fragment() {

     private lateinit var onEditingFinishListener:OnEditingFinishListener

    lateinit var viewModel: ShopItemViewModel
    lateinit var tilName: TextInputLayout
    lateinit var tilCount: TextInputLayout
    lateinit var etName: TextInputEditText
    lateinit var etCount: TextInputEditText
    lateinit var saveButton: Button

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishListener){
            onEditingFinishListener= context
        } else{
            throw RuntimeException("Activity must implement OnEditingFinishListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_shop_item, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)//важно
        addTextChangeListener()
        launchRightMode()
        observeViewModel()
    }


    private fun observeViewModel() {
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
            onEditingFinishListener.onEditingFinish()
          //  activity?.onBackPressed()
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode ?: MODE_UNKNOWN
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shopItemId is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, UNDEFINED_ID)
        }
    }
    private fun launchRightMode() {//выбор мода
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
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""


        interface OnEditingFinishListener{
            fun onEditingFinish()
        }
        fun newInstanceAddItem(): ShopItemFragment {
            Bundle().apply {
                   return ShopItemFragment().apply {
                       arguments = Bundle().apply {
                           putString(SCREEN_MODE, MODE_ADD)
                       }
                   }
               }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {//статический фабричный метод для передачи фрагмента
            Bundle().apply {
                return ShopItemFragment().apply {
                    arguments = Bundle().apply {
                        putString(SCREEN_MODE, MODE_EDIT)
                        putInt(SHOP_ITEM_ID, shopItemId)
                    }
                }
            }
        }

    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        saveButton = view.findViewById(R.id.save_button)
    }

}