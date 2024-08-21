package domain.db

import domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun historyMovies(): Flow<List<Movie>>
}