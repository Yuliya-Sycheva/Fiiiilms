package com.itproger.spr_15_clean_architecture_films

import android.app.Application
import com.itproger.spr_15_clean_architecture_films.di.dataModule
import com.itproger.spr_15_clean_architecture_films.di.interactorModule
import com.itproger.spr_15_clean_architecture_films.di.repositoryModule
import com.itproger.spr_15_clean_architecture_films.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MoviesApplication : Application() {

    //   var moviesSearchPresenter : MoviesSearchViewModel? = null

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MoviesApplication)
            modules(
                dataModule,
                repositoryModule,
                interactorModule,
                viewModelModule
            )
        }
    }

}