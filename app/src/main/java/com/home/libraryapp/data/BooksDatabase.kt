package com.home.libraryapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookObject::class, SearchResults::class, SearchQueryRemoteKey::class], version = 1)
abstract class BooksDatabase : RoomDatabase() {

    abstract fun booksDao(): BooksDao

    abstract fun searchQueryRemoteKeyDao(): SearchQueryRemoteKeyDao
}