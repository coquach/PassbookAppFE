package com.se104.passbookapp.ui.screen.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.model.MonthlyReport
import com.se104.passbookapp.domain.repository.ReportRepository
import com.se104.passbookapp.ui.screen.report.ReportState.Event.OnBack
import com.se104.passbookapp.ui.screen.report.ReportState.Event.ShowErrorToast
import com.se104.passbookapp.ui.screen.report.ReportState.MonthlyReportState
import com.se104.passbookapp.ui.screen.report.ReportState.MonthlyReportsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
) : ViewModel(){
    private val toMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    private val toYear = Calendar.getInstance().get(Calendar.YEAR)
    private val fromMonthYear = adjustMonthYear(toMonth -1, toYear)
    private val fromMonth = fromMonthYear.first
    private val fromYear = fromMonthYear.second


    private val _uiState = MutableStateFlow(
        ReportState.UiState(
            fromMonth = fromMonth,
            fromYear = fromYear,
            toMonth = toMonth,
            toYear = toYear
        )
    )
    val uiState get() = _uiState.asStateFlow()

    private val _event = Channel<ReportState.Event>()
    val event get() = _event.receiveAsFlow()




    fun getMonthlyReports() {
        viewModelScope.launch {
            reportRepository.getMonthlyReports(
                _uiState.value.fromYear,
                _uiState.value.fromMonth,
                _uiState.value.toYear,
                _uiState.value.toMonth
            ).collect { result ->
                when (result) {
                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(monthlyReportsState = MonthlyReportsState.Loading) }
                    }

                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                monthlyReportsState = MonthlyReportsState.Success,
                                monthlyReports = result.data
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                monthlyReportsState = MonthlyReportsState.Error(
                                    result.errorMessage
                                )
                            )
                        }
                    }

                }
            }
        }
    }

    fun getMonthlyReport() {
        viewModelScope.launch {
            reportRepository.getMonthlyReport(year = _uiState.value.selectedYear, month = _uiState.value.selectedMonth)
                .collect { result ->
                    when (result) {
                        is ApiResponse.Loading -> {
                            _uiState.update { it.copy(monthlyReportState = MonthlyReportState.Loading) }
                        }

                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    monthlyReportState = MonthlyReportState.Success(result.data),
                                )
                            }
                        }

                        is ApiResponse.Failure -> {
                            _uiState.update {
                                it.copy(
                                    monthlyReportState = MonthlyReportState.Error(
                                        result.errorMessage
                                    )
                                )
                            }
                        }
                    }
                }
        }
    }



    fun onAction(action: ReportState.Action) {
        when (action) {
            is ReportState.Action.OnChangeFromMonthYear -> {
                if (isMonthYearBeforeOrEqual(action.year, action.month, _uiState.value.toYear, _uiState.value.toMonth)) {
                    _uiState.update { it.copy(fromMonth = action.month, fromYear = action.year) }
                    getMonthlyReports()
                } else {
                    viewModelScope.launch {
                        _event.send(ShowErrorToast("Thời gian không hợp lệ"))
                    }
                }

            }

            is ReportState.Action.OnChangeToMonthYear -> {
                val now = Calendar.getInstance()
                val currentYear = now.get(Calendar.YEAR)
                val currentMonth = now.get(Calendar.MONTH) + 1 // vì Calendar.MONTH bắt đầu từ 0

                if (
                    isMonthYearAfterOrEqual(action.year, action.month, _uiState.value.fromYear, _uiState.value.fromMonth) &&
                    isMonthYearBeforeOrEqual(action.year, action.month, currentYear, currentMonth)
                ) {
                    _uiState.update { it.copy(toMonth = action.month, toYear = action.year) }
                    getMonthlyReports()
                } else {
                    viewModelScope.launch {
                        _event.send(ShowErrorToast("Thời gian không hợp lệ"))
                    }
                }
            }

            is ReportState.Action.OnChangeSelectedMonthYear -> {
                val (newMonth, newYear) = adjustMonthYear(action.month, action.year)
                val now = Calendar.getInstance()
                val nowMonth = now.get(Calendar.MONTH) + 1
                val nowYear = now.get(Calendar.YEAR)

                if (isMonthYearBeforeOrEqual(newYear, newMonth, nowYear, nowMonth)) {
                    _uiState.update {
                        it.copy(
                            selectedMonth = newMonth,
                            selectedYear = newYear
                        )
                    }
                    getMonthlyReport()
                } else {
                    viewModelScope.launch {
                        _event.send(ShowErrorToast("Thời gian vượt hiện tại"))
                    }
                }
            }


            ReportState.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(OnBack)
                }
            }

        }
    }

}

object ReportState {
    data class UiState(
        val monthlyReports: List<MonthlyReport> = emptyList(),
        val monthlyReportsState: MonthlyReportsState = MonthlyReportsState.Loading,
        val monthlyReportState: MonthlyReportState = MonthlyReportState.Loading,
        val fromMonth: Int,
        val fromYear: Int,
        val toMonth: Int,
        val toYear: Int,
        val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
        val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),


        )

    sealed interface MonthlyReportsState {
        data object Loading : MonthlyReportsState
        data class Error(val message: String) : MonthlyReportsState
        data object Success : MonthlyReportsState

    }

    sealed interface MonthlyReportState {
        data object Loading : MonthlyReportState
        data class Error(val message: String) : MonthlyReportState
        data class Success(val data: MonthlyReport) : MonthlyReportState
    }



    sealed interface Event {
        data class ShowErrorToast(val message: String) : Event
        data object OnBack: Event

    }

    sealed interface Action {
        data class OnChangeFromMonthYear(val year: Int, val month: Int) : Action
        data class OnChangeToMonthYear(val year: Int, val month: Int) : Action
        data class OnChangeSelectedMonthYear(val year: Int, val month: Int) : Action
        data object OnBack: Action


    }
}

fun adjustMonthYear(currentMonth: Int, currentYear: Int): Pair<Int, Int> {
    var newMonth = currentMonth
    var newYear = currentYear

    // Lùi hoặc tiến nhiều tháng
    while (newMonth < 1) {
        newMonth += 12
        newYear -= 1
    }

    while (newMonth > 12) {
        newMonth -= 12
        newYear += 1
    }

    return newMonth to newYear
}

fun isMonthYearBeforeOrEqual(y1: Int, m1: Int, y2: Int, m2: Int): Boolean {
    return y1 < y2 || (y1 == y2 && m1 <= m2)
}

fun isMonthYearAfterOrEqual(y1: Int, m1: Int, y2: Int, m2: Int): Boolean {
    return y1 > y2 || (y1 == y2 && m1 >= m2)
}