package com.codeliner.achacha.titles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeliner.achacha.domains.Date
import timber.log.Timber

class TitleViewModel(app: Application): AndroidViewModel(app) {

    private val _date = MutableLiveData<Date>()
    val date: LiveData<Date> get() = _date

    init {
        _date.value = Date()
    }
}
