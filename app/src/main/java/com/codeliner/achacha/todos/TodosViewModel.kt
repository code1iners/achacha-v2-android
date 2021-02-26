package com.codeliner.achacha.todos

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeliner.achacha.R
import com.codeliner.achacha.utils.Date

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TodosViewModel(app: Application): AndroidViewModel(app) {

    private val _date = MutableLiveData<Date>()
    val date: LiveData<Date> get() = _date

    init {
        _date.value = Date()
    }
}
