package com.home.libraryapp.di

import android.app.Application
import androidx.room.Room
import com.home.libraryapp.api.BooksApi
import com.home.libraryapp.data.BooksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BooksApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): BooksApi =
        retrofit.create(BooksApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): BooksDatabase =
        Room.databaseBuilder(app, BooksDatabase::class.java, "books_database")
            .fallbackToDestructiveMigration()
            .build()
}