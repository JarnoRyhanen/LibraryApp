package com.home.libraryapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "book_object")
@Parcelize
data class BookObject(
    // Volume info
    @PrimaryKey val bookId: String,
    val bookTitle: String?,
    val bookSubtitle: String?,
    val bookAuthors: String?,
    val bookPublisher: String?,
    val bookPublishedDate: String?,
    val bookDescription: String?,
    val bookPageCount: Int?,
    val bookAverageRating: Float?,
    val bookRatingsCount: Int?,

    val industryIdentifiers: String?,

    // Links
    val bookPreviewLink: String?,
    val bookInfoLink: String?,
    val bookCanonicalVolumeLink: String?,

    // Cover images
    val bookSmallThumbnail: String?,
    val bookThumbnail: String?,
) : Parcelable

@Entity(tableName = "search_results", primaryKeys = ["searchQuery","searchedBookId"])
data class SearchResults(
    val searchQuery: String,
    val searchedBookId: String,
    val queryPosition: Int
)

