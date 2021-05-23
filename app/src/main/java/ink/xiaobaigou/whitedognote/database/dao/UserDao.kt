package ink.xiaobaigou.whitedognote.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ink.xiaobaigou.whitedognote.database.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insert(vararg user: User): List<Long>

    @Delete
    suspend fun delete(vararg user: User)

    @Query("SELECT * FROM User")
    fun queryAll(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE id=:id")
    suspend fun queryById(id: Long): User?

    @Query("SELECT * FROM User WHERE userName=:name")
    suspend fun queryByName(name: String): User?
}