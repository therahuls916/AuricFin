// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/transactions/TransactionsScreen.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.transactions

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rahul.auric.fintrack.auricfin.data.local.Transaction
import com.rahul.auric.fintrack.auricfin.ui.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val transactions by viewModel.transactions.collectAsState()
    val typeFilter by viewModel.transactionTypeFilter.collectAsState()
    val categoryFilter by viewModel.categoryFilter.collectAsState()
    val categories by viewModel.allCategories.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()

    var isTypeMenuExpanded by remember { mutableStateOf(false) }
    var isCategoryMenuExpanded by remember { mutableStateOf(false) }
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (selectedTransaction != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedTransaction = null },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                ListItem(
                    headlineContent = { Text("Edit Transaction") },
                    leadingContent = { Icon(Icons.Default.Edit, contentDescription = "Edit") },
                    modifier = Modifier.clickable {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            selectedTransaction?.let {
                                navController.navigate(
                                    Screen.EditTransaction.createRoute(
                                        it.id
                                    )
                                )
                            }
                            selectedTransaction = null
                        }
                    }
                )
                ListItem(
                    headlineContent = { Text("Delete Transaction") },
                    leadingContent = { Icon(Icons.Default.Delete, contentDescription = "Delete") },
                    modifier = Modifier.clickable {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            showDeleteDialog = true
                        }
                    }
                )
            }
        }
    }

    if (showDeleteDialog && selectedTransaction != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                selectedTransaction = null
            },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        selectedTransaction?.let { viewModel.deleteTransaction(it) }
                        showDeleteDialog = false
                        selectedTransaction = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    selectedTransaction = null
                }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Transactions") },
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search Transactions"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.toggleSortOrder() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Date")
                    Icon(
                        if (sortOrder == SortOrder.NEWEST_FIRST) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = "Sort Order"
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { isCategoryMenuExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(categoryFilter, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = isCategoryMenuExpanded,
                        onDismissRequest = { isCategoryMenuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("All") },
                            onClick = {
                                viewModel.setCategoryFilter("All"); isCategoryMenuExpanded = false
                            })
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    viewModel.setCategoryFilter(category.name); isCategoryMenuExpanded =
                                    false
                                })
                        }
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { isTypeMenuExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(typeFilter.name.lowercase().replaceFirstChar { it.titlecase() })
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = isTypeMenuExpanded,
                        onDismissRequest = { isTypeMenuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("All") },
                            onClick = {
                                viewModel.setTransactionTypeFilter(TransactionTypeFilter.ALL); isTypeMenuExpanded =
                                false
                            })
                        // --- FIX: Corrected the function name here ---
                        DropdownMenuItem(
                            text = { Text("Income") },
                            onClick = {
                                viewModel.setTransactionTypeFilter(TransactionTypeFilter.INCOME); isTypeMenuExpanded =
                                false
                            })
                        DropdownMenuItem(
                            text = { Text("Expense") },
                            onClick = {
                                viewModel.setTransactionTypeFilter(TransactionTypeFilter.EXPENSE); isTypeMenuExpanded =
                                false
                            })
                    }
                }
            }
            if (transactions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No transactions match the current filter.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(transactions) { transaction ->
                        val formattedDate = transaction.date?.let {
                            android.text.format.DateFormat.format("dd/MM/yy", it).toString()
                        } ?: "No Date"
                        Card(modifier = Modifier.fillMaxWidth()) {
                            TransactionRow(
                                categoryName = transaction.category,
                                date = formattedDate,
                                amount = transaction.amount,
                                note = transaction.note,
                                onLongClick = { selectedTransaction = transaction }
                            )
                        }
                    }
                }
            }
        }
    }
}