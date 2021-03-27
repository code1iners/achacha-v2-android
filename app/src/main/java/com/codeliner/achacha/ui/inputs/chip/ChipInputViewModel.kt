package com.codeliner.achacha.ui.inputs.chip

import android.app.Application
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.codeliner.achacha.utils.getTags
import timber.log.Timber

class ChipInputViewModel(
    application: Application
): ViewModel() {

    private val _onTitle = MutableLiveData<String>()
    val onTitle: LiveData<String> get() = _onTitle
    fun setTitle(title: String) {
        _onTitle.value = title
    }
    
    private val _tags = MutableLiveData<ArrayList<String>>()
    val tags: LiveData<ArrayList<String>> get() = _tags
    fun setTags(tags: ArrayList<String>) {
        if (_tags.value == null) _tags.value = ArrayList()

        _tags.value?.clear()
        _tags.value?.addAll(tags)
    }

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

    // note. In progress.
    val resultsTags = Transformations.map(_onSelectedTags) {
        Timber.w("resultsTags changed: $it")
        it?.let { tags ->
            val results: ArrayList<String> = ArrayList()
            for (tag in tags) {
                results.add(tag.text.toString())
            }
            results
        }
    }

    val selectedItems = ArrayList<Int>()
    fun selectItem(itemPosition: Int) {
        if (!selectedItems.contains(itemPosition)) {
            selectedItems.add(itemPosition)
        }
    }

    private val _save = MutableLiveData<Boolean>()
    val save: LiveData<Boolean> get() = _save
    fun saveStart() {
        _save.value = true
    }

    fun saveComplete() {
        _save.value = false
    }

    fun showTags() {
        tags.value?.forEach { item ->
            Timber.v("item: $item")
        }
    }
}