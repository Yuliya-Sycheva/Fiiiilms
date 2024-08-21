package presentation.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.api.MoviesInteractor
import domain.models.MovieCast
import kotlinx.coroutines.launch
import ui.cast.models.CastState

class MoviesCastViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<CastState>()
    fun observeState(): LiveData<CastState> = stateLiveData

    init {

        stateLiveData.postValue(CastState.Loading)

        viewModelScope.launch {
            moviesInteractor.getFullCast(movieId).collect { pair ->
                processResult(pair.first, pair.second)
            }
        }
    }

    private fun processResult(movieCast: MovieCast?, errorMessage: String?) {
        if (movieCast != null) {
            renderState(castToUiStateContent(movieCast))
        } else {
            renderState(CastState.Error(errorMessage ?: "Unknown error"))
        }
    }

    private fun renderState(state: CastState) {
        stateLiveData.postValue(state)
    }

    private fun castToUiStateContent(cast: MovieCast): CastState {
// Строим список элементов RecyclerView
        val items = buildList<MoviesCastRVItem> {
            // Если есть хотя бы один режиссёр, добавим заголовок
            if (cast.directors.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Directors")
                this += cast.directors.map { MoviesCastRVItem.PersonItem(it) }
            }

            // Если есть хотя бы один сценарист, добавим заголовок
            if (cast.writers.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Writers")
                this += cast.writers.map { MoviesCastRVItem.PersonItem(it) }
            }

            // Если есть хотя бы один актёр, добавим заголовок
            if (cast.actors.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Actors")
                this += cast.actors.map { MoviesCastRVItem.PersonItem(it) }
            }

            // Если есть хотя бы один дополнительный участник, добавим заголовок
            if (cast.others.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Others")
                this += cast.others.map { MoviesCastRVItem.PersonItem(it) }
            }
        }
        return CastState.Content(
            fullTitle = cast.fullTitle,
            items = items
        )

    }
}

/*
Вот как работает buildList на примере вашего кода:
1 Создание пустого изменяемого списка.
2 Добавление элементов в список внутри лямбда-выражения.
3 Возвращение неизменяемого списка с добавленными элементами.

В этом примере buildList создаёт новый список MoviesCastRVItem. Внутри лямбда-выражения:
Проверяется, есть ли хотя бы один режиссёр. Если есть, добавляется заголовок и соответствующие элементы.
и т.д.
В итоге создаётся список items, который возвращается в виде CastState.Content
 */