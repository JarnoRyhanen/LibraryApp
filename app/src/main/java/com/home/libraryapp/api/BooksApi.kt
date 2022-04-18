package com.home.libraryapp.api

import com.home.libraryapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {

    companion object {
        const val BASE_URL = "https://www.googleapis.com/books/v1/"
        const val API_KEY = BuildConfig.LIBRARY_API_KEY
    }

    @GET("volumes?key=$API_KEY&printType=books")
    suspend fun searchBooks(
        @Query("q") searchQuery: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") pageSize: Int
    ) : BooksResponse

}