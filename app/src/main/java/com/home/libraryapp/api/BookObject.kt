package com.home.libraryapp.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookObject(
    val id: String,
    val volumeInfo: VolumeInfo
) : Parcelable {
    //TODO add authors and industryIdentifiers
    @Parcelize
    data class VolumeInfo(
        val title: String,
        val subtitle: String?,
        val authors: List<String>,
        val publishedDate: String,
        val industryIdentifiers: List<IndustryIdentifiers>?,
        val description: String?,
        val pageCount: Int,
        val averageRating: Float?,
        val ratingsCount: Int?,
        val imageLinks: ImageLinks?,
        val previewLink: String,
        val infoLink: String,
        val canonicalVolumeLink: String
    ) : Parcelable {

        @Parcelize
        data class ImageLinks(
            val smallThumbnail: String?,
            val thumbnail: String?
        ) : Parcelable

        @Parcelize
        data class IndustryIdentifiers(
            val type: String,
            val identifier: String
        ) : Parcelable
    }
}


