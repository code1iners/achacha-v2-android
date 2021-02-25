package com.codeliner.achacha.titles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codeliner.achacha.databinding.FragmentTitleBinding

class TitleFragment: Fragment() {

    private lateinit var binding: FragmentTitleBinding
    private lateinit var viewModelFactory: TitleViewModelFactory
    private lateinit var viewModel: TitleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTitleBinding.inflate(inflater)

        initViewModel()

        return binding.root
    }

    private fun initViewModel() {
        val app = requireNotNull(activity).application
        viewModelFactory = TitleViewModelFactory(app)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TitleViewModel::class.java)
        // note. assignment view model into layout
        binding.viewModel = viewModel
    }
}