package com.se104.passbookapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.foodapp.utils.gson.BigDecimalDeserializer
import com.example.foodapp.utils.gson.LocalDateDeserializer
import com.example.foodapp.utils.gson.LocalDateTimeDeserializer
import com.example.foodapp.utils.gson.LocalTimeDeserializer

import com.google.gson.GsonBuilder
import com.se104.passbookapp.BuildConfig
import com.se104.passbookapp.data.remote.api.AuthApiService
import com.se104.passbookapp.data.remote.api.MainApiService
import com.se104.passbookapp.data.remote.api.SavingApiService
import com.se104.passbookapp.data.remote.interceptor.AuthAuthenticator
import com.se104.passbookapp.data.remote.interceptor.AuthInterceptor

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")
@Module
@InstallIn(SingletonComponent::class)
object AppModule  {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Provides
    @Singleton
    fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator =
        AuthAuthenticator(tokenManager)

    @Provides
    @Singleton
    fun provideRetrofit():  Retrofit.Builder {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
            .registerTypeAdapter(LocalTime::class.java, LocalTimeDeserializer())
            .registerTypeAdapter(BigDecimal::class.java, BigDecimalDeserializer())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))

    }

    @Provides
    @Singleton
    fun provideAuthAPIService(retrofit: Retrofit.Builder): AuthApiService {
       return retrofit
           .build()
           .create(AuthApiService::class.java)
    }



    @Provides
    @Singleton
    fun provideSavingAPIService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): SavingApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(SavingApiService::class.java)



    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}
