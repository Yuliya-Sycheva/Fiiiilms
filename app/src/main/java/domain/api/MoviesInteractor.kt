package domain.api

import domain.models.Movie
import domain.models.MovieCast
import domain.models.MovieDetails
import domain.models.Person
import kotlinx.coroutines.flow.Flow

interface MoviesInteractor {
    fun findMovies(expression: String): Flow<Pair<List<Movie>?, String?>>

    fun getMovieDetails(movieId: String): Flow<Pair<MovieDetails?, String?>>

    fun getFullCast(movieId : String): Flow<Pair<MovieCast?, String?>>

//    interface MoviesConsumer {
//        fun consume(foundMovies: List<Movie>?, errorMessage : String?)
//    }

//    interface MovieDetailsConsumer{
//        fun consume(movieDetails: MovieDetails?, errorMessage: String?)
//    }

//    interface MovieCastConsumer{
//        fun consume(movieCast: MovieCast?, errorMessage: String?)
//    }
}