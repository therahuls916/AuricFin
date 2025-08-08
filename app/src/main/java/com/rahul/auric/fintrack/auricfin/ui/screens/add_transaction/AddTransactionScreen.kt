// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/add_transaction/AddTransactionScreen.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.add_transaction

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.auric.fintrack.auricfin.ui.theme.AuricFinTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddTransactionScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    var amount by remember { mutableStateOf("") }
    // --- FIX: Add state to track if the amount field has an error ---
    var isAmountError by remember { mutableStateOf(false) }

    var note by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Expense", "Income")

    var selectedDate by remember { mutableStateOf(Date()) }
    val showDatePicker = remember { mutableStateOf(false) }

    var isCategoryMenuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }

    val transactionType = if (selectedTab == 0) "expense" else "income"
    val categories by if (transactionType == "expense") {
        viewModel.expenseCategories.collectAsState()
    } else {
        viewModel.incomeCategories.collectAsState()
    }

    LaunchedEffect(selectedTab) { selectedCategory = "" }

    val context = LocalContext.current

    // --- FIX: Improve form validation logic ---
    val isFormValid by remember(amount, selectedCategory) {
        derivedStateOf {
            val amountAsDouble = amount.toDoubleOrNull()
            amountAsDouble != null && amountAsDouble > 0 && selectedCategory.isNotBlank()
        }
    }


    if (showDatePicker.value) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.time
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate = Date(it)
                        }
                        showDatePicker.value = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            SegmentedButtonRow(
                items = tabs,
                selectedIndex = selectedTab,
                onSelectionChange = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- FIX: Update the Amount field to show errors ---
            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    // Check for an error whenever the value changes
                    isAmountError = it.toDoubleOrNull() == null && it.isNotEmpty()
                },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = isAmountError,
                supportingText = {
                    if (isAmountError) {
                        Text("Please enter a valid number")
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = isCategoryMenuExpanded,
                onExpandedChange = { isCategoryMenuExpanded = !isCategoryMenuExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryMenuExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isCategoryMenuExpanded,
                    onDismissRequest = { isCategoryMenuExpanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedCategory = category.name
                                isCategoryMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            Box(
                modifier = Modifier.clickable { showDatePicker.value = true }
            ) {
                OutlinedTextField(
                    value = sdf.format(selectedDate),
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.DateRange, "Select Date") },
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        showDatePicker.value = true
                                    }
                                }
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (Optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                enabled = isFormValid,
                onClick = {
                    viewModel.addTransaction(
                        amount = amount.toDouble(), // We can now safely use .toDouble() because isFormValid checks it
                        type = transactionType,
                        category = selectedCategory,
                        date = selectedDate,
                        note = note.takeIf { it.isNotBlank() }
                    )
                    Toast.makeText(context, "Transaction Added", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SegmentedButtonRow(
    items: List<String>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit
) {
    MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = items.size),
                onCheckedChange = { onSelectionChange(index) },
                checked = index == selectedIndex
            ) {
                Text(label)
            }
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAddTransactionScreen() {
    AuricFinTheme {
        AddTransactionScreen(onNavigateBack = {})
    }
}