package com.codeliner.achacha.utils

import androidx.recyclerview.widget.DiffUtil
import com.codeliner.achacha.data.todos.Todo

class TodoListDiffCallback: DiffUtil.ItemCallback<Todo>() {

    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
//            Timber.w("areItemsTheSame: ${oldItem.id == newItem.id}")
//            Timber.d("${oldItem.id}")
//            Timber.v("${newItem.id}")
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
//        Timber.w("areContentsTheSame: ${oldItem == newItem}, oldItem: ${oldItem.isFinished}, newItem: ${newItem.isFinished}")
//            Timber.d("${oldItem}")
//            Timber.v("${newItem}")
        return oldItem == newItem
    }
}