package com.codeliner.achacha.todos.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codeliner.achacha.databinding.FragmentTodoCreateBinding

class TodoCreateFragment: Fragment() {

    private lateinit var binding: FragmentTodoCreateBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTodoCreateBinding.inflate(inflater)


        return binding.root
    }
}