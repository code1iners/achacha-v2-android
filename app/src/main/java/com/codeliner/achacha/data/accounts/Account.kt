package com.codeliner.achacha.data.accounts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts_table")
data class Account (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "created")
    var created: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "subtitle")
    var subtitle: String? = null,

    @ColumnInfo(name = "username")
    var username: String? = null,

    @ColumnInfo(name = "password")
    var password: String? = null,

    @ColumnInfo(name = "hint")
    var hint: String? = null,

    @ColumnInfo(name = "thumbnail")
    var thumbnail: String? = null
)