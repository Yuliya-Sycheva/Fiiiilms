package com.itproger.spr_15_clean_architecture_films.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import presentation.cast.MoviesCastViewModel
import presentation.movies.MoviesSearchViewModel
import presentation.details.AboutViewModel
import presentation.details.PosterViewModel
import presentation.history.HistoryViewModel
import presentation.names.NamesSearchViewModel

val viewModelModule = module {

    viewModel {
        MoviesSearchViewModel(get())
    }

    viewModel { (posterUrl: String) ->
        PosterViewModel(posterUrl)
    }

    viewModel {(movieId: String) ->
        AboutViewModel(movieId, get())
    }

    viewModel {(movieId: String) ->
        MoviesCastViewModel(movieId, get())
    }

    viewModel {
        NamesSearchViewModel(get())
    }

    viewModel {
        HistoryViewModel(androidContext(), get())
    }
}