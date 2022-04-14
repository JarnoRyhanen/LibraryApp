package com.home.libraryapp.features.searchbooks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.home.libraryapp.api.BookObjectDto
import com.home.libraryapp.databinding.ItemBookBinding
import com.home.libraryapp.shared.BookComparator
import com.home.libraryapp.shared.BookViewHolder

class BookAdapter(
    private val onItemClick: (BookObjectDto) -> Unit
) : PagingDataAdapter<BookObjectDto, BookViewHolder>(BookComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding,
            onItemClick = { position ->
                val book = getItem(position)
                if (book != null) {
                    onItemClick(book)
                }
            })
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}