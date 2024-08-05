package com.itproger.spr_15_clean_architecture_films.di

import domain.api.MoviesInteractor
import domain.api.NamesInteractor
import domain.impl.MoviesInteractorImpl
import domain.impl.NamesInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<MoviesInteractor> {
        MoviesInteractorImpl(get())
    }

    single<NamesInteractor> {
        NamesInteractorImpl(get())
    }

}