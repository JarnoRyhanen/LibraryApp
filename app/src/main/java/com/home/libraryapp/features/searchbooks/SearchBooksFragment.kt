package com.home.libraryapp.features.searchbooks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.libraryapp.R
import com.home.libraryapp.databinding.FragmentBookSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchBooksFragment : Fragment(R.layout.fragment_book_search) {

    private val viewModel: SearchBooksViewModel by viewModels()

    private var currentBinding: FragmentBookSearchBinding? = null
    private val binding get() = currentBinding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentBinding = FragmentBookSearchBinding.bind(view)

        val bookAdapter = BookAdapter()

        binding.apply {
            recyclerView.apply {
                adapter = bookAdapter
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator?.changeDuration = 0
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.books.collect {
                    bookAdapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentBinding = null
    }
}