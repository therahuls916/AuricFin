// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/budget/BudgetScreen.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.budget

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.auric.fintrack.auricfin.ui.theme.AuricFinTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BudgetScreen(
    // --- 1. Inject the ViewModel ---
    viewModel: BudgetViewModel = hiltViewModel()
) {
    // --- 2. Collect the real data ---
    val budgetProgressList by viewModel.budgetProgress.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Spending Breakdown") } // Changed title
            )
        },
        floatingActionButton = {
            // --- 3. Update FAB to show a message ---
            FloatingActionButton(onClick = {
                Toast.makeText(
                    context,
                    "Setting custom budgets is a future feature!",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add or Edit Budget")
            }
        }
    ) { innerPadding ->
        // --- 4. Add an empty state ---
        if (budgetProgressList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No expense data for this month.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- 5. Use the real data in the list ---
                items(budgetProgressList) { budget ->
                    BudgetRow(
                        categoryName = budget.category,
                        spentAmount = budget.spent,
                        totalBudget = budget.total // This is now total monthly spending
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBudgetScreen() {
    AuricFinTheme {
        BudgetScreen()
    }
}