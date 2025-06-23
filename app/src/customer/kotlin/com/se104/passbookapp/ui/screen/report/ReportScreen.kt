package com.se104.passbookapp.ui.screen.report

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.se104.passbookapp.data.model.MonthlyReport
import com.se104.passbookapp.data.model.MonthlyReportDetails
import com.se104.passbookapp.ui.component.MonthPicker
import com.se104.passbookapp.ui.component.report.ChartEntry
import com.se104.passbookapp.ui.component.report.ChartLine
import com.se104.passbookapp.ui.component.report.LineChartWithLegend
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.LoadingAnimation
import com.se104.passbookapp.ui.screen.components.Nothing
import com.se104.passbookapp.ui.screen.components.Retry
import com.se104.passbookapp.ui.screen.components.TabWithPager
import com.se104.passbookapp.utils.StringUtils
import java.math.BigDecimal

@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {

                is ReportState.Event.ShowErrorToast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                ReportState.Event.OnBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getMonthlyReports()
        viewModel.getMonthlyReport()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderDefaultView(
            text = "Thống kê",
            onBack = {
                viewModel.onAction(ReportState.Action.OnBack)
            })


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                MonthSection(
                    month = uiState.fromMonth,
                    year = uiState.fromYear,
                    onClicked = { month, year ->
                        viewModel.onAction(ReportState.Action.OnChangeFromMonthYear(year, month))

                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                MonthSection(
                    month = uiState.toMonth,
                    year = uiState.toYear,
                    onClicked = { month, year ->
                        viewModel.onAction(ReportState.Action.OnChangeToMonthYear(year, month))
                    }
                )
            }
            TabWithPager(
                tabs = listOf("Tổng thu", "Tổng chi", "Chênh lệch"),
                pages = listOf(
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        ) {
                            when (uiState.monthlyReportsState) {
                                is ReportState.MonthlyReportsState.Error -> {
                                    Retry(
                                        modifier = Modifier.fillMaxSize(),
                                        onClicked = {
                                            viewModel.getMonthlyReports()
                                        },
                                        message = (uiState.monthlyReportsState as ReportState.MonthlyReportsState.Error).message,
                                    )
                                }

                                ReportState.MonthlyReportsState.Loading -> {
                                    LoadingAnimation(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .align(Alignment.Center),
                                    )
                                }

                                ReportState.MonthlyReportsState.Success -> {
                                    if (uiState.monthlyReports.isEmpty()) {
                                        Nothing(
                                            modifier = Modifier.fillMaxSize(),
                                            icon = Icons.Default.BarChart,
                                            text = "Chưa có thống kê tháng"


                                        )
                                    } else {
                                        val lines = prepareChartLines(uiState.monthlyReports) { it.totalIncome }
                                        LineChartWithLegend(
                                            lines = lines,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                }
                            }
                        }


                    },
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        ) {
                            when (uiState.monthlyReportsState) {
                                is ReportState.MonthlyReportsState.Error -> {
                                    Retry(
                                        modifier = Modifier.fillMaxSize(),
                                        onClicked = {
                                            viewModel.getMonthlyReports()},
                                        message = (uiState.monthlyReportsState as ReportState.MonthlyReportsState.Error).message,
                                    )
                                }

                                ReportState.MonthlyReportsState.Loading -> {
                                    LoadingAnimation(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .align(Alignment.Center),
                                    )
                                }

                                ReportState.MonthlyReportsState.Success -> {
                                    if (uiState.monthlyReports.isEmpty()) {
                                        Nothing(
                                            modifier = Modifier.fillMaxSize(),
                                            icon = Icons.Default.BarChart,
                                            text = "Chưa có thống kê tháng"


                                        )
                                    } else {
                                       val lines = prepareChartLines(uiState.monthlyReports) {it.totalExpense  }
                                        LineChartWithLegend(
                                            lines = lines,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                }
                            }
                        }

                    },
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        ) {
                            when (uiState.monthlyReportsState) {
                                is ReportState.MonthlyReportsState.Error -> {
                                    Retry(
                                        modifier = Modifier.fillMaxSize(),
                                        onClicked = {
                                            viewModel.getMonthlyReports()
                                        },
                                        message = (uiState.monthlyReportsState as ReportState.MonthlyReportsState.Error).message,
                                    )
                                }

                                ReportState.MonthlyReportsState.Loading -> {
                                    LoadingAnimation(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .align(Alignment.Center),
                                    )
                                }

                                ReportState.MonthlyReportsState.Success -> {
                                    if (uiState.monthlyReports.isEmpty()) {
                                        Nothing(
                                            modifier = Modifier.fillMaxSize(),
                                            icon = Icons.Default.BarChart,
                                            text = "Chưa có thống kê tháng"


                                        )
                                    } else {
                                        val lines = prepareChartLines(uiState.monthlyReports) {it.difference }
                                        LineChartWithLegend(
                                            lines = lines,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                }
                            }
                        }

                    },
                ),
                onTabSelected = {

                },
                modifier = Modifier.fillMaxWidth()

            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                IconButton(
                    onClick = {
                        viewModel.onAction(
                            ReportState.Action.OnChangeSelectedMonthYear(

                               year = uiState.selectedYear,
                                month = uiState.selectedMonth - 1,
                            )
                        )
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color = Color.Transparent),

                    ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = "Left",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                MonthSection(
                    month = uiState.selectedMonth,
                    year = uiState.selectedYear,
                    onClicked = { month, year ->
                        viewModel.onAction(
                            ReportState.Action.OnChangeSelectedMonthYear(
                                year,
                                month
                            )
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        viewModel.onAction(
                            ReportState.Action.OnChangeSelectedMonthYear(

                                year =uiState.selectedYear,
                                month = uiState.selectedMonth + 1,
                            )
                        )
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color = Color.Transparent),

                    ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = "Left",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth().height(500.dp),

            ){
                when(uiState.monthlyReportState){
                    is ReportState.MonthlyReportState.Error -> {
                        val message = (uiState.monthlyReportState as ReportState.MonthlyReportState.Error).message
                        Retry(
                            message = message,
                            onClicked = {
                                viewModel.getMonthlyReport()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    ReportState.MonthlyReportState.Loading -> {
                        LoadingAnimation(
                            modifier = Modifier.fillMaxSize().align(Alignment.Center)
                        )
                    }
                    is ReportState.MonthlyReportState.Success -> {
                        val monthlyReport = (uiState.monthlyReportState as ReportState.MonthlyReportState.Success).data
                        MonthlyReportTable(monthlyReport)
                    }
                }
            }


        }


    }
}
@Composable
fun MonthlyReportTable(report: MonthlyReport) {
    val headerStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.extraLarge)
                .padding(8.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {


            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), MaterialTheme.shapes.large)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Loại tiết kiệm",
                    modifier = Modifier.weight(1f),
                    style = headerStyle,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Tổng thu",
                    modifier = Modifier.weight(1f),
                    style = headerStyle
                )
                Text(
                    text = "Tổng chi",
                    modifier = Modifier.weight(1f),
                    style = headerStyle
                )
                Text(
                    text = "Chênh lệch",
                    modifier = Modifier.weight(1f),
                    style = headerStyle
                )
            }

            // Data rows
            if (report.monthlyReportDetails.isEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Không có dữ liệu chi tiết cho tháng này",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                report.monthlyReportDetails.forEach { detail ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = detail.savingTypeName,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = StringUtils.formatCurrency(detail.totalIncome),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,

                        )
                        Text(
                            text = StringUtils.formatCurrency(detail.totalExpense),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = StringUtils.formatCurrency(detail.difference),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            // Tổng cộng
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = "Tổng cộng",
                    modifier = Modifier.weight(1f),
                    style = headerStyle
                )
                Text(
                    text = StringUtils.formatCurrency(report.totalIncome),
                    modifier = Modifier.weight(1f),
                    style = headerStyle,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = StringUtils.formatCurrency(report.totalExpense),
                    modifier = Modifier.weight(1f),
                    style = headerStyle,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = StringUtils.formatCurrency(report.difference),
                    modifier = Modifier.weight(1f),
                    style = headerStyle,
                    textAlign = TextAlign.Center
                )
            }
        }


}



