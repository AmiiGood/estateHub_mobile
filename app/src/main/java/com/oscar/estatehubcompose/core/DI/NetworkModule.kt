package com.oscar.estatehubcompose.core.DI

import com.oscar.estatehubcompose.BuildConfig
import com.oscar.estatehubcompose.analisis.data.network.AnalisisClient
import com.oscar.estatehubcompose.properties.data.network.PropertyClient
import com.oscar.estatehubcompose.analisis.data.network.GeminiClient
import com.oscar.estatehubcompose.login.data.network.LoginClient
import com.oscar.estatehubcompose.register.data.network.RegisterClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EstateHubRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeminiRetrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val BASE_URL = BuildConfig.BASE_URL;
    @Singleton
    @Provides
    @EstateHubRetrofit
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Singleton
    @Provides
    @GeminiRetrofit
    fun provideGeminiRetrofit(): Retrofit{

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGeminiClient(@GeminiRetrofit retrofit: Retrofit): GeminiClient {
        return retrofit.create(GeminiClient::class.java)
    }
    
    @Provides
    @Singleton
    fun provideLoginClient(@EstateHubRetrofit retrofit: Retrofit): LoginClient{
        return retrofit.create(LoginClient::class.java)
    }

    @Provides
    @Singleton
    fun provideRegisterClient(@EstateHubRetrofit retrofit: Retrofit): RegisterClient {
        return retrofit.create(RegisterClient::class.java)
    }

    @Provides
    @Singleton
    fun provideAnalisisClient(@EstateHubRetrofit retrofit: Retrofit): AnalisisClient {
        return retrofit.create(AnalisisClient::class.java)
    }

    @Provides
    @Singleton
    fun providePropertyClient(@EstateHubRetrofit retrofit: Retrofit): PropertyClient {
        return retrofit.create(PropertyClient::class.java)
    }
}