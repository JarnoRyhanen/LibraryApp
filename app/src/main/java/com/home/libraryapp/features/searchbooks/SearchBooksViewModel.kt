package com.home.libraryapp.features.searchbooks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.home.libraryapp.api.BookObject
import com.home.libraryapp.data.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchBooksViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val currentQuery = MutableStateFlow(DEFAULT_QUERY)

    val books = currentQuery.flatMapLatest { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchBooks(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "Brandon Sanderson"
    }
}