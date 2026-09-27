package io.bbs.seva.vbbs004mobile.presentation.screens.currency_rates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bbs.seva.vbbs004mobile.domain.model.AppError
import io.bbs.seva.vbbs004mobile.domain.model.CurrencyRatePoint
import io.bbs.seva.vbbs004mobile.feature.currency_rates.R
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun CurrencyDynamicsScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrencyDynamicsViewModel = hiltViewModel(),
    currencyId: String,
    charCode: String,
    name: String,
) {
    //val viewModel: CurrencyDynamicsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(currencyId) {
        viewModel.start(currencyId, charCode, name)
    }

    var showIntervalPicker by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.openIntervalPicker.collect { showIntervalPicker = true }
    }

    if (showIntervalPicker) {
        IntervalPickerDialog(
            initialFrom = state.from,
            initialTo = state.to,
            onConfirm = { from, to ->
                showIntervalPicker = false
                viewModel.onIntervalSelected(from, to)
            },
            onDismiss = { showIntervalPicker = false },
        )
    }
    Box(modifier = modifier.fillMaxSize()) {
        when {
            // ── full-screen loading (first entry, retry) ──
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            // ── error: typed → localized string + Retry ──
            state.error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = when (state.error) {
                                AppError.Network -> stringResource(R.string.currency_rates_error_load)
                                AppError.Unknown -> stringResource(R.string.currency_rates_error_load)
                                AppError.Server -> stringResource(R.string.currency_rates_error_server)
                                null -> ""
                            },
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(24.dp),
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = viewModel::retry) {
                            Text(stringResource(R.string.currency_rates_retry))
                        }
                    }
                }
            }

            // ── content: pull-to-refresh wraps the list ──
            else -> PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = viewModel::refresh,
                modifier = Modifier.fillMaxSize(),
            ) {
                if (state.points.isEmpty()) {
                    // range resolved but converter had no data (holidays, fresh currency, etc.)
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(R.string.currency_dynamics_empty),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(24.dp),
                        )
                    }
                } else {
                    DynamicsList(
                        charCode = state.charCode,
                        name = state.name,
                        points = state.points,
                    )
                }
            }
        }
    }
}

// ─────────────────────────── content ───────────────────────────

@Composable
private fun DynamicsList(charCode: String, name: String, points: List<CurrencyRatePoint>) {
    val dateFmt = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    val stats = remember(points) {
        if (points.isEmpty()) null
        else {
            val values = points.map { it.perUnit }
            Stats(
                max = values.max(),
                min = values.min(),
                avg = values.reduce { a, b -> a + b } / BigDecimal(values.size),
                maxDate = points.maxBy { it.perUnit }.date,
                minDate = points.minBy { it.perUnit }.date,
            )
        }
    }
    //Column(Modifier.fillMaxSize()) {

        // summary line: first → last over the window
//        Text(
//            text = "$charCode — ${points.first().perUnit.toPlainString()} → " +
//                    points.last().perUnit.toPlainString(),
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier.padding(16.dp),
//        )
//        HorizontalDivider()

        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
            item {
                stats?.let { s ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        StatCell("Max", s.max, s.maxDate, dateFmt)
                        StatCell("Min", s.min, s.minDate, dateFmt)
                        StatCell("Avg", s.avg, null, dateFmt)
                    }
                    HorizontalDivider()
                }
            }
            items(points) { point ->
                DynamicsRow(point, dateFmt)
                HorizontalDivider()
            }
        }
    //}
}

private data class Stats(
    val max: BigDecimal,
    val min: BigDecimal,
    val avg: BigDecimal,
    val maxDate: LocalDate,
    val minDate: LocalDate,
)

@Composable
private fun StatCell(
    label: String,
    value: BigDecimal,
    date: LocalDate?,
    dateFmt: DateTimeFormatter
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value.toPlainString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        date?.let {
            Text(
                it.format(dateFmt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DynamicsRow(point: CurrencyRatePoint, dateFmt: DateTimeFormatter) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Text(
            text = point.date.format(dateFmt),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = point.perUnit.toPlainString(),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntervalPickerDialog(
    initialFrom: LocalDate,
    initialTo: LocalDate,
    onConfirm: (LocalDate, LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialFrom.atStartOfDay(ZoneOffset.UTC).toInstant()
            .toEpochMilli(),
        initialSelectedEndDateMillis = initialTo.atStartOfDay(ZoneOffset.UTC).toInstant()
            .toEpochMilli(),
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = pickerState.selectedStartDateMillis != null &&
                        pickerState.selectedEndDateMillis != null,
                onClick = {
                    val from = pickerState.selectedStartDateMillis!!.let {
                        Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    val to = pickerState.selectedEndDateMillis!!.let {
                        Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    onConfirm(minOf(from, to), maxOf(from, to))
                },
            ) {
                Text(stringResource(R.string.currency_dynamics_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.currency_rates_cancel))
            }
        },

        ) {
        DateRangePicker(
            state = pickerState,
            showModeToggle = false,
            title = {
                Text(
                    stringResource(R.string.currency_dynamics_pick_interval),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                )
            },
        )
    }
}
