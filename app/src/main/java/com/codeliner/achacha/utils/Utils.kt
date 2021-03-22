package com.codeliner.achacha.utils

import android.app.Application
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.codeliner.achacha.R
import com.codeliner.achacha.data.todos.Todo
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber

fun Todo.log(type: String) {
    when (type) {
        "w" -> {
            Timber.w("id: ${this.id}, work: ${this.work}, isFinished: ${this.isFinished}")
        }

        "d" -> {
            Timber.d("id: ${this.id}, work: ${this.work}, isFinished: ${this.isFinished}")
        }

        "v" -> {
            Timber.v("id: ${this.id}, work: ${this.work}, isFinished: ${this.isFinished}")
        }

        "i" -> {
            Timber.i("id: ${this.id}, work: ${this.work}, isFinished: ${this.isFinished}")
        }
    }
}

fun List<TextInputLayout>.clearErrors() {
    this.forEach {
        it.isErrorEnabled = false
        it.error = null
    }
}

fun List<TextInputLayout>.clearErrorsWithHelperOff() {
    this.forEach { layout ->
        layout.isErrorEnabled = false
        layout.error = null
        layout.editText?.let { et ->
            if (et.text.isNotEmpty()) {
                layout.isHelperTextEnabled = false
            }
        }
    }
}

fun TextView.setTextColorById(colorId: Int) {
    this.setTextColor(ContextCompat.getColor(context, colorId))
}

fun Application.getTags(): ArrayList<String> {
    return arrayListOf(
        this.getString(R.string.call),
        this.getString(R.string.check),
        this.getString(R.string.take),
        this.getString(R.string.email),
        this.getString(R.string.buy),
        this.getString(R.string.meet),
        this.getString(R.string.clean),
        this.getString(R.string.send),
        this.getString(R.string.payment),
        this.getString(R.string.create),
        this.getString(R.string.select),
        this.getString(R.string.doing),
        this.getString(R.string.read),
        this.getString(R.string.study),
    )
}
