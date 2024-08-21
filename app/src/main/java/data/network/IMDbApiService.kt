package data.network

import data.dto.MovieCastResponse
import data.dto.MovieDetailsResponse
import data.dto.NamesSearchResponse
import data.dto.MoviesSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call

interface IMDbApiService {
    @GET("/en/API/SearchMovie/k_zcuw1ytf/{expression}")
    suspend fun findMovies(@Path("expression") expression: String): MoviesSearchResponse

    @GET("/en/API/Title/k_zcuw1ytf/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: String): MovieDetailsResponse

    @GET("/en/API/FullCast/k_zcuw1ytf/{movie_id}")
    suspend fun getFullCast(@Path("movie_id") movieId: String): MovieCastResponse

    @GET("/en/API/SearchName/k_zcuw1ytf/{expression}")
    suspend fun getNames(@Path("expression") expression: String): NamesSearchResponse

}