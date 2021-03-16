package com.codeliner.achacha.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codeliner.achacha.data.accounts.Account
import com.codeliner.achacha.data.accounts.AccountDatabaseDao
import com.codeliner.achacha.data.patterns.Pattern
import com.codeliner.achacha.data.patterns.PatternDatabaseDao
import com.codeliner.achacha.data.todos.Todo
import com.codeliner.achacha.data.todos.TodoDatabaseDao

@Database(entities = [Todo::class, Account::class, Pattern::class], version = 7, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract val todoDatabaseDao: TodoDatabaseDao
    abstract val accountDatabaseDao: AccountDatabaseDao
    abstract val patternDatabaseDao: PatternDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "achacha_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}
