package com.home.libraryapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.home.libraryapp.api.Book
import com.home.libraryapp.api.BooksApi
import retrofit2.HttpException
import java.io.IOException

private const val BOOK_STARTING_PAGE_INDEX = 1

class BookPagingSource(
    private val booksApi: BooksApi,
    private val query: String
) : PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val position = params.key ?: BOOK_STARTING_PAGE_INDEX

        return try {
            val response = booksApi.searchNews(query, position, params.loadSize, "book")
            val books = response.items

            LoadResult.Page(
                data = books,
                prevKey = if (position == BOOK_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (books.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        TODO("Not yet implemented")
    }
}