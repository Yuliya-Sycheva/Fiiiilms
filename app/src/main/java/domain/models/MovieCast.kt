package domain.models

data class MovieCast (
    val imDbId: String,
    val fullTitle: String,
    val directors: List<MovieCastPerson>,
    val writers: List<MovieCastPerson>,
    val actors: List<MovieCastPerson>,
    val others: List<MovieCastPerson>,
)