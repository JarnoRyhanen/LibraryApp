package com.home.libraryapp.shared

import androidx.recyclerview.widget.DiffUtil
import com.home.libraryapp.api.BookObjectDto
import com.home.libraryapp.data.BookObject

class BookComparator : DiffUtil.ItemCallback<BookObject>() {
    override fun areItemsTheSame(oldItem: BookObject, newItem: BookObject) =
        oldItem.bookId == newItem.bookId

    override fun areContentsTheSame(oldItem: BookObject, newItem: BookObject) =
        oldItem == newItem
}