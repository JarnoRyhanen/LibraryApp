package com.home.libraryapp.shared

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.home.libraryapp.R
import com.home.libraryapp.api.BookObject
import com.home.libraryapp.databinding.ItemBookBinding

private const val TAG = "BookViewHolder"

class BookViewHolder(
    private val binding: ItemBookBinding,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(position)
            }
        }
    }

    fun bind(bookObject: BookObject) {
        binding.apply {
            val uri = bookObject.volumeInfo.imageLinks?.thumbnail
                ?: bookObject.volumeInfo.imageLinks?.smallThumbnail
                ?: ""

            val fixedUri = uri.replaceFirst("http", "https")
            Log.d(TAG, "bind: ${bookObject.volumeInfo.title} $uri    ${bookObject.volumeInfo.previewLink}" )
            Glide.with(itemView)
                .load(fixedUri)
                .fitCenter()
                .error(R.drawable.cover_not_found)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)

            var authors = ""
            if (!bookObject.volumeInfo.authors.isNullOrEmpty()) {
                for (author in bookObject.volumeInfo.authors)
                    authors += if (bookObject.volumeInfo.authors.size == 1) "  $author"
                    else ", $author"
            }
            titleTextView.text = bookObject.volumeInfo.title
            textViewAuthor.text = authors.drop(2)

            var industryIdentifier = ""

            dataTextView.text = String.format(
                bookObject.volumeInfo.publishedDate +
                        if (!bookObject.volumeInfo.industryIdentifiers.isNullOrEmpty()) {
                            bookObject.volumeInfo.industryIdentifiers.indices.forEach { index ->
                                if (bookObject.volumeInfo.industryIdentifiers[index].type == "ISBN_13") ("ISBN " + bookObject.volumeInfo.industryIdentifiers[index].identifier).also {
                                    industryIdentifier = it
                                } else if (bookObject.volumeInfo.industryIdentifiers[index].type == "OTHER") bookObject.volumeInfo.industryIdentifiers[index].identifier.also {
                                    industryIdentifier = it
                                }
                            }
                            if (industryIdentifier != "") ", $industryIdentifier" else ""
                        } else {
                            Log.e(TAG, "bind: This object does not have industry identifiers")
                        }
            )

            if (bookObject.volumeInfo.averageRating != null) {
                ratingTextView.text = String.format("${bookObject.volumeInfo.averageRating}/5")
                when (bookObject.volumeInfo.averageRating) {
                    0.0F -> imageViewRating.setImageResource(R.drawable.zero_out_of_five)
                    0.5F -> imageViewRating.setImageResource(R.drawable.zero_point_five_out_of_five)
                    1.0F -> imageViewRating.setImageResource(R.drawable.one_out_of_five)
                    1.5F -> imageViewRating.setImageResource(R.drawable.one_point_five_out_of_five)
                    2.0F -> imageViewRating.setImageResource(R.drawable.two_out_of_five)
                    2.5F -> imageViewRating.setImageResource(R.drawable.two_point_five_out_of_five)
                    3.0F -> imageViewRating.setImageResource(R.drawable.three_out_of_five)
                    3.5F -> imageViewRating.setImageResource(R.drawable.three_point_five_out_of_five)
                    4.0F -> imageViewRating.setImageResource(R.drawable.four_out_of_five)
                    4.5F -> imageViewRating.setImageResource(R.drawable.four_point_five_out_of_five)
                    5.0F -> imageViewRating.setImageResource(R.drawable.five_out_of_five)
                }
            }
        }
    }


}
