package com.codeliner.achacha.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.ActivityTextInputBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TextInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextInputBinding
    private val viewModel: TextInputViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_input)
        initialize()
    }

    private fun initialize() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_input)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}