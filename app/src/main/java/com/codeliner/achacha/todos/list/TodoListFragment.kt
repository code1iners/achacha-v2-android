package com.codeliner.achacha.todos.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.transition.TransitionManager
import com.codeliner.achacha.databinding.FragmentTodoListBinding
import com.codeliner.achacha.domains.todos.Todo
import com.codeliner.achacha.domains.todos.TodoDatabase
import com.codeliner.achacha.mains.MainActivity
import timber.log.Timber

class TodoListFragment: Fragment()
    , TodoClickListener
    , TodoMoveListener
{

    private lateinit var binding: FragmentTodoListBinding
    private lateinit var viewModelFactory: TodoListViewModelFactory
    private lateinit var viewModel: TodoListViewModel

    // note. adapters
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoListBinding.inflate(inflater)
        binding.lifecycleOwner = this

        initViewModel()
        initAdapters()
        initObservers()


        return binding.root
    }

    private fun initViewModel() {
        val app = requireNotNull(activity).application
        val todoDatabaseDao = TodoDatabase.getInstance(app.applicationContext).todoDatabaseDao
        viewModelFactory = TodoListViewModelFactory(app, todoDatabaseDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TodoListViewModel::class.java)
        // note. assignment view model into layout
        binding.viewModel = viewModel
    }

    private fun initAdapters() {
        todoAdapter = TodoAdapter(this, this)
        binding.fragmentTodoListTodoList.adapter = todoAdapter
        val helper = ItemTouchHelper(ItemTouchHelperCallback(todoAdapter))
        helper.attachToRecyclerView(binding.fragmentTodoListTodoList)
    }

    private fun initObservers() {
        // note. fabs
        val cs = ConstraintSet()
        cs.clone(binding.fragmentTodoListFabList)
        viewModel.isFavCollapsed.observe(viewLifecycleOwner, Observer { isCollapsed ->
            if (!isCollapsed) {
                cs.connect(binding.fragmentTodoListFabCreate.id, ConstraintSet.BOTTOM, binding.fragmentTodoListFabMain.id, ConstraintSet.TOP)
                cs.connect(binding.fragmentTodoListFabClear.id, ConstraintSet.BOTTOM, binding.fragmentTodoListFabCreate.id, ConstraintSet.TOP)

                binding.fragmentTodoListFabMain.startAnimation(viewModel.animRight)
                binding.fragmentTodoListFabCreate.startAnimation(viewModel.animShow)
                binding.fragmentTodoListFabClear.startAnimation(viewModel.animShow)

            } else {
                cs.connect(binding.fragmentTodoListFabCreate.id, ConstraintSet.BOTTOM, binding.fragmentTodoListFabList.id, ConstraintSet.BOTTOM)
                cs.connect(binding.fragmentTodoListFabClear.id, ConstraintSet.BOTTOM, binding.fragmentTodoListFabList.id, ConstraintSet.BOTTOM)

                binding.fragmentTodoListFabMain.startAnimation(viewModel.animLeft)
                binding.fragmentTodoListFabCreate.startAnimation(viewModel.animHide)
                binding.fragmentTodoListFabClear.startAnimation(viewModel.animHide)
            }

            TransitionManager.beginDelayedTransition(binding.fragmentTodoListFabList, viewModel.transition)
            cs.applyTo(binding.fragmentTodoListFabList)
        })

        // note. fab menu 1
        viewModel.onNavigateToCreateTodo.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController()
                        .navigate(TodoListFragmentDirections.actionTodoListFragmentToTodoCreateFragment(viewModel.tasks.value ?: -1))
                MainActivity.onBottomNavigationSwitch()
                viewModel.navigateToCreateTodoComplete()
            }
        })

        // note. todos
        viewModel.todos.observe(viewLifecycleOwner, Observer { it ->
            it?.let { todos ->

                todoAdapter.submitList(todos)
            }
        })
    }

    override fun onClick(todo: Todo) {
        Timber.v("work: ${todo.work}, position: ${todo.position}")
    }

    override fun onRemove(todo: Todo) {
        viewModel.onRemoveTodo(todo)
    }

    override fun onFinished(todo: Todo) {
        viewModel.onUpdateTodoIsFinished(todo)
    }

    override fun itemMove(fromPosition: Int, toPosition: Int) {
        todoAdapter.log()

        todoAdapter.storedList[fromPosition].position = toPosition
        todoAdapter.storedList[toPosition].position = fromPosition
        todoAdapter.storedList = todoAdapter.storedList.sortedBy { it.position }

        todoAdapter.log()

        todoAdapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun itemSwipe(todo: Todo) {
        Timber.w("itemSwipe")

        viewModel.onRemoveTodo(todo)
    }

    override fun onPause() {
        Timber.w("onPause")
        todoAdapter.log()
        viewModel.onUpdateTodos(todoAdapter.storedList)
        super.onPause()
    }
}