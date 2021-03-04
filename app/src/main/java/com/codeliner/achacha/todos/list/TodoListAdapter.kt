package com.codeliner.achacha.todos.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeliner.achacha.databinding.ItemTodoBinding
import com.codeliner.achacha.domains.todos.Todo
import timber.log.Timber

class TodoAdapter(
    val clickListener: TodoClickListener,
    val moveListener: TodoMoveListener
):
    ListAdapter<Todo, TodoAdapter.ViewHolder>(TodoDiffCallback())
    , ItemTouchHelperListener
{
    
    var storedList: List<Todo> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            itemCount > 0 -> {
                holder.bind(item, clickListener, moveListener)
            }

            else -> {
                Timber.w("itemCount is zero")
            }
        }
    }

    class ViewHolder private constructor(val binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Todo, clickListener: TodoClickListener, moveListener: TodoMoveListener) {
            binding.todo = item
            binding.clickListener = clickListener
            binding.moveListener = moveListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTodoBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class TodoDiffCallback: DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            Timber.w("areItemsTheSame: ${oldItem.work} ${oldItem.id == newItem.id}")
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            Timber.w("areContentsTheSame: ${oldItem.work} ${oldItem == newItem}")
            return oldItem == newItem
        }
    }

    override fun onCurrentListChanged(previousList: MutableList<Todo>, currentList: MutableList<Todo>) {
        Timber.w("onCurrentListChanged")
        this.storedList = currentList
        super.onCurrentListChanged(previousList, currentList)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        moveListener.itemMove(fromPosition, toPosition)
        return true
    }

    override fun onItemSwipe(position: Int) {
        val todo = getItem(position)
        moveListener.itemSwipe(todo)
    }

    fun log() {
        if (storedList.isEmpty()) return
        val sortedList = storedList.sortedBy { it.position }
        for (todo in sortedList) Timber.v("work: ${todo.work}, position: ${todo.position}")
        Timber.d("#########################################")
    }
}

interface TodoClickListener {
    fun onClick(todo: Todo)
    fun onRemove(todo: Todo)
    fun onFinished(todo: Todo)
}

interface TodoMoveListener {
    fun itemMove(fromPosition: Int, toPosition: Int)
    fun itemSwipe(todo: Todo)
}