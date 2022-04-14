package com.home.libraryapp.features.searchbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.home.libraryapp.data.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchBooksViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val currentQuery = MutableStateFlow<String?>(DEFAULT_QUERY)

    @ExperimentalPagingApi
    val books = currentQuery.flatMapLatest { queryString ->
        queryString?.let {
            repository.getSearchResultsPaged(queryString).cachedIn(viewModelScope)
        } ?: emptyFlow()
    }

    fun searchBooks(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "Harry potter"
    }
}