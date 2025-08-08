// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/home/HomePieChart.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import android.graphics.Color as AndroidColor

@Composable
fun HomePieChart(pieChartData: List<PieChartData>) {
    if (pieChartData.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No expense data for this month.")
        }
    } else {
        // AndroidView is the bridge between Compose and traditional Android Views
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            factory = { context ->
                // This block runs once to create the View
                PieChart(context).apply {
                    // Basic chart setup
                    isDrawHoleEnabled = true
                    holeRadius = 35f
                    transparentCircleRadius = 45f
                    description.isEnabled = false
                    legend.isEnabled = false // We can add a custom legend later if needed
                    setEntryLabelColor(AndroidColor.BLACK)
                }
            },
            update = { chart ->
                // This block runs whenever the pieChartData changes
                val entries = pieChartData.map { PieEntry(it.value, it.label) }

                val dataSet = PieDataSet(entries, "Expenses").apply {
                    colors = ColorTemplate.MATERIAL_COLORS.toList()
                    valueTextColor = AndroidColor.BLACK
                    valueTextSize = 12f
                }

                chart.data = PieData(dataSet)
                chart.invalidate() // Redraw the chart
            }
        )
    }
}