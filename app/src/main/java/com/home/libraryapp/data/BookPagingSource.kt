package com.home.libraryapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.home.libraryapp.api.BookObjectDto
import com.home.libraryapp.api.BooksApi
import retrofit2.HttpException
import java.io.IOException

private const val BOOK_STARTING_PAGE_INDEX = 1

class BookPagingSource(
    private val booksApi: BooksApi,
    private val query: String
) : PagingSource<Int, BookObjectDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookObjectDto> {
        val position = params.key ?: BOOK_STARTING_PAGE_INDEX
        Log.d("searchbooksfragment", "onViewCreated: THIS FRAGMENT IS RECREATED")
        val startIndex = if (position == 1) {
            0
        } else {
            position * params.loadSize
        }

        return try {
            val response = booksApi.searchNews(query, startIndex, params.loadSize, "books")
            val books = response.items

            LoadResult.Page(
                data = books.orEmpty(),
                prevKey = if (position == BOOK_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (books?.isEmpty() == true) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BookObjectDto>): Int? {
        return state.anchorPosition
    }
}