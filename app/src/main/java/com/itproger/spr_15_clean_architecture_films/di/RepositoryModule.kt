package com.itproger.spr_15_clean_architecture_films.di

import data.MoviesRepositoryImpl
import data.NamesRepositoryImpl
import domain.api.MoviesRepository
import domain.api.NamesRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<MoviesRepository> {
        MoviesRepositoryImpl(get(), get())
    }

    single<NamesRepository> {
        NamesRepositoryImpl(get())
    }
}