@Composable
fun MonthSection(
    modifier: Modifier = Modifier,
    month: Int,
    year: Int,
    onClicked: (Int, Int) -> Unit,

    ) {
    var showDialog by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .width(127.dp)
            .height(37.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                showDialog = true
            },
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Month Selected",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "$month/$year",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

    }
    if (showDialog) {
        MonthPicker(
            currentMonth = month - 1,
            currentYear = year,
            confirmButtonCLicked = onClicked,
            cancelClicked = {
                showDialog = false
            }
        )
    }
}


fun prepareChartLines(
    reports: List<MonthlyReport>,
    valueSelector: (MonthlyReportDetails) -> BigDecimal
): List<ChartLine<BigDecimal>> {
    val sortedReports = reports.sortedBy { it.reportMonth }
    val monthLabels = sortedReports.map { "${it.reportMonth.monthValue}/${it.reportMonth.year}" }


    val allSavingTypes = sortedReports
        .flatMap { it.monthlyReportDetails }
        .map { it.savingTypeName }
        .toSet()

    val savingTypeMap = allSavingTypes.associateWith { mutableListOf<ChartEntry<BigDecimal>>() }

    for ((report, monthLabel) in sortedReports.zip(monthLabels)) {
        val detailsByType = report.monthlyReportDetails.associateBy { it.savingTypeName }

        for (type in allSavingTypes) {
            val detail = detailsByType[type]
            val value = detail?.let { valueSelector(it) } ?: BigDecimal.ZERO

            savingTypeMap[type]?.add(
                ChartEntry(
                    xLabel = monthLabel,
                    value = value
                )
            )
        }
    }

    return savingTypeMap.map { (savingTypeName, entries) ->
        ChartLine.create(
            label = savingTypeName,
            entries = entries
        )
    }
}