// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/edit_transaction/EditTransactionScreen.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.edit_transaction

// --- FIX: Import SingleChoiceSegmentedButtonRow instead ---
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditTransactionScreen(
    onNavigateBack: () -> Unit,
    viewModel: EditTransactionViewModel = hiltViewModel()
) {
    // ... (All the state variables and LaunchedEffect remain the same) ...
    val transactionState by viewModel.transactionState.collectAsState()
    val context = LocalContext.current

    if (transactionState == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var amount by remember { mutableStateOf("") }
    var isAmountError by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Date()) }

    LaunchedEffect(transactionState) {
        transactionState?.let {
            amount = abs(it.amount).toString()
            note = it.note ?: ""
            selectedTab = if (it.type == "expense") 0 else 1
            selectedCategory = it.category
            selectedDate = it.date
        }
    }

    val tabs = listOf("Expense", "Income")
    val showDatePicker = remember { mutableStateOf(false) }
    var isCategoryMenuExpanded by remember { mutableStateOf(false) }

    val transactionType = if (selectedTab == 0) "expense" else "income"
    val categories by if (transactionType == "expense") {
        viewModel.expenseCategories.collectAsState()
    } else {
        viewModel.incomeCategories.collectAsState()
    }

    val isFormValid by remember(amount, selectedCategory) {
        derivedStateOf {
            val amountAsDouble = amount.toDoubleOrNull()
            amountAsDouble != null && amountAsDouble > 0 && selectedCategory.isNotBlank()
        }
    }

    if (showDatePicker.value) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate.time)
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedDate = Date(it) }
                    showDatePicker.value = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker.value = false
                }) { Text("Cancel") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Transaction") },
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
            // --- FIX: Use SingleChoiceSegmentedButtonRow ---
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                tabs.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = tabs.size),
                        // The onClick now correctly expects an Int
                        onClick = { selectedTab = index },
                        selected = index == selectedTab
                    ) { Text(label) }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            // ... (Rest of the UI remains the same) ...
            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    isAmountError = it.toDoubleOrNull() == null && it.isNotEmpty()
                },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = isAmountError,
                supportingText = { if (isAmountError) Text("Please enter a valid number") },
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
                    onDismissRequest = { isCategoryMenuExpanded = false }) {
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
            Box(modifier = Modifier.clickable { showDatePicker.value = true }) {
                OutlinedTextField(
                    value = sdf.format(selectedDate),
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.DateRange, "Select Date") },
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
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
                    viewModel.updateTransaction(
                        amount = amount.toDouble(),
                        type = transactionType,
                        category = selectedCategory,
                        date = selectedDate,
                        note = note.takeIf { it.isNotBlank() }
                    )
                    Toast.makeText(context, "Transaction Updated", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}