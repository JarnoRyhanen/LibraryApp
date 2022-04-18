package com.home.libraryapp.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.home.libraryapp.api.BooksApi
import retrofit2.HttpException
import java.io.IOException

private const val BOOKS_STARTING_PAGE_INDEX = 1

private const val TAG = "SearchBooksRemoteMediat"

@ExperimentalPagingApi
class SearchBooksRemoteMediator(
    private val searchQuery: String,
    private val booksApi: BooksApi,
    private val booksDatabase: BooksDatabase
) : RemoteMediator<Int, BookObject>() {

    private val booksDao = booksDatabase.booksDao()
    private val searchQueryRemoteKeyDao = booksDatabase.searchQueryRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BookObject>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> BOOKS_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> searchQueryRemoteKeyDao.getRemoteKey(searchQuery).nextPageKey
        }

        val index = if (page == BOOKS_STARTING_PAGE_INDEX) {
            0
        } else {
            page.times(40).minus(40)
        }

        try {
            Log.d(TAG, "index: $index, page: $page")
            val response = booksApi.searchBooks(searchQuery, index, 40)
            val serverSearchResults = response.items

            if (serverSearchResults != null) {
                val searchResultBooks = serverSearchResults.map { serverBookObject ->
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

                booksDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        booksDao.deleteSearchedResultsForQuery(searchQuery)
                    }

                    val lastQueryPosition = booksDao.getLastQueryPosition(searchQuery) ?: 0
                    var queryPosition = lastQueryPosition + 1

                    val searchResults = searchResultBooks.map {
                        SearchResults(searchQuery, it.bookId, queryPosition++)
                    }
                    val nextPageKey = page + 1

                    booksDao.insertBooks(searchResultBooks)
                    booksDao.insertSearchResults(searchResults)
                    searchQueryRemoteKeyDao.insertRemoteKey(
                        SearchQueryRemoteKey(searchQuery, nextPageKey)
                    )
                }
            }
            return MediatorResult.Success(endOfPaginationReached = serverSearchResults.isNullOrEmpty())
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}