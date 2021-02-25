package com.codeliner.achacha.utils

import timber.log.Timber
import java.text.SimpleDateFormat

class Date {

    var currentTimeAsMilli = System.currentTimeMillis()
    var year: String? = null
    var month: String? = null
    var day: String? = null
    var dayOfWeek: String? = null

    init {
        val form = SimpleDateFormat()
        form.applyPattern("yyyy")
        year = form.format(currentTimeAsMilli)
        form.applyPattern("MMM")
        month = form.format(currentTimeAsMilli)
        form.applyPattern("dd")
        day = form.format(currentTimeAsMilli)
        form.applyPattern("EEEE")
        dayOfWeek = form.format(currentTimeAsMilli)
    }

    fun log() {
        Timber.d("year: $year, month: $month, day: $day, dayOfWeek: $dayOfWeek")
    }
}