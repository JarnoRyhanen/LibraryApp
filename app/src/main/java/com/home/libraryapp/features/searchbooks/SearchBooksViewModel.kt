package com.home.libraryapp.features.searchbooks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.home.libraryapp.data.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchBooksViewModel @Inject constructor(
    private val repository: BookRepository,
    state: SavedStateHandle
) : ViewModel() {


    private val currentQuery = state.getLiveData<String?>("currentQuery", null)

    val hasCurrentQuery = currentQuery.asFlow().map { it != null }

    @ExperimentalPagingApi
    val books = currentQuery.asFlow().flatMapLatest { queryString ->
        queryString?.let {
            repository.getSearchResultsPaged(queryString).cachedIn(viewModelScope)
        } ?: emptyFlow()
    }

    fun searchBooks(query: String) {
        currentQuery.value = query
    }
}