package com.example.weather.di

import android.content.Context
import com.example.weather.api.ApiInterface
import com.example.weather.data.local.database.CitiesWeatherDatabase
import com.example.weather.data.local.preferences.PreferenceStorage
import com.example.weather.data.local.preferences.SharedPreferenceStorage
import com.example.weather.data.local.iconsStorage.WeatherIconsStorage
import com.example.weather.util.Constants
import com.example.weather.util.Constants.ICONS_PATH
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }
        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL_RETROFIT_API)
            .client(okHttpClient)
            .build()

    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CitiesWeatherDatabase =
        CitiesWeatherDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)

    @Provides
    @Singleton
    fun provideFileImageStorage(@ApplicationContext context: Context): WeatherIconsStorage =
        WeatherIconsStorage(context, ICONS_PATH)
}