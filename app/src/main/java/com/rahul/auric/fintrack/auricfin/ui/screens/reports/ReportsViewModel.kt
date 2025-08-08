// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/reports/ReportsViewModel.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.rahul.auric.fintrack.auricfin.data.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs

data class LineChartUiData(
    val entries: List<Entry> = emptyList(),
    val labels: List<String> = emptyList()
)

data class BarChartUiData(
    val entries: List<BarEntry> = emptyList(),
    val labels: List<String> = emptyList()
)

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val monthlyTransactions = transactionRepository.allTransactions.map { transactions ->
        transactions.filter { it.type == "expense" && isCurrentMonth(it.date) }
    }

    val totalMonthlyExpenses: StateFlow<Double> = monthlyTransactions
        .map { transactions -> transactions.sumOf { abs(it.amount) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val topCategory: StateFlow<String> = monthlyTransactions
        .map { transactions ->
            if (transactions.isEmpty()) "N/A"
            else transactions.groupBy { it.category }
                .maxByOrNull { entry -> entry.value.sumOf { abs(it.amount) } }
                ?.key ?: "N/A"
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "N/A"
        )

    val highestExpense: StateFlow<Double> = monthlyTransactions
        .map { transactions -> transactions.maxOfOrNull { abs(it.amount) } ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val lineChartData: StateFlow<LineChartUiData> = transactionRepository.allTransactions
        .map { transactions ->
            val monthlyExpenses = transactions
                .filter { it.type == "expense" && isWithinLastYear(it.date) }
                .groupBy { SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(it.date) }
                .mapValues { entry -> entry.value.sumOf { abs(it.amount) } }
                .toSortedMap()

            val entries = monthlyExpenses.values.mapIndexed { index, total ->
                Entry(
                    index.toFloat(),
                    total.toFloat()
                )
            }
            val labels = monthlyExpenses.keys.map { yearMonth ->
                val date = SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse(yearMonth)
                date?.let { SimpleDateFormat("MMM", Locale.getDefault()).format(it) } ?: ""
            }
            LineChartUiData(entries = entries, labels = labels)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LineChartUiData()
        )

    val barChartData: StateFlow<BarChartUiData> = monthlyTransactions
        .map { transactions ->
            if (transactions.isEmpty()) {
                BarChartUiData()
            } else {
                val spendingByCategory = transactions
                    .groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { abs(it.amount) } }
                    .entries
                    .sortedByDescending { it.value }

                val entries = spendingByCategory.mapIndexed { index, entry ->
                    BarEntry(index.toFloat(), entry.value.toFloat())
                }
                val labels = spendingByCategory.map { it.key }

                BarChartUiData(entries = entries, labels = labels)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BarChartUiData()
        )
}

private fun isWithinLastYear(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.YEAR, -1)
    val oneYearAgo = calendar.time
    return date.after(oneYearAgo)
}

private fun isCurrentMonth(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    val transactionCalendar = Calendar.getInstance()
    transactionCalendar.time = date
    val transactionMonth = transactionCalendar.get(Calendar.MONTH)
    val transactionYear = transactionCalendar.get(Calendar.YEAR)

    return transactionMonth == currentMonth && transactionYear == currentYear
}