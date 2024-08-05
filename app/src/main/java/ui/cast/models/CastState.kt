package ui.cast.models

import core.ui.RVItem

sealed interface CastState {

    object Loading : CastState

    data class Content(
        val fullTitle: String,
        val items: List<RVItem>,
    ) : CastState

    data class Error(val message: String
    ) : CastState
}