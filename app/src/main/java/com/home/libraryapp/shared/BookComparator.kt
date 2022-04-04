package com.home.libraryapp.shared

import androidx.recyclerview.widget.DiffUtil
import com.home.libraryapp.api.BookObject

class BookComparator : DiffUtil.ItemCallback<BookObject>() {
    override fun areItemsTheSame(oldItem: BookObject, newItem: BookObject) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BookObject, newItem: BookObject) =
        oldItem == newItem
}