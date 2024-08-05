package domain.api

import domain.models.Movie
import domain.models.MovieCast
import domain.models.MovieDetails
import domain.models.Person

interface MoviesInteractor {
    fun findMovies(expression: String, consumer: MoviesConsumer)

    fun getMovieDetails(movieId: String, consumer: MovieDetailsConsumer)

    fun getFullCast(movieId : String, consumer: MovieCastConsumer)

    interface MoviesConsumer {
        fun consume(foundMovies: List<Movie>?, errorMessage : String?)
    }

    interface MovieDetailsConsumer{
        fun consume(movieDetails: MovieDetails?, errorMessage: String?)
    }

    interface MovieCastConsumer{
        fun consume(movieCast: MovieCast?, errorMessage: String?)
    }
}