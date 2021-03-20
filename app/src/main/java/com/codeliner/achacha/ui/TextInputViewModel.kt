package com.codeliner.achacha.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TextInputViewModel: ViewModel() {

    // note. User input text variable.
    var input: String? = null

    // note. When user pressed window close button.
    private val _onCloseWindow = MutableLiveData<Boolean>()
    val onCloseWindow: LiveData<Boolean> get() = _onCloseWindow
    fun closeWindowJob() {
        _onCloseWindow.value = true
    }
    fun closeWindowComplete() {
        _onCloseWindow.value = false
    }

    // note. When user pressed save button.
    private val _onInputSave = MutableLiveData<Boolean>()
    val onInputSave: LiveData<Boolean> get() = _onInputSave
    fun inputSaveJob() {
        _onInputSave.value = true
    }
    fun inputSaveComplete() {
        _onInputSave.value = false
    }
}