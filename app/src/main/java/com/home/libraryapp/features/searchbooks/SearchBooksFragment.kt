package com.home.libraryapp.features.searchbooks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.home.libraryapp.R
import com.home.libraryapp.databinding.FragmentBookSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchBooksFragment : Fragment(R.layout.fragment_book_search) {

    private val viewModel: SearchBooksViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBookSearchBinding.bind(view)


        binding.apply {


            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.books.collect {

                }
            }
        }
    }

}