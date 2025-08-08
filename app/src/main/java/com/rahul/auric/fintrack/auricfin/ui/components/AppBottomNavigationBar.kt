package com.rahul.auric.fintrack.auricfin.ui.components // Make sure package name is correct

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rahul.auric.fintrack.auricfin.ui.bottomNavItems
import com.rahul.auric.fintrack.auricfin.ui.theme.AuricFinTheme

@Composable
fun AppBottomNavigationBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = item.route == currentRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppBottomNavigationBar() {
    AuricFinTheme {
        AppBottomNavigationBar(
            currentRoute = "home",
            onItemClick = {}
        )
    }
}