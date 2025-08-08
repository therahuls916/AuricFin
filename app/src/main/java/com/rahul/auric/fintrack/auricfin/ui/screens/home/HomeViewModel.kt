// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/home/HomeViewModel.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.auric.fintrack.auricfin.data.TransactionRepository
import com.rahul.auric.fintrack.auricfin.data.local.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.abs

// Helper data class for our pie chart data
data class PieChartData(val label: String, val value: Float)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    // --- StateFlows for transactions, balance, and expenses (no change here) ---
    val recentTransactions: StateFlow<List<Transaction>> = transactionRepository.allTransactions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalBalance: StateFlow<Double> = transactionRepository.allTransactions
        .map { transactions -> transactions.sumOf { it.amount } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val monthlyExpenses: StateFlow<Double> = transactionRepository.allTransactions
        .map { transactions ->
            transactions
                .filter { it.type == "expense" && isCurrentMonth(it.date) }
                .sumOf { it.amount }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    // --- NEW: StateFlow for Pie Chart Data ---
    val pieChartData: StateFlow<List<PieChartData>> = transactionRepository.allTransactions
        .map { transactions ->
            transactions
                .filter { it.type == "expense" && isCurrentMonth(it.date) }
                .groupBy { it.category } // Group transactions by their category name
                .map { (category, transactionsInCategory) ->
                    // For each group, create a PieChartData object
                    PieChartData(
                        label = category,
                        // Sum the amounts in the group and convert to a positive Float
                        value = transactionsInCategory.sumOf { abs(it.amount) }.toFloat()
                    )
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