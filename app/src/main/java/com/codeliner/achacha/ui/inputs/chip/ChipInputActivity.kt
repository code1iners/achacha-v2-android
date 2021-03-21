package com.codeliner.achacha.ui.inputs.chip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.ActivityChipInputBinding

class ChipInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChipInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chip_input)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chip_input)
        binding.lifecycleOwner = this
    }
}