package com.home.libraryapp.features.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
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

            val uri = book.bookThumbnail
                ?: book.bookSmallThumbnail
                ?: ""

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
                        bookDescription.isVisible = !book.bookDescription.isNullOrEmpty()
                        bookSubtitle.isVisible = book.bookSubtitle != null
                        bookAuthor.isVisible = !book.bookAuthors.isNullOrEmpty()
                        bookRatingImage.isVisible = book.bookAverageRating != null
                        bookRating.isVisible = book.bookAverageRating != null

                        bookProductInformationAuthor.isVisible = !book.bookAuthors.isNullOrEmpty()
                        bookProductInformationIndustryIdentifier.isVisible = !book.industryIdentifiers.isNullOrEmpty()
                        bookProductInformationReleaseDate.isVisible = !book.bookPublishedDate.isNullOrEmpty()
                        bookProductInformationPageCount.isVisible = book.bookPageCount != null
                        bookProductInformationPublisher.isVisible = !book.bookPublisher.isNullOrEmpty()
                        return false
                    }
                })
                .into(imageView)

            bookTitle.text = book.bookTitle
            bookSubtitle.text = book.bookSubtitle
            bookAuthor.text = book.bookAuthors

            if (book.bookAverageRating != null) {
                when (book.bookAverageRating) {
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
                    if (!book.bookDescription.isNullOrEmpty()) {
                        bookDescription.text = book.bookDescription
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
                    bookProductInformationAuthor.text = book.bookAuthors

                    bookProductInformationIndustryIdentifier.text = book.industryIdentifiers
                    bookProductInformationReleaseDate.text =
                        String.format("Published: ${book.bookPublishedDate}")
                    bookProductInformationPageCount.text =
                        String.format("Number of pages: ${book.bookPageCount}")
                    bookProductInformationPublisher.text =
                        String.format("Publisher: ${book.bookPublisher}")
                }
            }
            }
        }
    }

