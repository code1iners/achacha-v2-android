package com.codeliner.achacha.data.todos

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codeliner.achacha.utils.Const
import kotlinx.android.parcel.Parcelize

@Parcelize
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

        @ColumnInfo(name = "tags")
        var tags: String? = null,

        @ColumnInfo(name = "memo")
        var memo: String? = null,

        @ColumnInfo(name = "is_finished")
        var isFinished: Boolean = false,

        @ColumnInfo(name = "is_important")
        var isImportant: Boolean = false
): Parcelable
