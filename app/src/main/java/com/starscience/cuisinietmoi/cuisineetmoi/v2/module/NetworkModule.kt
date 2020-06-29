package com.starscience.cuisinietmoi.cuisineetmoi.v2.module

import com.starscience.cuisinietmoi.cuisineetmoi.v2.network.AppApi
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@Suppress("unused")
object NetworkModule {

    @Provides
    @Reusable
    internal fun provideRetrofitInterface() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Reusable
    internal fun provideAppApi(retrofit: Retrofit): AppApi {
        return retrofit.create(AppApi::class.java)
    }
}