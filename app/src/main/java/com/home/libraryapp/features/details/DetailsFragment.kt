package com.home.libraryapp.features.details

import android.graphics.drawable.Drawable
import android.os.Bundle
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

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binging = FragmentDetailsBinding.bind(view)

        binging.apply {
            val book = args.book

            val uri = book.volumeInfo.imageLinks?.thumbnail
                ?: book.volumeInfo.imageLinks?.smallThumbnail
                ?: ""

            val fixedUri = uri.replaceFirst("http", "https")

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
                        bookDescription.isVisible = book.volumeInfo.description != null
                        return false
                    }
                })
                .into(imageView)

            bookTitle.text = book.volumeInfo.title
            bookSubtitle.text = book.volumeInfo.subtitle

            var authors = "Author: "
            if (!book.volumeInfo.authors.isNullOrEmpty()) {
                for (author in book.volumeInfo.authors)
                    authors += if (book.volumeInfo.authors.size == 1) "$author  "
                    else "$author, "
            }
            bookAuthor.text = authors.dropLast(2)

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
            if (!book.volumeInfo.description.isNullOrEmpty()) bookDescription.text =
                book.volumeInfo.description
        }
    }

}
