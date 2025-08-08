// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/budget/BudgetRow.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rahul.auric.fintrack.auricfin.ui.theme.AuricFinTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BudgetRow(
    categoryName: String,
    spentAmount: Double,
    totalBudget: Double,
    modifier: Modifier = Modifier
) {
    // Format amounts with commas and currency symbol
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    currencyFormat.maximumFractionDigits = 0 // Optional: remove decimals

    val spentText = currencyFormat.format(spentAmount)
    val totalText = currencyFormat.format(totalBudget)

    // Calculate progress, ensuring it doesn't exceed 1.0
    val progress = (spentAmount / totalBudget).toFloat().coerceIn(0f, 1f)

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$spentText / $totalText",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBudgetRow() {
    AuricFinTheme {
        BudgetRow(
            categoryName = "Food & Drinks",
            spentAmount = 250.0,
            totalBudget = 300.0,
            modifier = Modifier.padding(16.dp)
        )
    }
}