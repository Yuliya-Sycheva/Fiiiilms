package domain.db

import domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface HistoryInteractor {
    fun historyMovies(): Flow<List<Movie>>
}