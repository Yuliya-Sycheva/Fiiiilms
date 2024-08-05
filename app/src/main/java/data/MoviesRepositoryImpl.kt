package data

import data.converters.MovieCastConverter
import data.dto.MovieCastRequest
import data.dto.MovieCastResponse
import data.dto.MovieDetailsRequest
import data.dto.MovieDetailsResponse
import data.dto.MoviesSearchRequest
import data.dto.MoviesSearchResponse
import domain.api.MoviesRepository
import domain.models.Movie
import domain.models.MovieCast
import domain.models.MovieDetails
import utils.Resource

class MoviesRepositoryImpl(private val networkClient: NetworkClient, private val movieCastConverter : MovieCastConverter) : MoviesRepository {

    override fun findMovies(expression: String): Resource<List<Movie>> {

        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success((response as MoviesSearchResponse).results.map {
                    Movie(it.id, it.resultType, it.image, it.title, it.description)
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun getMovieDetails(movieId: String): Resource<MovieDetails> {
        val response = networkClient.doRequest(MovieDetailsRequest(movieId))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                with(response as MovieDetailsResponse) {
                    Resource.Success(
                        MovieDetails(
                            id, title, imDbRating, year,
                            countries, genres, directors, writers, stars, plot
                        )
                    )
                }
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun getFullCast(movieId: String): Resource<MovieCast> {
        val response = networkClient.doRequest(MovieCastRequest(movieId))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> { Resource.Success(data = movieCastConverter.convert(response as MovieCastResponse))
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}

/*
Функция map используется для преобразования элементов коллекции. Она применяет заданную лямбду-функцию
к каждому элементу коллекции и возвращает новую коллекцию, содержащую результаты применения этой функции.
val numbers = listOf(1, 2, 3, 4, 5)
val squares = numbers.map { it * it }
println(squares)  // [1, 4, 9, 16, 25]

Функция flatMap сначала применяет указанную лямбду-функцию к каждому элементу коллекции (как map),
 но лямбда-функция должна возвращать коллекцию. Затем flatMap объединяет (сглаживает) все возвращенные
  коллекции в одну.
val numbers = listOf(1, 2, 3)
val result = numbers.flatMap { listOf(it, it * it) }
println(result)  // [1, 1, 2, 4, 3, 9]
 */