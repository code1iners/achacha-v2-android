package com.codeliner.achacha.data.todos

import com.codeliner.achacha.data.AppDatabase

class TodoRepository(
    private val database: AppDatabase
) {

    fun create(todo: Todo) = database.todoDatabaseDao.createTodo(todo)

    fun readAllOrderedById() = database.todoDatabaseDao.readAllOrderedById()

    fun readTodoById(todo: Todo) = database.todoDatabaseDao.readTodoById(todo.id)

    fun updateTodo(todo: Todo) = database.todoDatabaseDao.updateTodo(todo)

    fun updateTodos(todos: List<Todo>) = database.todoDatabaseDao.updateTodos(todos)

    fun clear() = database.todoDatabaseDao.clear()

    fun deleteTodoById(todo: Todo) = database.todoDatabaseDao.deleteTodoById(todo.id)
}