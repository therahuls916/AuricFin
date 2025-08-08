// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/home/HomeScreen.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.auric.fintrack.auricfin.ui.screens.transactions.TransactionRow
import com.rahul.auric.fintrack.auricfin.ui.theme.AuricFinTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    val totalBalance by viewModel.totalBalance.collectAsState()
    val monthlyExpenses by viewModel.monthlyExpenses.collectAsState()
    val pieChartData by viewModel.pieChartData.collectAsState()

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FinTrack") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            OverviewCard(title = "Total Balance", amount = currencyFormat.format(totalBalance))
            Spacer(modifier = Modifier.height(16.dp))
            OverviewCard(
                title = "Monthly Expenses",
                amount = currencyFormat.format(kotlin.math.abs(monthlyExpenses))
            )
            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Text(
                    text = "Spending Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Spending by Category", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            currencyFormat.format(kotlin.math.abs(monthlyExpenses)),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text("This Month", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(16.dp))
                        HomePieChart(pieChartData = pieChartData)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (recentTransactions.isEmpty()) {
                    Text("No recent transactions.", style = MaterialTheme.typography.bodyMedium)
                } else {
                    recentTransactions.take(3).forEach { transaction ->
                        // --- FIX: Provide the new 'note' and 'onLongClick' parameters ---
                        TransactionRow(
                            categoryName = transaction.category,
                            date = android.text.format.DateFormat.format(
                                "dd/MM/yy",
                                transaction.date
                            ).toString(),
                            amount = transaction.amount,
                            note = transaction.note,
                            onLongClick = { /* No action needed on home screen */ }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreen() {
    AuricFinTheme {
        HomeScreen(onNavigateToSettings = {})
    }
}