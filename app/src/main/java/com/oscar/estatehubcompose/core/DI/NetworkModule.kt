package com.oscar.estatehubcompose.core.DI

import com.oscar.estatehubcompose.login.data.network.LoginClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://localhost:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    };

    @Provides
    @Singleton
    fun provideLoginClient(retrofit: Retrofit): LoginClient{
        return retrofit.create(LoginClient::class.java)
    }
};

