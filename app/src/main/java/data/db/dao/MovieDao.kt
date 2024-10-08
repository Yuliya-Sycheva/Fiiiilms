package data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import data.db.MovieEntity
import androidx.room.OnConflictStrategy

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movie_table")
    suspend fun getMovies(): List<MovieEntity>
}