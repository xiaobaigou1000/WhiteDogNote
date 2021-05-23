package ink.xiaobaigou.whitedognote.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ink.xiaobaigou.whitedognote.database.AppDatabase
import ink.xiaobaigou.whitedognote.database.entity.Note
import ink.xiaobaigou.whitedognote.database.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val database = AppDatabase.getDatabase(application)
    private val userDao = database.userDao()
    private val noteDao = database.noteDao()

    var requestLogin = true

    suspend fun queryUserByName(name: String): User? {
        return userDao.queryByName(name)
    }

    var currentUser: User?=null

    fun getNoteListFlow(user: User): Flow<List<Note>> {
        return noteDao.queryUserNoteFlow(user.id)
    }

    private val _currentNote = MutableLiveData<Note?>()
    val currentNote: LiveData<Note?> get() = _currentNote

    fun updateCurrentNote(note: Note?) {
        _currentNote.value = note
    }

    suspend fun createNote(note: Note): Note {
        val result = noteDao.insert(note)
        return noteDao.queryById(result.first())!!
    }

    fun deleteCurrentNote() {
        viewModelScope.launch {
            currentNote.value?.let {
                noteDao.delete(it)
                _currentNote.value = null
            }
        }
    }

    suspend fun deleteFromDatabase(note: Note) {
        noteDao.delete(note)
    }

    fun deleteNotesFromDatabase(noteList: List<Note>) {
        viewModelScope.launch {
            noteDao.delete(noteList)
        }
    }

    fun updateNoteInDatabase(note: Note) {
        viewModelScope.launch {
            noteDao.updateNote(note)
        }
    }

    suspend fun register(user: User): User? {
        val existedUser = userDao.queryByName(user.username)
        existedUser?.let { return null }
        val result = userDao.insert(user)
        val id = result.firstOrNull()
        return id?.let { userDao.queryById(id) }
    }
}