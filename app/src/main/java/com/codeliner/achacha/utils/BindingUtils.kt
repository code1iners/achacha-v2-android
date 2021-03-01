package com.codeliner.achacha.utils

import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.codeliner.achacha.domains.todos.Todo

@BindingAdapter("todoWorkString")
fun TextView.setTodoWork(item: Todo?) {
    item?.let {
        text = item.work
    }
}

@BindingAdapter("todoHelpString")
fun TextView.setTodoHelp(item: Todo?) {
    item?.let {
        text = item.help
    }
}

@BindingAdapter("todoIsFinished")
fun CheckBox.setIsFinished(item: Todo?) {
    item?.let {
        isChecked = item.isFinished
    }
}