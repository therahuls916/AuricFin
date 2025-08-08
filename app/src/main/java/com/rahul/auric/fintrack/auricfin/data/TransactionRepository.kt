// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/data/TransactionRepository.kt
package com.rahul.auric.fintrack.auricfin.data

import com.rahul.auric.fintrack.auricfin.data.local.Transaction
import com.rahul.auric.fintrack.auricfin.data.local.TransactionDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    fun getTransactionById(id: Int): Flow<Transaction?> {
        return transactionDao.getTransactionById(id)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }
}