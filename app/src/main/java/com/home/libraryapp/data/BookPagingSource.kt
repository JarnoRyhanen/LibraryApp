package com.home.libraryapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
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
        val startIndex = if (position == 1) {
            0
        } else {
            position * params.loadSize
        }

        return try {
            val response = booksApi.searchNews(query, startIndex, params.loadSize, "books")
            val serverBooks = response.items


            val books = serverBooks.map { serverBookObject ->
                BookObject(
                    bookId = serverBookObject.id,
                    bookTitle = serverBookObject.volumeInfo.title,
                    bookDescription = serverBookObject.volumeInfo.description,
                    bookSubtitle = serverBookObject.volumeInfo.subtitle,
                    bookAuthors = serverBookObject.volumeInfo.getAuthors(),
                    bookPublisher = serverBookObject.volumeInfo.publisher,
                    bookPublishedDate = serverBookObject.volumeInfo.publishedDate,
                    bookPageCount = serverBookObject.volumeInfo.pageCount,
                    bookAverageRating = serverBookObject.volumeInfo.averageRating,
                    bookRatingsCount = serverBookObject.volumeInfo.ratingsCount,
                    industryIdentifiers = serverBookObject.volumeInfo.getIdentifiers(),
                    bookPreviewLink = serverBookObject.volumeInfo.previewLink,
                    bookInfoLink = serverBookObject.volumeInfo.infoLink,
                    bookCanonicalVolumeLink = serverBookObject.volumeInfo.canonicalVolumeLink,
                    bookSmallThumbnail = serverBookObject.volumeInfo.imageLinks?.smallThumbnail,
                    bookThumbnail = serverBookObject.volumeInfo.imageLinks?.thumbnail
                )
            }

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
        return state.anchorPosition
    }
}