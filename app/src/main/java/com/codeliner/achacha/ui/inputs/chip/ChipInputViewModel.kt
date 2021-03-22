package com.codeliner.achacha.ui.inputs.chip

import android.app.Application
import androidx.lifecycle.ViewModel
import com.codeliner.achacha.R
import com.codeliner.achacha.utils.getTags
import timber.log.Timber

class ChipInputViewModel(
    application: Application
): ViewModel() {
    private val tags = application.getTags()

    fun showTags() {
        tags.forEach { item ->
            Timber.v("item: $item")
        }
    }
}