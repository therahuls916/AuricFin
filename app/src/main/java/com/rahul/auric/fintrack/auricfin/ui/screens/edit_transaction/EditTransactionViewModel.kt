// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/edit_transaction/EditTransactionViewModel.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.edit_transaction

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class EditTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    // SavedStateHandle allows us to get navigation arguments like the transactionId
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // --- State for the input fields ---
    private val _transactionState = MutableStateFlow<Transaction?>(null)
    val transactionState: StateFlow<Transaction?> = _transactionState.asStateFlow()

    // --- We get the transactionId from the navigation arguments ---
    private val transactionId: Int = checkNotNull(savedStateHandle["transactionId"])

    // --- State for categories (same as in AddTransactionViewModel) ---
    val expenseCategories: StateFlow<List<Category>> =
        categoryRepository.getCategoriesByType("expense")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val incomeCategories: StateFlow<List<Category>> =
        categoryRepository.getCategoriesByType("income")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        // Load the transaction as soon as the ViewModel is created
        loadTransaction()
    }

    private fun loadTransaction() {
        viewModelScope.launch {
            // .first() gets the first value emitted by the Flow
            val transaction = transactionRepository.getTransactionById(transactionId).first()
            _transactionState.value = transaction
        }
    }

    // --- Function to save the updated transaction ---
    fun updateTransaction(
        amount: Double,
        type: String,
        category: String,
        date: Date,
        note: String?
    ) {
        viewModelScope.launch {
            val updatedTransaction = Transaction(
                id = transactionId, // CRITICAL: Use the original ID
                amount = if (type == "expense") -abs(amount) else abs(amount),
                type = type,
                category = category,
                date = date,
                note = note
            )
            transactionRepository.updateTransaction(updatedTransaction)
        }
    }
}