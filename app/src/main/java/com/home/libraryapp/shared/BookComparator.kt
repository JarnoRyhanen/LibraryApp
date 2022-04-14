package com.home.libraryapp.shared

import androidx.recyclerview.widget.DiffUtil
import com.home.libraryapp.api.BookObjectDto

class BookComparator : DiffUtil.ItemCallback<BookObjectDto>() {
    override fun areItemsTheSame(oldItem: BookObjectDto, newItem: BookObjectDto) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BookObjectDto, newItem: BookObjectDto) =
        oldItem == newItem
}