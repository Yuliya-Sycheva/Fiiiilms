package ui.details.models

import domain.models.MovieDetails

sealed interface AboutState {

    data class Content(
        val movie: MovieDetails?
    ) : AboutState

    data class Error(val message: String
    ) : AboutState
}