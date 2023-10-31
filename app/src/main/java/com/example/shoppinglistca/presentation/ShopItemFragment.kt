package com.example.shoppinglistca.presentation

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

    lateinit var viewModel: ShopItemViewModel

    lateinit var tilName: TextInputLayout
    lateinit var tilCount: TextInputLayout
    lateinit var etName: TextInputEditText
    lateinit var etCount: TextInputEditText
    lateinit var saveButton: Button

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        //onCreateView нужен для того что бы из макета создать view
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_shop_item, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //момент в который мы будем уверены что это view точно создано(onViewCreated вызывается когда view уже точно будет создано и начиная с этого метода можно начинать работать с view )
        //без каких либо ограничений
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
//    viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
//        finish()
//   у фрагментов нет метода finish есть метот onBackPressed() который работает так как будто вы сами нажали на кнопку назад на телефоне

        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            //что бы получить ссылку на активити к который прикреплен фрагмент можно вызвать метод activity или requireActivity()
            //они отличаются что activity возрашает нулабельный обьект и если у него нужно вызвать какие то методы то нужно вызывать
            // проверку ?на налл а requireActivity() возврашает не нулабельный обьект.Здесь проблемма заключается в том что если мы в
            // фрагменте обращаемся к activity то можем сделать это когда фрагмент еще не прикреплен к активити или уже был удален
            // requireActivity не безопасный метод!
            activity?.onBackPressed()


            //  requireActivity()
        }
    }

    private fun parseParams() {
        //этот метод вызывается что бы проверить что все параметры были правильно переданы в фрагмент
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
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""


        //статический фабричный метод для передачи фрагмента
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
        //у фрагмента нет метода findViewById
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        saveButton = view.findViewById(R.id.save_button)
    }

}