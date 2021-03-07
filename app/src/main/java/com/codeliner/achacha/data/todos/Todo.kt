package com.codeliner.achacha.data.todos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codeliner.achacha.utils.Const

@Entity(tableName = "todos_table")
data class Todo (
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo(name = "created")
        var created: Long = System.currentTimeMillis(),

        @ColumnInfo(name = "icon")
        var icon: String = Const.WORK,

        @ColumnInfo(name = "work")
        var work: String? = null,

        // note. need rename help to tags later
        @ColumnInfo(name = "help")
        var help: String? = null,

        @ColumnInfo(name = "is_finished")
        var isFinished: Boolean = false,

        @ColumnInfo(name = "is_important")
        var isImportant: Boolean = false
)
