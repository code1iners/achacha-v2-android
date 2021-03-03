package com.codeliner.achacha.utils

import android.content.res.Configuration

fun Configuration.isDarkMode(): Boolean {
    val currentNightMode = this.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return when (currentNightMode) {
        Configuration.UI_MODE_NIGHT_YES -> true
        Configuration.UI_MODE_NIGHT_NO -> false
        else -> false
    }
}