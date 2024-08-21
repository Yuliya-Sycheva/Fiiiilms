package com.itproger.spr_15_clean_architecture_films.di

import androidx.room.Room
import data.network.IMDbApiService

import data.NetworkClient
import data.converters.MovieCastConverter
import data.db.AppDatabase
import data.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<IMDbApiService> {
        Retrofit.Builder()
            .baseUrl("https://tv-api.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IMDbApiService::class.java)
    }

    single<NetworkClient> {
         RetrofitNetworkClient(get(), androidContext())
    }

    single {
        MovieCastConverter()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}