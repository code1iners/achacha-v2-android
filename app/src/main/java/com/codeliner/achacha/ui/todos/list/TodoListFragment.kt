package com.codeliner.achacha.ui.todos.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.codeliner.achacha.databinding.FragmentTodoListBinding
import com.codeliner.achacha.data.todos.Todo
import com.codeliner.achacha.data.AppDatabase
import com.codeliner.achacha.data.todos.TodoRepository
import com.codeliner.achacha.mains.MainActivity
import com.codeliner.achacha.utils.Const
import com.codeliner.achacha.utils.log
import com.example.helpers.toastForShort
import com.example.helpers.ui.AnimationManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class TodoListFragment: Fragment()
    , TodoClickListener
    , TodoMoveListener
{

    private lateinit var binding: FragmentTodoListBinding
    private val viewModel: TodoListViewModel by viewModel()

    // note. adapters
    private lateinit var todoAdapter: TodoAdapter

    // note. animations (ui)
    lateinit var animHeaderShow: Animation
    lateinit var animHeaderHide: Animation
    lateinit var animRotateLeft: Animation
    lateinit var animRotateRight: Animation
    lateinit var animHide: Animation
    lateinit var animShow: Animation
    lateinit var transition: AutoTransition

    // note. actions

    override fun onStop() {
        super.onStop()

        // note. header
        binding.fragmentTodoListCalendarContainer.startAnimation(animHeaderHide)
        // note. header
        binding.fragmentTodoListCalendarDividerBottom.startAnimation(animHeaderHide)
        // note. body
        binding.fragmentTodoListTodoList.startAnimation(
                AnimationManager.getFadeOut(requireContext()).apply {
                    duration = Const.animDefaultDuration
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
        binding.fragmentTodoListTodoList.startAnimation(
                AnimationManager.getFadeIn(requireContext()).apply {
                    duration = Const.animDefaultDuration
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
            duration = Const.animDefaultDuration
            fillAfter = true
        }
        animShow = AnimationManager.getFadeIn(requireContext()).apply {
            duration = Const.animDefaultDuration
            fillAfter = true
        }

        transition = AutoTransition().apply {
            duration = Const.animDefaultDuration
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    private fun initAdapters() {
        todoAdapter = TodoAdapter(this, this)
        binding.fragmentTodoListTodoList.adapter = todoAdapter
        val helper = ItemTouchHelper(ItemTouchHelperCallback(todoAdapter))
        helper.attachToRecyclerView(binding.fragmentTodoListTodoList)
    }

    private fun initObservers() {
        // note. fab main
        observeFavMain()
        // note. fab menu 1
        observeFavOne()
        // note. fab menu test
        observeFabTest()
        // note. todos
        observeTodos()
    }

    private fun observeFavMain() {
        val cs = ConstraintSet()
        cs.clone(binding.fragmentTodoListFabList)
        viewModel.isFavCollapsed.observe(viewLifecycleOwner, Observer { isCollapsed ->
            if (!isCollapsed) {
                cs.connect(binding.fragmentTodoListFabCreate.id, ConstraintSet.BOTTOM, binding.fragmentTodoListFabMain.id, ConstraintSet.TOP)
                cs.connect(binding.fragmentTodoListFabClear.id, ConstraintSet.BOTTOM, binding.fragmentTodoListFabCreate.id, ConstraintSet.TOP)
//                cs.connect(binding.fragmentTodoListFabTest.id, ConstraintSet.BOTTOM, binding.fragmentTodoListFabClear.id, ConstraintSet.TOP)

                binding.fragmentTodoListFabMain.startAnimation(animRotateRight)
                binding.fragmentTodoListFabCreate.startAnimation(animShow)
                binding.fragmentTodoListFabClear.startAnimation(animShow)
//                binding.fragmentTodoListFabTest.startAnimation(animShow)

            } else {
                cs.connect(binding.fragmentTodoListFabCreate.id, ConstraintSet.BOTTOM, binding.fragmentTodoListFabList.id, ConstraintSet.BOTTOM)
                cs.connect(binding.fragmentTodoListFabClear.id, ConstraintSet.BOTTOM, binding.fragmentTodoListFabList.id, ConstraintSet.BOTTOM)
//                cs.connect(binding.fragmentTodoListFabTest.id, ConstraintSet.BOTTOM, binding.fragmentTodoListFabList.id, ConstraintSet.BOTTOM)

                binding.fragmentTodoListFabMain.startAnimation(animRotateLeft)
                binding.fragmentTodoListFabCreate.startAnimation(animHide)
                binding.fragmentTodoListFabClear.startAnimation(animHide)
//                binding.fragmentTodoListFabTest.startAnimation(animHide)
            }

            TransitionManager.beginDelayedTransition(binding.fragmentTodoListFabList, transition)
            cs.applyTo(binding.fragmentTodoListFabList)
        })
    }

    private fun observeFavOne() {
        viewModel.onNavigateToCreateTodoReady.observe(viewLifecycleOwner, Observer { isReady ->
            if (isReady) {
                // note. update ui
                MainActivity.onBottomNavigationHide()
                binding.fragmentTodoListCalendarContainer.startAnimation(animHeaderHide)
                binding.fragmentTodoListCalendarDividerBottom.startAnimation(animHeaderHide)
                binding.fragmentTodoListTodoList.startAnimation(animHide)
                binding.fragmentTodoListFabList.startAnimation(animHide)

                viewModel.navigateToCreateTodoReady()
                viewModel.navigateToCreateTodoComplete()
            }
        })

        viewModel.onNavigateToCreateTodoProcess.observe(viewLifecycleOwner, Observer { start ->
            if (start) {

                findNavController().navigate(TodoListFragmentDirections.actionTodoListFragmentToTodoCreateFragment(viewModel.tasks.value ?: -1))

                viewModel.navigateToCreateTodoProcessComplete()
            }
        })
    }

    private fun observeFabTest() {
        viewModel.onTestTrigger.observe(viewLifecycleOwner, Observer {
            if (it) {
                context?.toastForShort("Test button clicked!")
                viewModel.onTestComplete()
            }
        })
    }

    private fun observeTodos() {
        viewModel.todos.observe(viewLifecycleOwner, Observer { todos ->

            todoAdapter.testSubmitList(todos.map { it.copy() })
        })
    }

    override fun onClick(todo: Todo) {
        todo.log("d")
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