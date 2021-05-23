package ink.xiaobaigou.whitedognote.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(
        value = ["userName"],
        unique = true
    )]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "userName")
    val userName: String,
    @ColumnInfo(name = "passWord")
    val passWord: String,
)