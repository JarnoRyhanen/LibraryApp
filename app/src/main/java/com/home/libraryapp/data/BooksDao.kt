package com.home.libraryapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {

    @Query("SELECT * FROM book_object INNER JOIN search_results ON searchedBookId = bookId")
    fun getAllSearchedBooks(): Flow<List<BookObject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResults(searchResults: List<SearchResults>)

    @Query("SELECT MAX(queryPosition) FROM search_results WHERE searchQuery = :searchQuery")
    suspend fun getLastQueryPosition(searchQuery: String): Int?

    @Query("DELETE FROM search_results WHERE searchQuery = :query")
    suspend fun deleteSearchedResultsForQuery(query: String)

}