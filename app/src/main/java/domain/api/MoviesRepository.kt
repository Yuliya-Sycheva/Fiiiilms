package domain.api

import domain.models.Movie
import domain.models.MovieCast
import domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow
import utils.Resource

interface MoviesRepository {
    fun findMovies(expression: String): Flow<Resource<List<Movie>>>
    fun getMovieDetails(movieId : String) : Flow<Resource<MovieDetails>>
    fun getFullCast(movieId : String) : Flow<Resource<MovieCast>>
}