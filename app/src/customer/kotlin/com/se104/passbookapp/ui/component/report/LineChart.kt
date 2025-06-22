package com.se104.passbookapp.ui.component.report

import android.R.attr.entries
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import java.text.DecimalFormat

import kotlin.collections.getOrNull
import kotlin.collections.mapIndexed
import kotlin.collections.maxOfOrNull
import kotlin.let
import kotlin.math.absoluteValue
import kotlin.random.Random

@Composable
fun <T> LineChartFromEntries(
    modifier: Modifier = Modifier,
    lines: List<ChartLine<T>>,
    steps: Int = 6,
) {
    // Lấy toàn bộ value từ các đường
    val allValues = lines.flatMap { it.entries }.map { it.toFloat(it.value) }

    // Tính min/max có padding hợp lý
    val minY = allValues.minOrNull() ?: 0f
    val maxY = allValues.maxOrNull() ?: 0f
    val range = (maxY - minY).takeIf { it != 0f } ?: 1f

    val paddedMinY = if (minY >= 0f) {
        0f
    } else {
        minY - range * 0.1f
    }
    val paddedMaxY = maxY + range * 0.1f

    // Lấy nhãn trục X từ dòng đầu tiên
    val xAxisLabels = lines.firstOrNull()?.entries?.map { it.xLabel }.orEmpty()

    // Trục X
    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(Color.Transparent)
        .steps(xAxisLabels.size - 1)
        .labelData { i -> xAxisLabels.getOrNull(i) ?: "" }
        .labelAndAxisLinePadding(15.dp)
        .axisLineColor(MaterialTheme.colorScheme.primary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()
    val numberFormat = DecimalFormat("#,##0.##")
    // Trục Y
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(25.dp)
        .labelData { i ->
            val step = (paddedMaxY - paddedMinY) / steps
            numberFormat.format(paddedMinY + i * step)
        }
        .axisLineColor(MaterialTheme.colorScheme.primary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()

    // Dữ liệu các đường
    val linePlotData = LinePlotData(
        lines = lines.map { chartLine ->
            val entryCount = chartLine.entries.size - 1
            val pointsData = chartLine.entries.mapIndexed { index, entry ->
                Point(index.toFloat(), entry.toFloat(entry.value))
            }

            Line(
                dataPoints = pointsData,
                lineStyle = LineStyle(
                    color = chartLine.color,
                    lineType = LineType.SmoothCurve(isDotted = false)
                ),
                intersectionPoint = IntersectionPoint(color = chartLine.color),
                selectionHighlightPoint = SelectionHighlightPoint(color = chartLine.color),
                selectionHighlightPopUp = SelectionHighlightPopUp()
            )
        }
    )

    // Gói dữ liệu cho biểu đồ
    val lineChartData = LineChartData(
        linePlotData = linePlotData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = MaterialTheme.colorScheme.outline),
        backgroundColor = MaterialTheme.colorScheme.surface,
    )

    // Render
    LineChart(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp),
        lineChartData = lineChartData
    )
}


@Composable
fun<T: Any> LineChartWithLegend(
    lines: List<ChartLine<T>>,
    modifier: Modifier = Modifier,
    steps: Int = 5,
) {
    Column(modifier = modifier) {
        LineChartFromEntries(
            lines = lines,
            steps = steps,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            lines.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(it.color, shape = CircleShape)
                    )
                    Text(text = it.label, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}


data class ChartLine<T>(
    val label: String,
    val entries: List<ChartEntry<T>>,
    val color: Color,

) {
    companion object {
        fun <T> create(
            label: String,
            entries: List<ChartEntry<T>>,
        ): ChartLine<T> {
            return ChartLine(
                label = label,
                entries = entries,
                color = generateStableColor(label)
            )
        }


        private fun generateStableColor(key: String): Color {
            val hash = key.hashCode().absoluteValue
            val hue = (hash % 360).toFloat()  // 0..359
            val saturation = 0.7f             // bão hòa cao → màu đậm
            val value = 0.9f                  // độ sáng cao → dễ nhìn

            val hsvColor = android.graphics.Color.HSVToColor(floatArrayOf(hue, saturation, value))
            return Color(hsvColor)
        }
    }
}



data class ChartEntry<T>(
    val xLabel: String,
    val value: T,
    val toFloat: (T) -> Float = { (it as Number).toFloat() }, // fallback default

)
