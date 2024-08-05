package domain.impl

import domain.api.MoviesInteractor
import domain.api.MoviesRepository
import utils.Resource
import java.util.concurrent.Executors

class MoviesInteractorImpl (private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun findMovies(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
        executor.execute {
            when (val resource = repository.findMovies(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun getMovieDetails(movieId: String, consumer: MoviesInteractor.MovieDetailsConsumer) {
        executor.execute {
            when (val resource = repository.getMovieDetails(movieId)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, resource.message)
                }

                is Resource.Error -> {
                    consumer.consume(resource.data, resource.message)
                }
            }
        }
    }

    override fun getFullCast(movieId: String, consumer: MoviesInteractor.MovieCastConsumer) {
        executor.execute {
            when (val resource = repository.getFullCast(movieId)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, resource.message)
                }

                is Resource.Error -> {
                    consumer.consume(resource.data, resource.message)
                }
            }
        }
    }
}