// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/reports/BarChart.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.reports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun CategoryBarChart(barChartData: BarChartUiData) {
    // Define the color based on the current theme
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()

    if (barChartData.entries.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No expense data for this month to display chart.")
        }
    } else {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            factory = { context ->
                BarChart(context).apply {
                    description.isEnabled = false
                    legend.isEnabled = false
                    axisRight.isEnabled = false

                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        // --- THIS IS THE FIX ---
                        // Directly assign the textColor variable
                        this.textColor = textColor
                        granularity = 1f
                        isGranularityEnabled = true
                    }

                    axisLeft.apply {
                        setDrawGridLines(false)
                        // --- THIS IS THE FIX ---
                        // Directly assign the textColor variable
                        this.textColor = textColor
                        axisMinimum = 0f
                    }
                }
            },
            update = { chart ->
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(barChartData.labels)
                chart.xAxis.labelCount = barChartData.labels.size

                val dataSet = BarDataSet(barChartData.entries, "Expenses by Category").apply {
                    colors = ColorTemplate.MATERIAL_COLORS.toList()
                    // --- THIS IS THE FIX ---
                    // Directly assign the textColor variable
                    this.valueTextColor = textColor
                    valueTextSize = 10f
                }

                chart.data = BarData(dataSet)
                chart.invalidate()
            }
        )
    }
}