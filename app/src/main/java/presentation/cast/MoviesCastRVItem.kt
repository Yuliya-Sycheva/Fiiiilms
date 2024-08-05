package presentation.cast

import domain.models.MovieCastPerson
import core.ui.RVItem

sealed interface MoviesCastRVItem : RVItem {

    data class HeaderItem(
        val headerText: String,
    ) : MoviesCastRVItem

    data class PersonItem(
        val data: MovieCastPerson,
    ) : MoviesCastRVItem

}