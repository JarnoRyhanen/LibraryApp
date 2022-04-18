package com.home.libraryapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.home.libraryapp.api.BooksApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val booksApi: BooksApi,
    private val booksDatabase: BooksDatabase
) {
    private val booksDao = booksDatabase.booksDao()

    @ExperimentalPagingApi
    fun getSearchResultsPaged(query: String): Flow<PagingData<BookObject>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                initialLoadSize = 40
            ), remoteMediator = SearchBooksRemoteMediator(query, booksApi, booksDatabase),
            pagingSourceFactory = {
                booksDao.getSearchResultBooksPaged(query)
            }
        ).flow
}