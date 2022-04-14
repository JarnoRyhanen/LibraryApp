package com.home.libraryapp.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookObjectDto(
    val id: String,
    val volumeInfo: VolumeInfo
) : Parcelable {
    @Parcelize
    data class VolumeInfo(
        val title: String,
        val subtitle: String?,
        val authors: List<String>?,
        val publisher: String?,
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
        ) : Parcelable {
            val identifiers: String
                get() = "$type $identifier"
        }

        fun getAuthors(): String {
            var authorString = ""
            if (authors != null) {
                for (name in authors)
                    authorString += "$name, "
            }
            return authorString.dropLast(2)
        }

        fun getIdentifiers(): String {
            var identifier = ""
            if (industryIdentifiers != null) {
                for (i in industryIdentifiers) {
                    if (i.type == "ISBN_13") {
                        identifier = "ISBN: ${i.identifier}"
                    } else if (i.type == "OTHER") {
                        identifier = i.identifiers
                    }
                }
            }
            return identifier
        }
    }
}


