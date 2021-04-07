package com.latihangoding.tittle_tattle.di

import android.app.Application
import androidx.room.Room
import com.latihangoding.tittle_tattle.BuildConfig
import com.latihangoding.tittle_tattle.api.ApiService
import com.latihangoding.tittle_tattle.db.gallery.GalleryDatabase
import com.latihangoding.tittle_tattle.db.weather.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideGalleryDb(app: Application) =
        Room.databaseBuilder(app, GalleryDatabase::class.java, "gallery.db")
            .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideGalleryDao(db: GalleryDatabase) = db.galleryDao()

    @Singleton
    @Provides
    fun provideWeatherDb(app: Application) =
        Room.databaseBuilder(app, WeatherDatabase::class.java, "weather.db")
            .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideWeatherDao(db: WeatherDatabase) = db.weatherDao()

    @Singleton
    @Provides
    fun provideApiService(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = provideService(okHttpClient, converterFactory, ApiService::class.java)

    private fun createRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()

    @UploadApi
    @Singleton
    @Provides
    fun provideUploadApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = createUploadRetrofit(okHttpClient, converterFactory).create(ApiService::class.java)

    @WeatherApi
    @Singleton
    @Provides
    fun provideWeatherApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = createWeatherRetrofit(okHttpClient, converterFactory).create(ApiService::class.java)

    private fun createUploadRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = Retrofit.Builder()
        .baseUrl(BuildConfig.UPLOAD_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()

    private fun createWeatherRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = Retrofit.Builder()
        .baseUrl(BuildConfig.WEATHER_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()

    private fun <T> provideService(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
        clazz: Class<T>
    ): T = createRetrofit(okHttpClient, converterFactory).create(clazz)

}