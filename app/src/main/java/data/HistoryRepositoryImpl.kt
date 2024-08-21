package data

import data.converters.MovieDbConvertor
import data.db.AppDatabase
import data.db.MovieEntity
import domain.db.HistoryRepository
import domain.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: MovieDbConvertor,
) : HistoryRepository {
    override fun historyMovies(): Flow<List<Movie>> = flow {
        val movies = appDatabase.movieDao().getMovies()
        emit(convertFromMovieEntity(movies))
    }

    private fun convertFromMovieEntity(movies: List<MovieEntity>): List<Movie> {
        return movies.map { movie ->
            movieDbConvertor.map(movie)
        }
    }
}