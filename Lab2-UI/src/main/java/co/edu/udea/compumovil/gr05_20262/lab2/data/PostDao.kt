package co.edu.udea.compumovil.gr05_20262.lab2.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id ASC")
    fun observeAll(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): PostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<PostEntity>)

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun count(): Int

    @Query("DELETE FROM posts")
    suspend fun clear()
}

@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
abstract class LabDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
