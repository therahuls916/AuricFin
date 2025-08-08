// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/data/local/TransactionDao.kt
package com.rahul.auric.fintrack.auricfin.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    // --- Insert/Update Operations ---

    // Inserts a new transaction. If a transaction with the same ID already exists, it will be replaced.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    // Updates an existing transaction.
    @Update
    suspend fun updateTransaction(transaction: Transaction)


    // --- Delete Operations ---

    // Deletes a specific transaction.
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)


    // --- Query Operations ---

    // Gets all transactions from the table, ordered by the most recent date first.
    // Flow is a modern data type that allows our UI to automatically update when the data changes.
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    // Gets a single transaction by its ID.
    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionById(id: Int): Flow<Transaction?> // The '?' means it can be null if not found
}