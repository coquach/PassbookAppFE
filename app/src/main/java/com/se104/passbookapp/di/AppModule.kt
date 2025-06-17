package com.se104.passbookapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.se104.passbookapp.utils.gson.BigDecimalDeserializer
import com.se104.passbookapp.utils.gson.LocalDateDeserializer
import com.se104.passbookapp.utils.gson.LocalDateTimeDeserializer
import com.se104.passbookapp.utils.gson.LocalTimeDeserializer

import com.google.gson.GsonBuilder
import com.se104.passbookapp.BuildConfig
import com.se104.passbookapp.data.remote.api.AuthApiService
import com.se104.passbookapp.data.remote.api.ParameterApiService
import com.se104.passbookapp.data.remote.api.SalesReportApiService
import com.se104.passbookapp.data.remote.api.SavingTicketApiService
import com.se104.passbookapp.data.remote.api.SavingTypeApiService
import com.se104.passbookapp.data.remote.api.TransactionApiService
import com.se104.passbookapp.data.remote.api.WithdrawalApiService
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


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideRetrofit(): Retrofit.Builder {
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
    fun provideSavingTypeAPIService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): SavingTypeApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(SavingTypeApiService::class.java)

    @Provides
    @Singleton
    fun provideSavingTicketAPIService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): SavingTicketApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(SavingTicketApiService::class.java)

    @Provides
    @Singleton
    fun provideTransactionAPIService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): TransactionApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(TransactionApiService::class.java)

    @Provides
    @Singleton
    fun provideWithdrawalAPIService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): WithdrawalApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(WithdrawalApiService::class.java)

    @Provides
    @Singleton
    fun provideSalesReportAPIService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): SalesReportApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(SalesReportApiService::class.java)

    @Provides
    @Singleton
    fun provideParameterAPIService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): ParameterApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(ParameterApiService::class.java)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
