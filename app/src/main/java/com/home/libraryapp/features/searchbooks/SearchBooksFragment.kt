package com.home.libraryapp.features.searchbooks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.libraryapp.R
import com.home.libraryapp.databinding.FragmentBookSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

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
                adapter = bookAdapter.withLoadStateHeaderAndFooter(
                    header = BookLoadStateAdapter { bookAdapter.retry() },
                    footer = BookLoadStateAdapter { bookAdapter.retry() }
                )
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator?.changeDuration = 0
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.books.collectLatest {
                    bookAdapter.submitData(it)
                }
            }
            bookAdapter.addLoadStateListener { loadState ->
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    bookAdapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewError.isVisible = true
                } else {
                    textViewError.isVisible = false
                }
            }
            buttonRetry.setOnClickListener {
                bookAdapter.retry()
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchBooks(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        currentBinding = null
    }
}