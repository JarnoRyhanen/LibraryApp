package com.home.libraryapp.data

import com.home.libraryapp.api.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val booksApi: BooksApi
) {


}