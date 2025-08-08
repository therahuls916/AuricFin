// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/MainScreen.kt
package com.rahul.auric.fintrack.auricfin.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rahul.auric.fintrack.auricfin.ui.Screen
import com.rahul.auric.fintrack.auricfin.ui.components.AppBottomNavigationBar
import com.rahul.auric.fintrack.auricfin.ui.screens.add_transaction.AddTransactionScreen
import com.rahul.auric.fintrack.auricfin.ui.screens.budget.BudgetScreen
import com.rahul.auric.fintrack.auricfin.ui.screens.edit_transaction.EditTransactionScreen // <-- Import new screen
import com.rahul.auric.fintrack.auricfin.ui.screens.home.HomeScreen
import com.rahul.auric.fintrack.auricfin.ui.screens.reports.ReportsScreen
import com.rahul.auric.fintrack.auricfin.ui.screens.settings.ManageCategoriesScreen
import com.rahul.auric.fintrack.auricfin.ui.screens.settings.SettingsScreen
import com.rahul.auric.fintrack.auricfin.ui.screens.transactions.TransactionsScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    val bottomBarScreens = listOf(
        Screen.Home.route,
        Screen.Transactions.route,
        Screen.Reports.route,
        Screen.Budget.route
    )
    val showBottomBar = currentRoute in bottomBarScreens

    androidx.compose.material3.Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        if (route == Screen.AddTransaction.route) {
                            navController.navigate(route)
                        } else {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(onNavigateToSettings = { navController.navigate(Screen.Settings.route) })
            }
            composable(Screen.Transactions.route) {
                // --- FIX: Pass the navController to the TransactionsScreen ---
                TransactionsScreen(navController = navController)
            }
            composable(Screen.Reports.route) { ReportsScreen() }
            composable(Screen.Budget.route) { BudgetScreen() }
            composable(Screen.AddTransaction.route) {
                AddTransactionScreen(onNavigateBack = { navController.navigateUp() })
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onManageCategoriesClicked = { navController.navigate(Screen.ManageCategories.route) },
                    onExportDataClicked = {
                        Toast.makeText(
                            context,
                            "Export Data Clicked",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onResetAllDataClicked = {
                        Toast.makeText(
                            context,
                            "Reset All Data Clicked",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onDeleteAllDataClicked = {
                        Toast.makeText(
                            context,
                            "Delete All Data Clicked",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
            composable(Screen.ManageCategories.route) {
                ManageCategoriesScreen(onNavigateBack = { navController.navigateUp() })
            }

            // --- FIX: Add the new composable for the Edit Transaction Screen ---
            composable(
                route = Screen.EditTransaction.route,
                arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
            ) {
                EditTransactionScreen(onNavigateBack = { navController.navigateUp() })
            }
        }
    }
}