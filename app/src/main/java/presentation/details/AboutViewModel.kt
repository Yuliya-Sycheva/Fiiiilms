package presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import domain.api.MoviesInteractor
import domain.models.MovieDetails
import ui.details.models.AboutState

class AboutViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {

    private val aboutLiveData = MutableLiveData<AboutState>()
    fun observeAbout(): LiveData<AboutState> = aboutLiveData

    init {
        moviesInteractor.getMovieDetails(movieId, object : MoviesInteractor.MovieDetailsConsumer {
            override fun consume(movieDetails: MovieDetails?, errorMessage: String?) {
                if (errorMessage != null) {
                    renderState(AboutState.Error(errorMessage?: "Unknown error"))
                } else {
                    renderState(AboutState.Content(movieDetails))
                }
            }
        })
    }

    private fun renderState(state: AboutState) {
        aboutLiveData.postValue(state)
    }

}

