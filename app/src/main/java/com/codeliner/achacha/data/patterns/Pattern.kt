package com.codeliner.achacha.data.patterns

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pattern_table")
data class Pattern(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo(name = "stored_pattern")
        var storedPattern: String? = null
)