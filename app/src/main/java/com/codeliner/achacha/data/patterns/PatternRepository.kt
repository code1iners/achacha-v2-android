package com.codeliner.achacha.data.patterns

import com.codeliner.achacha.data.AppDatabase

class PatternRepository(
        private val database: AppDatabase
) {
    fun createPattern(pattern: Pattern) = database.patternDatabaseDao.createPattern(pattern)
    fun updatePattern(pattern: Pattern) = database.patternDatabaseDao.updatePattern(pattern)
    fun readStoredPattern() = database.patternDatabaseDao.readStoredPattern()
    fun clearPattern() = database.patternDatabaseDao.clearPattern()
}