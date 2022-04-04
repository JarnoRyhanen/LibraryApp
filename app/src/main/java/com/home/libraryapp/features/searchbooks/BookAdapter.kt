package com.home.libraryapp.features.searchbooks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.home.libraryapp.api.BookObject
import com.home.libraryapp.databinding.ItemBookBinding
import com.home.libraryapp.shared.BookComparator
import com.home.libraryapp.shared.BookViewHolder

class BookAdapter : PagingDataAdapter<BookObject, BookViewHolder>(BookComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}