package com.home.libraryapp.features.searchbooks

import androidx.lifecycle.ViewModel
import com.home.libraryapp.data.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchBooksViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {



}