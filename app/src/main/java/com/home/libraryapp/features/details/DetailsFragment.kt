package com.home.libraryapp.features.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.home.libraryapp.R
import com.home.libraryapp.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "DetailsFragment"

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binging = FragmentDetailsBinding.bind(view)

        binging.apply {
            val book = args.book

            var uri = ""
            if (book.volumeInfo.imageLinks != null) {
                 uri = book.volumeInfo.imageLinks.thumbnail
                    ?: book.volumeInfo.imageLinks.smallThumbnail
                    ?: ""
            }

           val fixedUri = if (uri.isNotEmpty()) uri.replaceFirst("http", "https") else null

            Glide.with(this@DetailsFragment)
                .load(fixedUri)
                .error(R.drawable.cover_not_found)
                .fitCenter()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        bookTitle.isVisible = true
                        bookSubtitle.isVisible = book.volumeInfo.subtitle != null
                        bookAuthor.isVisible = !book.volumeInfo.authors.isNullOrEmpty()
                        bookRatingImage.isVisible = book.volumeInfo.averageRating != null
                        bookRating.isVisible = book.volumeInfo.averageRating != null
                        return false
                    }
                })
                .into(imageView)

            bookTitle.text = book.volumeInfo.title
            bookSubtitle.text = book.volumeInfo.subtitle ?: ""

            var authors = "Author: "
            if (!book.volumeInfo.authors.isNullOrEmpty()) {
                for (author in book.volumeInfo.authors)
                    authors += if (book.volumeInfo.authors.size == 1) "$author  "
                    else "$author, "
                bookAuthor.text = authors.dropLast(2)
            }

            if (book.volumeInfo.averageRating != null) {
                when (book.volumeInfo.averageRating) {
                    0.0F -> bookRatingImage.setImageResource(R.drawable.zero_out_of_five)
                    0.5F -> bookRatingImage.setImageResource(R.drawable.zero_point_five_out_of_five)
                    1.0F -> bookRatingImage.setImageResource(R.drawable.one_out_of_five)
                    1.5F -> bookRatingImage.setImageResource(R.drawable.one_point_five_out_of_five)
                    2.0F -> bookRatingImage.setImageResource(R.drawable.two_out_of_five)
                    2.5F -> bookRatingImage.setImageResource(R.drawable.two_point_five_out_of_five)
                    3.0F -> bookRatingImage.setImageResource(R.drawable.three_out_of_five)
                    3.5F -> bookRatingImage.setImageResource(R.drawable.three_point_five_out_of_five)
                    4.0F -> bookRatingImage.setImageResource(R.drawable.four_out_of_five)
                    4.5F -> bookRatingImage.setImageResource(R.drawable.four_point_five_out_of_five)
                    5.0F -> bookRatingImage.setImageResource(R.drawable.five_out_of_five)
                }
            }

            openDescriptionArrow.setOnClickListener {
                if (bookDescription.visibility == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(
                        bookDescriptionCardView,
                        AutoTransition()
                    )
                    bookDescription.visibility = View.GONE
                } else {
                    TransitionManager.beginDelayedTransition(
                        bookDescriptionCardView,
                        AutoTransition()
                    )
                    bookDescription.visibility = View.VISIBLE
                    if (!book.volumeInfo.description.isNullOrEmpty()) {
                        bookDescription.text = book.volumeInfo.description
                    }
                }
            }
            openInformationArrow.setOnClickListener {
                if (bookProductInformationLayout.visibility == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(
                        bookProductInformation,
                        AutoTransition()
                    )
                    bookProductInformationLayout.visibility = View.GONE
                } else {
                    TransitionManager.beginDelayedTransition(
                        bookProductInformation,
                        AutoTransition()
                    )
                    bookProductInformationLayout.visibility = View.VISIBLE
                    bookProductInformationAuthor.text = authors

                    var industryIdentifier = ""
                    if (!book.volumeInfo.industryIdentifiers.isNullOrEmpty()) {
                        book.volumeInfo.industryIdentifiers.indices.forEach { index ->
                            if (book.volumeInfo.industryIdentifiers[index].type == "ISBN_13") ("ISBN " + book.volumeInfo.industryIdentifiers[index].identifier).also {
                                industryIdentifier = it
                            } else if (book.volumeInfo.industryIdentifiers[index].type == "OTHER") book.volumeInfo.industryIdentifiers[index].identifier.also {
                                industryIdentifier = it
                            }
                        }
                        if (industryIdentifier != "") ", $industryIdentifier" else ""
                    } else {
                        Log.e(TAG, "bind: This object does not have industry identifiers")
                    }

                    bookProductInformationIndustryIdentifier.text = industryIdentifier
                    bookProductInformationReleaseDate.text = String.format("Published: ${book.volumeInfo.publishedDate}")
                    bookProductInformationPageCount.text = String.format("Number of pages: ${book.volumeInfo.pageCount}")
                    bookProductInformationPublisher.text = String.format("Publisher: ${book.volumeInfo.publisher}")
                }
            }
        }
    }
}
