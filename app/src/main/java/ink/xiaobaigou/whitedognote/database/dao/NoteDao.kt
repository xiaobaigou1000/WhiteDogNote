package ink.xiaobaigou.whitedognote.database.dao

import androidx.room.*
import ink.xiaobaigou.whitedognote.database.entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(vararg note: Note):List<Long>

    @Delete
    suspend fun delete(vararg note: Note)

    @Delete
    suspend fun delete(noteList:List<Note>)

    @Query("SELECT * FROM Note")
    fun queryAll(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE id=:id")
    suspend fun queryById(id: Long): Note?

    @Query("SELECT * FROM Note WHERE title=:title")
    suspend fun queryByTitle(title: String): Note?

    @Query("select * from Note where author=:userId")
    fun queryUserNoteFlow(userId: Long): Flow<List<Note>>

    @Update
    suspend fun updateNote(note:Note)
}