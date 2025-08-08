// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/transactions/TransactionsViewModel.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.auric.fintrack.auricfin.data.CategoryRepository
import com.rahul.auric.fintrack.auricfin.data.TransactionRepository
import com.rahul.auric.fintrack.auricfin.data.local.Category
import com.rahul.auric.fintrack.auricfin.data.local.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TransactionTypeFilter { ALL, INCOME, EXPENSE }

// --- FIX: Add an enum for sorting ---
enum class SortOrder { NEWEST_FIRST, OLDEST_FIRST }

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _transactionTypeFilter = MutableStateFlow(TransactionTypeFilter.ALL)
    val transactionTypeFilter: StateFlow<TransactionTypeFilter> = _transactionTypeFilter

    private val _categoryFilter = MutableStateFlow("All")
    val categoryFilter: StateFlow<String> = _categoryFilter

    // --- FIX: Add state for sorting ---
    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST_FIRST)
    val sortOrder: StateFlow<SortOrder> = _sortOrder

    val allCategories: StateFlow<List<Category>> = categoryRepository.allCategories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- FIX: Update combine to include sorting logic ---
    val transactions: StateFlow<List<Transaction>> =
        combine(
            transactionRepository.allTransactions,
            _transactionTypeFilter,
            _categoryFilter,
            _sortOrder // Listen to sort order changes
        ) { transactions, typeFilter, categoryFilter, sortOrder ->
            val filteredList = transactions.filter { transaction ->
                val typeMatch = when (typeFilter) {
                    TransactionTypeFilter.ALL -> true
                    TransactionTypeFilter.INCOME -> transaction.type == "income"
                    TransactionTypeFilter.EXPENSE -> transaction.type == "expense"
                }
                val categoryMatch =
                    if (categoryFilter == "All") true else transaction.category == categoryFilter
                typeMatch && categoryMatch
            }

            // Apply sorting to the already filtered list
            when (sortOrder) {
                SortOrder.NEWEST_FIRST -> filteredList.sortedByDescending { it.date }
                SortOrder.OLDEST_FIRST -> filteredList.sortedBy { it.date }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setTransactionTypeFilter(filter: TransactionTypeFilter) {
        _transactionTypeFilter.value = filter
    }

    fun setCategoryFilter(category: String) {
        _categoryFilter.value = category
    }

    // --- FIX: Add a function to toggle the sort order ---
    fun toggleSortOrder() {
        _sortOrder.value = if (_sortOrder.value == SortOrder.NEWEST_FIRST) {
            SortOrder.OLDEST_FIRST
        } else {
            SortOrder.NEWEST_FIRST
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch { transactionRepository.deleteTransaction(transaction) }
    }
}