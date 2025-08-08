// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/add_transaction/AddTransactionViewModel.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.add_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.auric.fintrack.auricfin.data.CategoryRepository // <-- FIX: Import original name
import com.rahul.auric.fintrack.auricfin.data.TransactionRepository
import com.rahul.auric.fintrack.auricfin.data.local.Category
import com.rahul.auric.fintrack.auricfin.data.local.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository // <-- FIX: Use original name
) : ViewModel() {

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

    fun addTransaction(
        amount: Double,
        type: String,
        category: String,
        date: Date,
        note: String?
    ) {
        viewModelScope.launch {
            val transaction = Transaction(
                amount = if (type == "expense") -amount else amount,
                type = type,
                category = category,
                date = date,
                note = note
            )
            transactionRepository.insertTransaction(transaction)
        }
    }
}