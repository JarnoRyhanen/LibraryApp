package com.home.libraryapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.home.libraryapp.api.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val booksApi: BooksApi
) {

fun getSearchResults(query: String) =
    Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false,
            initialLoadSize = 40
        ),
        pagingSourceFactory = { BookPagingSource(booksApi, query) }
    ).flow
}