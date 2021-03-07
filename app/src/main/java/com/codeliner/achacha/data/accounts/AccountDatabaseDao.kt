package com.codeliner.achacha.data.accounts

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AccountDatabaseDao {

    // note. Create
    @Insert
    fun createAccount(account: Account)

    // note. Read
    @Query("SELECT * FROM accounts_table ORDER BY id DESC")
    fun readAllOrderedById(): LiveData<List<Account>>

    @Query("SELECT * FROM accounts_table WHERE id = :accountId")
    fun readAccountById(accountId: Long): Account

    @Query("SELECT * FROM accounts_table ORDER BY id DESC LIMIT 1")
    fun readAccountLatest(): LiveData<Account>?

    // note. Update
    @Update
    fun updateAccount(account: Account)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAccounts(accounts: List<Account>)

    // note. Delete
    @Query("DELETE FROM accounts_table")
    fun clear()

    @Query("DELETE FROM accounts_table WHERE id = :accountId")
    fun deleteAccountById(accountId: Long)
}