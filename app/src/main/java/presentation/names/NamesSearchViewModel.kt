package presentation.names

import SingleLiveEvent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.api.MoviesInteractor
import domain.api.NamesInteractor
import domain.models.Person
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import presentation.movies.MoviesSearchViewModel
import ui.names.models.NamesState

class NamesSearchViewModel(private val namesInteractor: NamesInteractor) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<NamesState>()
    fun observeState(): LiveData<NamesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeToastState(): SingleLiveEvent<String?> = showToast

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(NamesState.Loading)

            viewModelScope.launch {
                namesInteractor.getNames(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundNames: List<Person>?, errorMessage: String?) {

        val persons = mutableListOf<Person>()

        if (foundNames != null) {
            persons.addAll(foundNames)
        }
        when {
            errorMessage != null -> {
                renderState(
                    NamesState.Error
                )
                showToast.postValue(errorMessage)
            }

            persons.isEmpty() -> {
                renderState(
                    NamesState.Empty
                )
            }

            else -> {
                renderState(
                    NamesState.Content(
                        persons = persons
                    )
                )
            }
        }
    }


    private fun renderState(state: NamesState) {
        stateLiveData.postValue(state)
    }
}
