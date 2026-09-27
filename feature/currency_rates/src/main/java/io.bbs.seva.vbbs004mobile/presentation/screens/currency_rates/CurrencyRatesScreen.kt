package io.bbs.seva.vbbs004mobile.presentation.screens.currency_rates

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bbs.seva.vbbs004mobile.domain.model.AppError
import io.bbs.seva.vbbs004mobile.domain.model.CurrencyRate
import io.bbs.seva.vbbs004mobile.feature.currency_rates.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
fun CurrencyRatesScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrencyRatesViewModel = hiltViewModel(),
    onRateClick: (CurrencyRate) -> Unit,
) {
    //val viewModel: CurrencyRatesViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.openDatePicker.collect { showDatePicker = true }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.dailyRates?.date
                ?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onDateSelected(
                            Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        )
                    }
                }) { Text(stringResource(R.string.currency_rates_ok)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) { Text(stringResource(R.string.currency_rates_cancel)) }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        when {
            state.isLoading -> FullScreenLoading()

            state.error != null -> ErrorContent(
                error = state.error,
                onRetry = viewModel::retry,
            )

            else -> RatesContent(
                state = state,
                onRefresh = viewModel::refresh,
                onDateSelected = viewModel::onDateSelected,
                onRateClick = onRateClick,
            )
        }
    }
}

// ---------- loading ----------

@Composable
private fun FullScreenLoading() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

// ---------- error ----------

@Composable
private fun ErrorContent(
    error: AppError?,
    onRetry: () -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = when (error) {
                    AppError.Network,
                    AppError.Unknown,
                        -> stringResource(R.string.currency_rates_error_load)

                    AppError.Server -> stringResource(R.string.currency_rates_error_server)
                    null -> ""
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(stringResource(R.string.currency_rates_retry))
            }
        }
    }
}

// ---------- content ----------

@Composable
private fun RatesContent(
    state: CurrencyRatesUiState,
    onRefresh: () -> Unit,
    onDateSelected: (LocalDate?) -> Unit,
    onRateClick: (CurrencyRate) -> Unit,

    ) {
    val daily = state.dailyRates

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(Modifier.fillMaxSize()) {

//            daily?.date?.let { date ->
//                Text(
//                    text = stringResource(
//                        R.string.currency_rates_updated_on,
//                        date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
//                    ),
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(16.dp),
//                )
//                HorizontalDivider()
//            }
//            if (state.isFilteredByDate) {
//                AssistChip(
//                    onClick = { onDateSelected(null) },
//                    label = { Text(stringResource(R.string.currency_rates_show_latest)) },
//                    leadingIcon = { Icon(Icons.Default.Close, contentDescription = null) },
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                )
//            }
            LazyColumn(Modifier.fillMaxSize()) {
                items(daily?.rates ?: emptyList()) { rate ->
                    RateRow(rate, onClick = { onRateClick(rate) })
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun RateRow(rate: CurrencyRate, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                rate.charCode,
                Modifier.clickable { onClick() },
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                rate.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = rate.perUnit.toPlainString(),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
