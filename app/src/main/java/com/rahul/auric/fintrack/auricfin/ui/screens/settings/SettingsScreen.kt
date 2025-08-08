// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/settings/SettingsScreen.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rahul.auric.fintrack.auricfin.ui.theme.AuricFinTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onManageCategoriesClicked: () -> Unit,
    onExportDataClicked: () -> Unit,
    onResetAllDataClicked: () -> Unit,
    onDeleteAllDataClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Appearance Section
            SettingsHeader("Appearance")
            Card {
                SettingsItem(
                    title = "Theme",
                    subtitle = "System (Dark)",
                    onClick = { /* TODO */ }
                )
            }

            // Data Section
            SettingsHeader("Data")
            Card {
                SettingsItem("Manage Categories", onClick = onManageCategoriesClicked)
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsItem("Export Data", onClick = onExportDataClicked)
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsItem("Reset All Data", onClick = onResetAllDataClicked)
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsItem(
                    "Delete All Data",
                    onClick = onDeleteAllDataClicked,
                    isDestructive = true
                )
            }

            // Dashboard Section
            SettingsHeader("Dashboard")
            Card {
                SettingsItem("Chart Type", subtitle = "Pie Chart", onClick = { /* TODO */ })
            }
        }
    }
}

@Composable
private fun SettingsHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String? = null,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    val titleColor =
        if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp), // Increased vertical padding
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = titleColor, style = MaterialTheme.typography.bodyLarge)
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSettingsScreen() {
    AuricFinTheme {
        SettingsScreen({}, {}, {}, {}, {})
    }
}