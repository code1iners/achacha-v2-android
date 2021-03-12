package com.codeliner.achacha.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

//fun AppCompatActivity.checkSelfPermissionCompat(permission: String) =
//        ActivityCompat.checkSelfPermission(this, permission)
//
//fun AppCompatActivity.shouldShowRequestPermissionRationaleCompat(permission: String) =
//        ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
//
//fun AppCompatActivity.requestPermissionsCompat(permissionsArray: Array<String>, requestCode: Int) {
//    ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
//}
