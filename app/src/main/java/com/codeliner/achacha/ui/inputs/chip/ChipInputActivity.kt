package com.codeliner.achacha.ui.inputs.chip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.ActivityChipInputBinding
import com.example.helpers.MeasureManager.toDp
import com.example.helpers.WidgetManager.LayoutParamsManager.Companion.setMarginHorizontal
import com.example.helpers.WidgetManager.LayoutParamsManager.Companion.setMarginRight
import com.example.helpers.WidgetManager.LayoutParamsManager.Companion.setMarginVertical
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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
        for (item in viewModel.tags) {
            // note. Add tag items.
            val itemView = TextView(this)
            itemView.text = item
            itemView.setMarginVertical(2.toDp(this))
            itemView.setPadding(8.toDp(this), 2.toDp(this), 8.toDp(this), 2.toDp(this))
            itemView.isClickable = true
            itemView.setBackgroundResource(R.drawable.item_touch_effect_rounded_m)

            binding.bodyFlexBox.addView(itemView)

            // note. Add spacing between tag items.
            val space = TextView(this)
            space.setMarginHorizontal(1.toDp(this))
            binding.bodyFlexBox.addView(space)
        }
    }

    private fun initializeBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chip_input)
        binding.lifecycleOwner = this
    }

}