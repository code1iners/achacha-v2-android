package com.codeliner.achacha.data.todos

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDatabaseDao {

    // note. Create
    @Insert
    fun createTodo(todo: Todo)

    // note. Read
    @Query("SELECT * FROM todos_table ORDER BY id DESC")
    fun readAllOrderedById(): LiveData<List<Todo>>

    @Query("SELECT * FROM todos_table WHERE id = :todoId")
    fun readTodoById(todoId: Long): LiveData<Todo>

    @Query("SELECT * FROM todos_table ORDER BY id DESC LIMIT 1")
    fun readTodoLatest(): LiveData<Todo>?

    // note. Update
    @Update
    fun updateTodo(todo: Todo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTodos(todos: List<Todo>)

    // note. Delete
    @Query("DELETE FROM todos_table")
    fun clear()

    @Query("DELETE FROM todos_table WHERE id = :todoId")
    fun deleteTodoById(todoId: Long)
}