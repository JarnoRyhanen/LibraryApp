package com.home.libraryapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.home.libraryapp.api.BookObject
import com.home.libraryapp.api.BooksApi
import retrofit2.HttpException
import java.io.IOException

private const val BOOK_STARTING_PAGE_INDEX = 1

class BookPagingSource(
    private val booksApi: BooksApi,
    private val query: String
) : PagingSource<Int, BookObject>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookObject> {
        val position = params.key ?: BOOK_STARTING_PAGE_INDEX

        return try {
            val response = booksApi.searchNews(query, position, params.loadSize, "books")
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

    override fun getRefreshKey(state: PagingState<Int, BookObject>): Int? {
        TODO("Not yet implemented")
    }
}