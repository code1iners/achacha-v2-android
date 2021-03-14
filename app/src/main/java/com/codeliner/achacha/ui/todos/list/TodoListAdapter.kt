package com.codeliner.achacha.ui.todos.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeliner.achacha.databinding.ItemTodoBinding
import com.codeliner.achacha.data.todos.Todo
import com.codeliner.achacha.utils.TodoListDiffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class TodoAdapter(val clickListener: TodoClickListener, val moveListener: TodoMoveListener):
    ListAdapter<Todo, TodoAdapter.ViewHolder>(TodoListDiffCallback())
    , ItemTouchHelperListener
{

    private var adapterScope = CoroutineScope(Dispatchers.Default)

    fun testSubmitList(list: List<Todo>?) {
        adapterScope.launch {

            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    private var storedList: ArrayList<Todo> = ArrayList()
    fun getStoredList(): ArrayList<Todo> {
        return storedList
    }
    fun setStoredList(list: List<Todo>?) {
        list?.let { newList ->
            storedList.clear()
            storedList.addAll(newList)
        }
    }
    fun logStoredList() {
        for (todo in storedList) Timber.v("id: ${todo.id}, work: ${todo.work}, isFinished: ${todo.isFinished}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            itemCount > 0 -> {
                holder.bind(item, position, clickListener, moveListener)
            }

            else -> {
                Timber.w("itemCount is zero")
            }
        }
    }

    class ViewHolder private constructor(val binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Todo, position: Int, clickListener: TodoClickListener, moveListener: TodoMoveListener) {
            binding.todo = item
            binding.clickListener = clickListener
            binding.moveListener = moveListener
            binding.position = position
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

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        moveListener.itemMove(fromPosition, toPosition)
        return true
    }

    override fun onItemSwipe(position: Int) {
        val todo = getItem(position)
        moveListener.itemSwipe(todo)
    }
}

interface TodoClickListener {
    fun onClick(todo: Todo)
    fun onRemove(todo: Todo)
    fun onFinished(todo: Todo, position: Int)
}

interface TodoMoveListener {
    fun itemMove(fromPosition: Int, toPosition: Int)
    fun itemSwipe(todo: Todo)
}