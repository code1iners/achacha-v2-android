package com.codeliner.achacha.todos.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.databinding.FragmentTodoCreateBinding
import com.codeliner.achacha.mains.MainActivity

class TodoCreateFragment: Fragment() {

    private lateinit var binding: FragmentTodoCreateBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTodoCreateBinding.inflate(inflater)
        binding.lifecycleOwner = this

        initBackPressed()

        return binding.root
    }

    private fun initBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            MainActivity.onBottomNavigationSwitch()
            this@TodoCreateFragment.findNavController().popBackStack()
        }
    }
}