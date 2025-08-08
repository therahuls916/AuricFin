// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/components/CategoryIcon.kt
package com.rahul.auric.fintrack.auricfin.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

// A helper function to map a category name to a specific Material Icon.
@Composable
fun getIconForCategory(category: String): ImageVector {
    return when (category.lowercase()) {
        // Expense Categories
        "food" -> Icons.Default.Fastfood
        "transport" -> Icons.Default.LocalGasStation // Using Gas Station for transport
        "shopping" -> Icons.Default.ShoppingCart
        "entertainment" -> Icons.Default.Movie
        "utilities" -> Icons.Default.Home
        "rent" -> Icons.Default.Home

        // Income Categories
        "salary" -> Icons.Default.AccountBalanceWallet
        "freelance" -> Icons.Default.BusinessCenter
        "investment" -> Icons.Default.TrendingUp

        // Add more specific mappings as needed
        "coffee shop" -> Icons.Default.Coffee
        "gasoline" -> Icons.Default.LocalGasStation

        // Default icon if no match is found
        else -> Icons.Default.QuestionMark
    }
}