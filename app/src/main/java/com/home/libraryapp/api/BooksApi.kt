package com.home.libraryapp.api

import com.home.libraryapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {

    companion object {
        const val BASE_URL = "https://www.googleapis.com/books/v1/"
        const val API_KEY = BuildConfig.LIBRARY_API_KEY
    }

    @GET("volumes?key=$API_KEY")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") pageSize: Int,
        @Query("printType") printType: String
    ) : BooksResponse

}