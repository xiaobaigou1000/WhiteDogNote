package ink.xiaobaigou.whitedognote.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ink.xiaobaigou.whitedognote.database.dao.NoteDao
import ink.xiaobaigou.whitedognote.database.dao.UserDao
import ink.xiaobaigou.whitedognote.database.entity.Note
import ink.xiaobaigou.whitedognote.database.entity.User

@Database(entities = [User::class, Note::class],version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao

    companion object{
        private var INSTANCE:AppDatabase?=null

        fun getDatabase(context: Context):AppDatabase{
            return INSTANCE?: synchronized(this){
                val instance= Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"notebook_database").build()
                INSTANCE=instance
                instance
            }
        }
    }
}