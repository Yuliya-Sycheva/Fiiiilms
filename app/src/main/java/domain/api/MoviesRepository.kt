package domain.api

import domain.models.Movie
import domain.models.MovieCast
import domain.models.MovieDetails
import utils.Resource

interface MoviesRepository {
    fun findMovies(expression: String): Resource<List<Movie>>
    fun getMovieDetails(movieId : String) : Resource<MovieDetails>
    fun getFullCast(movieId : String) : Resource<MovieCast>
}