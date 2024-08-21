package domain.impl

import domain.api.MoviesInteractor
import domain.api.MoviesRepository
import domain.models.Movie
import domain.models.MovieCast
import domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import utils.Resource
import java.util.concurrent.Executors

class MoviesInteractorImpl (private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun findMovies(expression: String): Flow<Pair<List<Movie>?, String?>> {
        return repository.findMovies(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getMovieDetails(movieId: String): Flow<Pair<MovieDetails?, String?>> {
        return repository.getMovieDetails(movieId).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, result.message)
                }

                is Resource.Error -> {
                    Pair(result.data, result.message)
                }
            }
        }
    }

    override fun getFullCast(movieId: String): Flow<Pair<MovieCast?, String?>> {
        return repository.getFullCast(movieId).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, result.message)
                }

                is Resource.Error -> {
                    Pair(result.data, result.message)
                }
            }
        }
    }
}