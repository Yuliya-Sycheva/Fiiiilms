package data.converters

import data.db.MovieEntity
import data.dto.MovieDto
import domain.models.Movie

class MovieDbConvertor {

    fun map(movie: MovieDto): MovieEntity {
        return MovieEntity(movie.id, movie.resultType, movie.image, movie.title, movie.description)
    }

    fun map(movie: MovieEntity): Movie {
        return Movie(movie.id, movie.resultType, movie.image, movie.title, movie.description)
    }
}