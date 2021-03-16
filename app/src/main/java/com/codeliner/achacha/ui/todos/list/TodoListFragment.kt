package com.codeliner.achacha.ui.todos.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.transition.AutoTransition
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.FragmentTodoListBinding
import com.codeliner.achacha.data.todos.Todo
import com.codeliner.achacha.ui.todos.detail.TodoDetailFragment
import com.codeliner.achacha.utils.Const.ANIMATION_DURATION_SHORT
import com.codeliner.achacha.utils.log
import com.example.helpers.ui.AnimationManager
import com.example.helpers.ui.toastingShort
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TodoListFragment: Fragment()
    , TodoClickListener
    , TodoMoveListener
{

    private lateinit var binding: FragmentTodoListBinding
    private val viewModel: TodoListViewModel by viewModel()

    // note. adapters
    private lateinit var todoAdapter: TodoAdapter

    // note. animations (ui)
    private lateinit var animHeaderShow: Animation
    private lateinit var animHeaderHide: Animation
    private lateinit var animRotateLeft: Animation
    private lateinit var animRotateRight: Animation
    private lateinit var animHide: Animation
    private lateinit var animShow: Animation
    private lateinit var transition: AutoTransition

    override fun onStop() {
        super.onStop()

        // note. header
        binding.fragmentTodoListCalendarContainer.startAnimation(animHeaderHide)
        // note. header
        binding.fragmentTodoListCalendarDividerBottom.startAnimation(animHeaderHide)
        // note. body
        binding.bodyContainer.startAnimation(
                AnimationManager.getFadeOut(requireContext()).apply {
                    duration = ANIMATION_DURATION_SHORT
                    fillAfter = true
                })
    }

    override fun onStart() {
        super.onStart()

        // note. header
        binding.fragmentTodoListCalendarContainer.startAnimation(animHeaderShow)
        // note. header
        binding.fragmentTodoListCalendarDividerBottom.startAnimation(animHeaderShow)
        // note. body
        binding.bodyContainer.startAnimation(
                AnimationManager.getFadeIn(requireContext()).apply {
                    duration = ANIMATION_DURATION_SHORT
                    fillAfter = true
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTodoListBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initAnimations()
        initAdapters()
        initObservers()

        return binding.root
    }

    private fun initAnimations() {
        animHeaderShow = AnimationManager.getHeaderShow(requireContext())
        animHeaderHide = AnimationManager.getHeaderHide(requireContext())

        animRotateLeft = AnimationManager.getRotateLeft45(requireContext())
        animRotateRight = AnimationManager.getRotateRight45(requireContext())

        animHide = AnimationManager.getFadeOut(requireContext()).apply {
            duration = ANIMATION_DURATION_SHORT
            fillAfter = true
        }
        animShow = AnimationManager.getFadeIn(requireContext()).apply {
            duration = ANIMATION_DURATION_SHORT
            fillAfter = true
        }

        transition = AutoTransition().apply {
            duration = ANIMATION_DURATION_SHORT
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    private fun initAdapters() {
        todoAdapter = TodoAdapter(this, this)
        binding.todoList.adapter = todoAdapter
        val helper = ItemTouchHelper(ItemTouchHelperCallback(todoAdapter))
        helper.attachToRecyclerView(binding.todoList)
    }

    private fun initObservers() {
        observeFabs()
        observeTodos()
    }

    private fun observeFabs() {
        // note. fab menu create
        observeCreate()
        // note. fab menu clear
        observeClear()
        // note. fab menu test
        observeTest()
    }

    private fun observeCreate() {
        TodoListViewModel.onTodoCreate.observe(viewLifecycleOwner) { isStart ->
            if (isStart) {
                viewModel.onNavigateToCreateTodoReady()

                TodoListViewModel.todoCreateJobComplete()
            }
        }

        viewModel.onNavigateToCreateTodoReady.observe(viewLifecycleOwner) { isReady ->
            if (isReady) {
                // note. update ui
                binding.fragmentTodoListCalendarContainer.startAnimation(animHeaderHide)
                binding.fragmentTodoListCalendarDividerBottom.startAnimation(animHeaderHide)
                binding.bodyContainer.startAnimation(animHide)

                viewModel.navigateToCreateTodoReady()
                viewModel.navigateToCreateTodoComplete()
            }
        }

        viewModel.onNavigateToCreateTodoProcess.observe(viewLifecycleOwner) { isStart ->
            if (isStart) {
                findNavController().navigate(TodoListFragmentDirections.actionTodoListFragmentToTodoCreateFragment(viewModel.tasks.value ?: -1))

                viewModel.navigateToCreateTodoProcessComplete()
            }
        }
    }

    private fun observeClear() {
        TodoListViewModel.onTodoClear.observe(viewLifecycleOwner) { isStart ->
            if (isStart) {
                viewModel.clearTodos()

                TodoListViewModel.todoClearJobComplete()
            }
        }
    }

    private fun observeTest() {
        TodoListViewModel.onTodoTest.observe(viewLifecycleOwner) { isStart ->
            if (isStart) {

                context?.toastingShort("Look like a Star.")

                TodoListViewModel.todoTestJobComplete()
            }
        }
    }

    private fun observeTodos() {
        viewModel.todos.observe(viewLifecycleOwner) { todos ->
            when (todos.isEmpty()) {
                true -> {
                    binding.todoListEmptyText.visibility = View.VISIBLE
                    binding.todoList.visibility = View.GONE
                }

                false -> {
                    binding.todoListEmptyText.visibility = View.GONE
                    binding.todoList.visibility = View.VISIBLE
                }
            }
            todoAdapter.testSubmitList(todos.map { it.copy() })
        }
    }

    override fun onClick(todo: Todo) {
        todo.log("d")
        activity?.let {
            TodoDetailFragment().show(it.supportFragmentManager, "todo_detail_dialog_fragment")
        }
    }

    override fun onRemove(todo: Todo) {
        viewModel.onRemoveTodo(todo)
    }

    override fun onFinished(todo: Todo, position: Int) {

        viewModel.onUpdateTodoIsFinished(todo)
    }

    override fun itemMove(fromPosition: Int, toPosition: Int) {
        // note. not using
    }

    override fun itemSwipe(todo: Todo) {
        viewModel.onRemoveTodo(todo)
    }
}