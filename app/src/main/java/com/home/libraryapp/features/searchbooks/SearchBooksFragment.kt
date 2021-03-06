package com.home.libraryapp.features.searchbooks

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.libraryapp.R
import com.home.libraryapp.databinding.FragmentBookSearchBinding
import com.home.libraryapp.util.onQueryTextSubmit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.log

@ExperimentalPagingApi
@AndroidEntryPoint
class SearchBooksFragment : Fragment(R.layout.fragment_book_search) {

    private val viewModel: SearchBooksViewModel by viewModels()

    private var currentBinding: FragmentBookSearchBinding? = null
    private val binding get() = currentBinding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentBinding = FragmentBookSearchBinding.bind(view)

        val bookAdapter = BookAdapter(
            onItemClick = { bookObject ->
                val action = SearchBooksFragmentDirections.actionSearchBooksFragmentToDetailsFragment(bookObject)
                findNavController().navigate(action)
            }
        )

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
                viewModel.books.collectLatest { data ->
                    bookAdapter.submitData(data)
                }
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.hasCurrentQuery.collect { hasCurrentQuery ->

                    textViewInstructions.isVisible = !hasCurrentQuery
                    if (!hasCurrentQuery) {
                        recyclerView.isVisible = false
                    }
                }
            }

            bookAdapter.addLoadStateListener { loadState ->
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
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

        searchView.onQueryTextSubmit { query ->
            binding.recyclerView.scrollToPosition(0)
            viewModel.searchBooks(query)
            searchView.clearFocus()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentBinding = null
    }
}