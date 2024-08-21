package domain.impl

import domain.db.HistoryInteractor
import domain.db.HistoryRepository
import domain.models.Movie
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(private val historyRepository: HistoryRepository) : HistoryInteractor {
    override fun historyMovies(): Flow<List<Movie>> {
        return historyRepository.historyMovies()
    }
}