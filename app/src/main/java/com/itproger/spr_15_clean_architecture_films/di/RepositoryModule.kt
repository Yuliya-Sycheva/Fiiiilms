package com.itproger.spr_15_clean_architecture_films.di

import data.HistoryRepositoryImpl
import data.MoviesRepositoryImpl
import data.NamesRepositoryImpl
import data.converters.MovieDbConvertor
import domain.api.MoviesRepository
import domain.api.NamesRepository
import domain.db.HistoryRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<MoviesRepository> {
        MoviesRepositoryImpl(get(), get(), get(), get())
    }

    single<NamesRepository> {
        NamesRepositoryImpl(get())
    }

    factory { MovieDbConvertor() }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }
}