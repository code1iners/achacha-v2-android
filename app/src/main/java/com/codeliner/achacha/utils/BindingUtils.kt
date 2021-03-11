package com.codeliner.achacha.utils

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.codeliner.achacha.R
import com.codeliner.achacha.data.todos.Todo
import com.example.helpers.CreatedParser
import com.example.helpers.ui.getMoveInRight
import com.example.helpers.ui.getMoveOutLeft
import timber.log.Timber


@BindingAdapter("setVisible")
fun View.setVisible(status: Boolean) {
//    Timber.w("status: $status")
    when (status) {
        true -> this.visibility = View.VISIBLE
        false -> this.visibility = View.GONE
    }
}

@BindingAdapter("setAnimation")
fun View.setAnimation(status: Boolean) {
    val defaultDuration = this.resources.getInteger(R.integer.animation_duration_default)
    when (status) {
        true -> {
            this.startAnimation(this.context.getMoveInRight().apply {
                duration = defaultDuration.toLong()
            })
        }
        false -> {
            this.startAnimation(this.context.getMoveOutLeft().apply {
                duration = defaultDuration.toLong()
            })
        }
    }
    this.setVisible(status)
}
