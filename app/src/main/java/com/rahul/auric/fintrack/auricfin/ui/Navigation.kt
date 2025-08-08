// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/Navigation.kt
package com.rahul.auric.fintrack.auricfin.ui // Make sure this package name is correct

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector

// Sealed class to define the routes for screens
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Transactions : Screen("transactions")
    object Reports : Screen("reports")
    object Budget : Screen("budget")

    // Screens without bottom bar items
    object AddTransaction : Screen("add_transaction")
    object Settings : Screen("settings")
    object ManageCategories : Screen("manage_categories")

    object EditTransaction : Screen("edit_transaction/{transactionId}") {
        // Helper function to build the full route with a specific ID.
        fun createRoute(transactionId: Int) = "edit_transaction/$transactionId"
    }
}

// Data class to represent items in the bottom navigation bar
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

// List of items to display in the bottom navigation bar
val bottomNavItems = listOf(
    BottomNavItem(
        label = "Home",
        icon = Icons.Default.Home,
        route = Screen.Home.route
    ),
    BottomNavItem(
        label = "Add",
        icon = Icons.Default.Add,
        route = Screen.AddTransaction.route
    ),
    BottomNavItem(
        label = "Transactions",
        icon = Icons.Default.List,
        route = Screen.Transactions.route
    ),
    BottomNavItem(
        label = "Reports",
        icon = Icons.Default.BarChart, // FIX: Corrected icon
        route = Screen.Reports.route
    ),
    BottomNavItem(
        label = "Budget",
        icon = Icons.Default.PieChart, // FIX: Corrected icon
        route = Screen.Budget.route
    )
)