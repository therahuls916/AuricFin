// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/settings/ManageCategoriesScreen.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.auric.fintrack.auricfin.data.local.Category
import com.rahul.auric.fintrack.auricfin.ui.theme.AuricFinTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ManageCategoriesScreen(
    onNavigateBack: () -> Unit,
    viewModel: ManageCategoriesViewModel = hiltViewModel()
) {
    val expenseCategories by viewModel.expenseCategories.collectAsState()
    val incomeCategories by viewModel.incomeCategories.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Expense", "Income")

    var showAddCategoryDialog by remember { mutableStateOf(false) }

    // --- FIX: Add state to manage the delete confirmation dialog ---
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Categories") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddCategoryDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            val categoriesToShow = if (selectedTab == 0) expenseCategories else incomeCategories

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(categoriesToShow) { category ->
                    ListItem(
                        headlineContent = { Text(category.name) },
                        trailingContent = {
                            // --- FIX: Set the categoryToDelete to show the dialog ---
                            IconButton(onClick = { categoryToDelete = category }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Category")
                            }
                        }
                    )
                    Divider()
                }
            }
        }
    }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { categoryName ->
                val type = if (selectedTab == 0) "expense" else "income"
                viewModel.addCategory(categoryName, type)
                showAddCategoryDialog = false
            }
        )
    }

    // --- FIX: Show the confirmation dialog when categoryToDelete is not null ---
    categoryToDelete?.let { category ->
        DeleteCategoryDialog(
            categoryName = category.name,
            onDismiss = { categoryToDelete = null },
            onConfirm = {
                viewModel.deleteCategory(category)
                categoryToDelete = null
            }
        )
    }
}

@Composable
private fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    val isNameValid = categoryName.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Category") },
        text = {
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category Name") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(categoryName) }, enabled = isNameValid) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// --- FIX: Add a new composable for the delete confirmation dialog ---
@Composable
private fun DeleteCategoryDialog(
    categoryName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Category") },
        text = {
            Text("Are you sure you want to delete the category \"$categoryName\"? This action cannot be undone.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewManageCategoriesScreen() {
    AuricFinTheme {
        ManageCategoriesScreen(onNavigateBack = {})
    }
}