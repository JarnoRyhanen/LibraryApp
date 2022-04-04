package com.home.libraryapp.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
) : Parcelable {
    //TODO add authors and industryIdentifiers
    @Parcelize
    data class VolumeInfo(
        val title: String,
        val subtitle: String?,
        val publishedDate: String,
        val description: String?,
        val pageCount: Int,
        val averageRating: Int?,
        val ratingsCount: Int?,
        val imageLinks: ImageLinks,
        val language: String,
        val previewLink: String,
        val infoLink: String,
        val canonicalVolumeLink: String
    ) : Parcelable {

        @Parcelize
        data class ImageLinks(
            val smallThumbnail: String?,
            val thumbnail: String?
        ) : Parcelable
    }
}


