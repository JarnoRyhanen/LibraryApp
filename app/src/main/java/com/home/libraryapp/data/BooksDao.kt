package com.home.libraryapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {

    @Query("SELECT * FROM book_object INNER JOIN search_results ON searchedBookId = bookId")
    fun getAllSearchedBooks(): Flow<List<BookObject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(bookObject: List<BookObject>)

    @Query("DELETE FROM search_results")
    suspend fun deleteAllSearchedBooks()

}