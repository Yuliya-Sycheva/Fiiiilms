package presentation.movies

import domain.models.Movie
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ui.movies.models.MoviesState

interface MoviesView : MvpView {
    // Методы, меняющие внешний вид экрана

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: MoviesState)

    // Методы «одноразовых событий»

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(additionalMessage: String)

}
