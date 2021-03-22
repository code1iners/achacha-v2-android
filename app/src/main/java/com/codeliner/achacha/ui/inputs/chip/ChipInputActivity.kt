package com.codeliner.achacha.ui.inputs.chip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.ActivityChipInputBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChipInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChipInputBinding
    private val viewModel: ChipInputViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        viewModel.showTags()
    }

    private fun initialize() {
        initializeBinding()
    }

    private fun initializeBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chip_input)
        binding.lifecycleOwner = this
    }
}