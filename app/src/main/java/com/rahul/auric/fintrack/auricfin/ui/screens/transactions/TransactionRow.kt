// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/transactions/TransactionRow.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rahul.auric.fintrack.auricfin.ui.components.getIconForCategory
import com.rahul.auric.fintrack.auricfin.ui.theme.AppTheme
import com.rahul.auric.fintrack.auricfin.ui.theme.AuricFinTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionRow(
    categoryName: String,
    date: String,
    amount: Double,
    note: String?,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isIncome = amount > 0
    val amountText =
        if (isIncome) "+₹%.2f".format(amount) else "-₹%.2f".format(kotlin.math.abs(amount))
    val amountColor =
        if (isIncome) AppTheme.extendedColors.income else MaterialTheme.colorScheme.error

    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { /* No action on simple click for now */ },
                onLongClick = onLongClick
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = getIconForCategory(category = categoryName),
                contentDescription = categoryName,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                // --- FIX: Stack Date and Note vertically ---
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!note.isNullOrBlank()) {
                    Text(
                        text = note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis // Ellipsis (...) for very long notes
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = amountText,
            style = MaterialTheme.typography.bodyLarge,
            color = amountColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// Previews updated to show the new layout
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTransactionRowExpense() {
    AuricFinTheme {
        TransactionRow(
            categoryName = "Shopping",
            date = "20/01/24",
            amount = -50.00,
            note = "Weekly groceries from the store",
            onLongClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTransactionRowIncome() {
    AuricFinTheme {
        TransactionRow(
            categoryName = "Salary",
            date = "21/01/24",
            amount = 2500.00,
            note = null,
            onLongClick = {}
        )
    }
}