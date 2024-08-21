package data

import data.converters.MovieCastConverter
import data.converters.MovieDbConvertor
import data.db.AppDatabase
import data.dto.MovieCastRequest
import data.dto.MovieCastResponse
import data.dto.MovieDetailsRequest
import data.dto.MovieDetailsResponse
import data.dto.MovieDto
import data.dto.MoviesSearchRequest
import data.dto.MoviesSearchResponse
import domain.api.MoviesRepository
import domain.models.Movie
import domain.models.MovieCast
import domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import utils.Resource

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val movieCastConverter: MovieCastConverter,
    // новые зависимости
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: MovieDbConvertor
) : MoviesRepository {

    override fun findMovies(expression: String): Flow<Resource<List<Movie>>> = flow {

        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as MoviesSearchResponse) {
                    val data = results.map {
                        Movie(it.id, it.resultType, it.image, it.title, it.description)
                    }
                        //сохраняем список фильмов в базу данных
                        saveMovie(results)
                        emit(Resource.Success(data))
                    }
                }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun getMovieDetails(movieId: String): Flow<Resource<MovieDetails>> = flow {
        val response = networkClient.doRequest(MovieDetailsRequest(movieId))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                emit(with(response as MovieDetailsResponse) {
                    Resource.Success(
                        MovieDetails(
                            id, title, imDbRating, year,
                            countries, genres, directors, writers, stars, plot
                        )
                    )
                })
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun getFullCast(movieId: String): Flow<Resource<MovieCast>> = flow {
        val response = networkClient.doRequest(MovieCastRequest(movieId))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                emit(Resource.Success(data = movieCastConverter.convert(response as MovieCastResponse)))
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    // маппим данные из сетевой модели в модель базы данных и сохраняем
    private suspend fun saveMovie(movies: List<MovieDto>) {
        val movieEntities = movies.map { movie -> movieDbConvertor.map(movie) }
        appDatabase.movieDao().insertMovies(movieEntities)
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