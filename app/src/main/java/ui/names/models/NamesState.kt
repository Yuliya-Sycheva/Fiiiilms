package ui.names.models

import domain.models.Movie
import domain.models.Person
import ui.movies.models.MoviesState

sealed interface NamesState {
    object Loading : NamesState

    data class Content(
        val persons: List<Person>
    ): NamesState

    object Error : NamesState

    object Empty : NamesState
}