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
import com.se104.passbookapp.data.remote.api.ReportApiService
import com.se104.passbookapp.data.remote.api.SavingTicketApiService
import com.se104.passbookapp.data.remote.api.SavingTypeApiService
import com.se104.passbookapp.data.remote.api.TransactionApiService
import com.se104.passbookapp.data.remote.api.UserApiService
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
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Provides
    @Singleton
    fun provideAuthAuthenticator(tokenManager: TokenManager, authApiService: AuthApiService): AuthAuthenticator =
        AuthAuthenticator(tokenManager, authApiService)


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
            .authenticator(authAuthenticator)
            .addInterceptor(loggingInterceptor)
            .build()
    }



    @Provides
    @Singleton
    @Named("MainApi")
    fun provideRetrofitMainApi(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
            .registerTypeAdapter(LocalTime::class.java, LocalTimeDeserializer())
            .registerTypeAdapter(BigDecimal::class.java, BigDecimalDeserializer())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
            .setLenient()
            .create()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BACKEND_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitAuth(): Retrofit {
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
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthAPIService(retrofit: Retrofit): AuthApiService {
        return retrofit
            .create(AuthApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideSavingTypeAPIService(
        @Named("MainApi") retrofit: Retrofit,
    ): SavingTypeApiService =
        retrofit
            .create(SavingTypeApiService::class.java)

    @Provides
    @Singleton
    fun provideUserAPIService(
        @Named("MainApi") retrofit: Retrofit,
    ): UserApiService =
        retrofit
            .create(UserApiService::class.java
    )



    @Provides
    @Singleton
    fun provideParameterAPIService(
        @Named("MainApi") retrofit: Retrofit,
    ): ParameterApiService =
        retrofit
            .create(ParameterApiService::class.java)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
