// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/reports/LineChart.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.reports

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun LineChart(chartData: LineChartUiData) {
    // Get colors from the MaterialTheme for theme-awareness
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

    // FIX 2: Add an empty state check
    if (chartData.entries.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Not enough data to display trend.")
        }
    } else {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            factory = { context ->
                LineChart(context).apply {
                    description.isEnabled = false
                    legend.isEnabled = false
                    axisRight.isEnabled = false

                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setDrawGridLines(false)
                    xAxis.granularity = 1f
                    // FIX 1: Use theme-aware text color
                    xAxis.textColor = textColor

                    axisLeft.setDrawGridLines(false)
                    axisLeft.axisMinimum = 0f
                    // FIX 1: Use theme-aware text color
                    axisLeft.textColor = textColor
                }
            },
            update = { chart ->
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(chartData.labels)

                val dataSet = LineDataSet(chartData.entries, "Monthly Expenses").apply {
                    // FIX 1: Use theme-aware primary color
                    color = primaryColor
                    // FIX 1: Use theme-aware text color
                    valueTextColor = textColor
                    valueTextSize = 10f
                    setDrawCircles(false)
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    setDrawFilled(true)
                    // FIX 1: Use theme-aware primary color for the gradient
                    val gradient = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(primaryColor, Color.TRANSPARENT)
                    )
                    fillDrawable = gradient
                }

                chart.data = LineData(dataSet)
                chart.invalidate()
            }
        )
    }
}