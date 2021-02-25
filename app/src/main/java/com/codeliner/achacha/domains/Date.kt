package com.codeliner.achacha.domains

import timber.log.Timber
import java.text.SimpleDateFormat

class Date {

    var currentTimeAsMilli = System.currentTimeMillis()
    var month: String? = null
    var dayOfWeek: String? = null
    var day: String? = null

    init {
        val form = SimpleDateFormat()
        form.applyPattern("MMMM")
        month = form.format(currentTimeAsMilli)
        form.applyPattern("EEEE")
        dayOfWeek = form.format(currentTimeAsMilli)
        form.applyPattern("dd")
        day = form.format(currentTimeAsMilli)
    }

    fun log() {
        Timber.d("month: $month, dayOfWeek: $dayOfWeek, day: $day")
    }
}