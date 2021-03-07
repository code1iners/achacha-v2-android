package com.codeliner.achacha.utils

import com.codeliner.achacha.data.todos.Todo
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