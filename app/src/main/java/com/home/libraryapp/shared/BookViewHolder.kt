package com.home.libraryapp.shared

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.home.libraryapp.R
import com.home.libraryapp.data.BookObject
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
            val uri = bookObject.bookThumbnail
                ?: bookObject.bookSmallThumbnail
                ?: ""

            val fixedUri = uri.replaceFirst("http", "https")
            Glide.with(itemView)
                .load(fixedUri)
                .fitCenter()
                .error(R.drawable.cover_not_found)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)

            titleTextView.text = bookObject.bookTitle
            textViewAuthor.text = bookObject.bookAuthors

            dataTextView.text = String.format("${bookObject.bookPublishedDate}, ${bookObject.industryIdentifiers}")

            if (bookObject.bookAverageRating != null) {
                ratingTextView.text = String.format("${bookObject.bookAverageRating}/5")
                when (bookObject.bookAverageRating) {
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
