package presentation.movies

import SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.api.MoviesInteractor
import domain.models.Movie
import ui.movies.models.MoviesState
import utils.debounce

class MoviesSearchViewModel(
    private val moviesInteractor: MoviesInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

//    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeToastState(): SingleLiveEvent<String?> = showToast

    private var latestSearchText: String? = null

    private val movies = ArrayList<Movie>()

//    private val searchRunnable = Runnable {
//        val newSearchText = latestSearchText ?: ""
//        searchRequest(newSearchText)
//    }

//    override fun onCleared() {
//        handler.removeCallbacks(searchRunnable)
//    }

    private val movieSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        newSearchText -> searchRequest(newSearchText) }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        movieSearchDebounce(changedText)
        }
//        handler.removeCallbacks(searchRunnable)
//        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)


    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(MoviesState.Loading)

            moviesInteractor.findMovies(newSearchText, object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {

                    if (foundMovies != null) {
                        movies.addAll(foundMovies)
                    }
                    when {
                        errorMessage != null -> {
                            renderState(
                                MoviesState.Error
                            )
                            showToast.postValue(errorMessage)
                        }

                        movies.isEmpty() -> {
                            renderState(
                                MoviesState.Empty
                            )
                        }

                        else -> {
                            renderState(
                                MoviesState.Content(
                                    movies = movies
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }
}