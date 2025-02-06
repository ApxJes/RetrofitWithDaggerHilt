package com.example.retrofitwithdagger_hilt.di

import android.content.Context
import androidx.room.Room
import com.example.retrofitwithdagger_hilt.api.NewsApi
import com.example.retrofitwithdagger_hilt.db.NewsDao
import com.example.retrofitwithdagger_hilt.db.NewsDatabase
import com.example.retrofitwithdagger_hilt.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Singleton
    @Provides
    fun providesRetrofitInstance(): Retrofit{
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesApi(retrofit: Retrofit): NewsApi =
        retrofit.create(NewsApi::class.java)

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news_db"
        ).build()

    @Singleton
    @Provides
    fun providesNewsDao(database: NewsDatabase): NewsDao =
        database.newsDao()
}