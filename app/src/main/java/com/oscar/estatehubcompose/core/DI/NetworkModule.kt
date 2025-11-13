package com.oscar.estatehubcompose.core.DI

import com.oscar.estatehubcompose.analisis.data.network.AnalisisClient
import com.oscar.estatehubcompose.properties.data.network.PropertyClient
import com.oscar.estatehubcompose.login.data.network.LoginClient
import com.oscar.estatehubcompose.register.data.network.RegisterClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://172.16.119.35:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginClient(retrofit: Retrofit): LoginClient {
        return retrofit.create(LoginClient::class.java)
    }

    @Provides
    @Singleton
    fun provideRegisterClient(retrofit: Retrofit): RegisterClient {
        return retrofit.create(RegisterClient::class.java)
    }

    @Provides
    @Singleton
    fun provideAnalisisClient(retrofit: Retrofit): AnalisisClient {
        return retrofit.create(AnalisisClient::class.java)
    }

    @Provides
    @Singleton
    fun providePropertyClient(retrofit: Retrofit): PropertyClient {
        return retrofit.create(PropertyClient::class.java)
    }
}