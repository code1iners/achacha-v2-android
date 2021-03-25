package com.codeliner.achacha.ui.inputs.chip

import android.app.Application
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeliner.achacha.utils.getTags
import timber.log.Timber

class ChipInputViewModel(
    application: Application
): ViewModel() {
    val tags = application.getTags()

    private val _onSelectedTags = MutableLiveData(ArrayList<TextView>())
    val onSelectedTags: LiveData<ArrayList<TextView>> get() = _onSelectedTags
    fun tagAddOrRemove(tag: TextView) {
        onSelectedTags.value?.let { tags ->
            // note. Clear to match items positioning.
            selectedItems.clear()
            // note. Check tags already exists in list.
            val newList = tags
            when (tags.contains(tag)) {
                true -> newList.remove(tag)
                false -> newList.add(tag)
            }
            // note. Update list by new list.
            _onSelectedTags.value = newList
        }
    }

    val selectedItems = ArrayList<Int>()
    fun selectItem(itemPosition: Int) {
        if (!selectedItems.contains(itemPosition)) {
            selectedItems.add(itemPosition)
        }
    }

    fun showTags() {
        tags.forEach { item ->
            Timber.v("item: $item")
        }
    }
}