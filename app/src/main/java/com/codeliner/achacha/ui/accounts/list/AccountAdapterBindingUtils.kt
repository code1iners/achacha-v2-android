package com.codeliner.achacha.ui.accounts.list

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.codeliner.achacha.data.accounts.Account

@BindingAdapter("setAccountTitle")
fun TextView.setAccountTitle(item: Account?) {
    item?.let {
        text = it.title
    }
}

@BindingAdapter("setAccountUsername")
fun TextView.setAccountUsername(item: Account?) {
    item?.let {
        text = it.username
    }
}

@BindingAdapter("setAccountThumbnailAsText")
fun TextView.setThumbnailAsTest(item: Account?) {
    item?.let { account ->
        val let = account.title?.let { title ->
            text = title.capitalize().firstOrNull().toString()
        }
        let
    }
}