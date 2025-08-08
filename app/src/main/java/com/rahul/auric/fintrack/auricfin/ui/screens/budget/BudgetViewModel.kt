// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/budget/BudgetViewModel.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.auric.fintrack.auricfin.data.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.abs

// A simple data class to hold the calculated progress for the UI
data class BudgetProgress(
    val category: String,
    val spent: Double,
    val total: Double
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    // This flow will calculate and provide the spending breakdown to the UI
    val budgetProgress: StateFlow<List<BudgetProgress>> = transactionRepository.allTransactions
        .map { transactions ->
            // 1. Filter for expenses in the current month
            val monthlyExpenses =
                transactions.filter { it.type == "expense" && isCurrentMonth(it.date) }

            // 2. Calculate the total spending for the month
            val totalMonthlySpending = monthlyExpenses.sumOf { abs(it.amount) }

            if (totalMonthlySpending == 0.0) {
                emptyList() // If no spending, return an empty list
            } else {
                // 3. Group expenses by category and map them to our UI data class
                monthlyExpenses
                    .groupBy { it.category }
                    .map { (category, transactionsInCategory) ->
                        BudgetProgress(
                            category = category,
                            spent = transactionsInCategory.sumOf { abs(it.amount) },
                            total = totalMonthlySpending // The "total" is the overall monthly spending
                        )
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

// Helper function to check if a date is in the current month and year
private fun isCurrentMonth(date: java.util.Date): Boolean {
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    val transactionCalendar = Calendar.getInstance()
    transactionCalendar.time = date
    val transactionMonth = transactionCalendar.get(Calendar.MONTH)
    val transactionYear = transactionCalendar.get(Calendar.YEAR)

    return transactionMonth == currentMonth && transactionYear == currentYear
}