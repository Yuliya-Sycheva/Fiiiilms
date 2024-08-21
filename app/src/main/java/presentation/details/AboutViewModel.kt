package presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.api.MoviesInteractor
import domain.models.MovieDetails
import kotlinx.coroutines.launch
import ui.details.models.AboutState

class AboutViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {

    private val aboutLiveData = MutableLiveData<AboutState>()
    fun observeAbout(): LiveData<AboutState> = aboutLiveData

    init {
        viewModelScope.launch {
            moviesInteractor.getMovieDetails(movieId).collect { pair ->
                processResult(pair.first, pair.second)
            }
        }
    }

    private fun processResult(movieDetails: MovieDetails?, errorMessage: String?) {
        if (errorMessage != null) {
            renderState(AboutState.Error(errorMessage ?: "Unknown error"))
        } else {
            renderState(AboutState.Content(movieDetails))
        }
    }

    private fun renderState(state: AboutState) {
        aboutLiveData.postValue(state)
    }
}

