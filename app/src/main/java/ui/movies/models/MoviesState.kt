package ui.movies.models

import domain.models.Movie

sealed interface MoviesState {
    object Loading : MoviesState

    data class Content(
        val movies: List<Movie>
    ): MoviesState

    object Error : MoviesState

    object Empty : MoviesState
}
