package com.codeliner.achacha.data.patterns

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PatternDatabaseDao {

    @Insert
    fun createPattern(pattern: Pattern)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePattern(pattern: Pattern)

    @Query("SELECT * FROM pattern_table LIMIT 1")
    fun readStoredPattern(): LiveData<Pattern?>

    @Query("DELETE FROM pattern_table")
    fun clearPattern()
}