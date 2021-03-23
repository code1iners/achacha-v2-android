package com.codeliner.achacha.ui.inputs.chip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.ActivityChipInputBinding
import com.example.helpers.MeasureManager.toDp
import com.example.helpers.WidgetManager.LayoutParamsManager.Companion.setMarginHorizontal
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChipInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChipInputBinding
    private val viewModel: ChipInputViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        initializeBinding()
        initializeFlexItems()
    }

    private fun initializeFlexItems() {
        viewModel.showTags()
        viewModel.tags.forEach { item ->
            val itemView = TextView(this)
            itemView.text = item
            itemView.setMarginHorizontal(4.toDp(this))
            itemView.isClickable = true
            itemView.setBackgroundResource(R.drawable.item_touch_effect_rounded_m)

            binding.bodyFlexBox.addView(itemView)
        }
    }

    private fun initializeBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chip_input)
        binding.lifecycleOwner = this
    }

}