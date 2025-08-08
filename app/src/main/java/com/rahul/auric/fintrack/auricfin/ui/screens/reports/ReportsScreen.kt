// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/reports/ReportsScreen.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.reports

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.auric.fintrack.auricfin.ui.theme.AuricFinTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val lineChartUiData by viewModel.lineChartData.collectAsState()
    val barChartUiData by viewModel.barChartData.collectAsState()
    val totalMonthlyExpenses by viewModel.totalMonthlyExpenses.collectAsState()
    val topCategory by viewModel.topCategory.collectAsState()
    val highestExpense by viewModel.highestExpense.collectAsState()

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    var selectedTab by remember { mutableStateOf(1) }
    val tabs = listOf("Weekly", "Monthly", "Yearly")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Reports") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                tabs.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = tabs.size),
                        onCheckedChange = { selectedTab = index },
                        checked = index == selectedTab
                    ) { Text(label) }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ReportSection(title = "Expenses Trend") {
                Column {
                    Text(
                        currencyFormat.format(totalMonthlyExpenses),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        "This month",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LineChart(chartData = lineChartUiData)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ReportSection(title = "Expenses by Category") {
                CategoryBarChart(barChartData = barChartUiData)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryCard(
                    title = "Total Spent",
                    value = currencyFormat.format(totalMonthlyExpenses),
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Top Category",
                    value = topCategory,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SummaryCard(
                title = "Highest Expense",
                value = currencyFormat.format(highestExpense),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ReportSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewReportsScreen() {
    AuricFinTheme {
        ReportsScreen()
    }
}