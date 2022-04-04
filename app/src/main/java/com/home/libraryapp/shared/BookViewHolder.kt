package com.home.libraryapp.shared

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.home.libraryapp.R
import com.home.libraryapp.api.BookObject
import com.home.libraryapp.databinding.ItemBookBinding

class BookViewHolder(
    private val binding: ItemBookBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(bookObject: BookObject) {
        binding.apply {

            val uri = bookObject.volumeInfo.imageLinks?.thumbnail ?: bookObject.volumeInfo.imageLinks?.smallThumbnail
            ?: ""

            val fixedUri = uri.replaceFirst("http", "https")

            Glide.with(itemView)
                .load(fixedUri)
                .fitCenter()
                .error(R.drawable.img)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)

            titleTextView.text = bookObject.volumeInfo.title
            textViewAuthor.text = bookObject.volumeInfo.subtitle
            dataTextView.text = String.format(
                bookObject.volumeInfo.publishedDate + ", "
                        + bookObject.volumeInfo.language
            )
            if (bookObject.volumeInfo.averageRating != null) {
                ratingTextView.text = String.format("${bookObject.volumeInfo.averageRating}/5")
            }
        }
    }
}
