package com.codeliner.achacha.domains.todos

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDatabaseDao {

    // note. Create
    @Insert
    fun insert(todo: Todo)

    // note. Read
    @Query("SELECT * FROM todos_table ORDER BY id DESC")
    fun getAllOrderedById(): LiveData<List<Todo>>

    @Query("SELECT * FROM todos_table ORDER BY position")
    fun getAllOrderedByPosition(): LiveData<List<Todo>>

    @Query("SELECT * FROM todos_table WHERE id = :todoId")
    fun getTodoById(todoId: Long): Todo

    @Query("SELECT * FROM todos_table ORDER BY id DESC LIMIT 1")
    fun getTodoLatest(): LiveData<Todo>?

    // note. Update
    @Update
    fun update(todo: Todo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(todos: List<Todo>)

    // note. Delete
    @Query("DELETE FROM todos_table")
    fun clear()

    @Query("DELETE FROM todos_table WHERE id = :todoId")
    fun deleteTodoById(todoId: Long)
}