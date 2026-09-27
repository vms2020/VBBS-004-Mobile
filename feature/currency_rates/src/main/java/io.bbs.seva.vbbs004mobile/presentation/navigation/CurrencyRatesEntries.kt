package io.bbs.seva.vbbs004mobile.presentation.navigation

// feature/currency_rates/src/main/java/io.bbs.seva.vbbs004mobile/presentation/navigation/CurrencyRatesEntries.kt

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.bbs.seva.vbbs004mobile.core.designsystem.topbar.LocalTopBarController
import io.bbs.seva.vbbs004mobile.core.designsystem.topbar.TopBarAction
import io.bbs.seva.vbbs004mobile.core.designsystem.topbar.TopBarState
import io.bbs.seva.vbbs004mobile.feature.currency_rates.R
import io.bbs.seva.vbbs004mobile.presentation.screens.currency_rates.CurrencyDynamicsScreen
import io.bbs.seva.vbbs004mobile.presentation.screens.currency_rates.CurrencyDynamicsViewModel
import io.bbs.seva.vbbs004mobile.presentation.screens.currency_rates.CurrencyRatesScreen
import io.bbs.seva.vbbs004mobile.presentation.screens.currency_rates.CurrencyRatesViewModel
import java.time.format.DateTimeFormatter

fun EntryProviderScope<NavKey>.currencyRatesEntryBuilder(navigator: AppNavigator) {
    entry<Destination.CurrencyRates> {
        val viewModel: CurrencyRatesViewModel = hiltViewModel()
        val topBar = LocalTopBarController.current
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        val titleText = state.dailyRates?.date
            ?.let { date ->
                stringResource(
                    R.string.currency_rates_updated_on,
                    date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                )
            }
            ?: stringResource(R.string.currency_rates_title)

        val actionDesc = stringResource(R.string.currency_rates_select_date)
        val actionDescShowLatest = stringResource(R.string.currency_rates_show_latest)

        SideEffect {
            topBar?.set(
                TopBarState(
                    title = titleText,
                    actions =
                        buildList {

                            add(
                                TopBarAction.MyTopBarActionIconButton(
                                    icon = Icons.Default.DateRange,
                                    contentDescription = actionDesc,
                                    onClick = { viewModel.requestDatePicker() },
                                )
                            )

                            if (state.isFilteredByDate) {
                                add(
                                    TopBarAction.MyTopBarActionIconButton(
                                        icon = Icons.Default.Close,
                                        contentDescription = actionDescShowLatest,
                                        onClick = { viewModel.onDateSelected(null) },
                                    )
                                )
                            }
                        },
//                        listOf(
//                        TopBarAction.MyTopBarActionIconButton(
//                            icon = Icons.Default.DateRange,
//                            contentDescription = actionDesc,
//                            onClick = { viewModel.requestDatePicker() },
//                        ),
//                    )
                ),
                owner = Destination.CurrencyRates,
            )
        }
        DisposableEffect(Unit) {
            onDispose {
                topBar?.clear(Destination.CurrencyRates)
            }
        }

        CurrencyRatesScreen(
            viewModel = viewModel,
            onRateClick = { rate ->
                navigator.navigate(
                    Destination.CurrencyDynamics(
                        rate.id, rate.charCode, rate.name
                    )
                )
            },
        )
    }
    entry<Destination.CurrencyDynamics> { key ->
        val viewModel: CurrencyDynamicsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val topBar = LocalTopBarController.current
        val dynamicsTitle = stringResource(R.string.currency_dynamics_title)
        val pickInterval = stringResource(R.string.currency_dynamics_pick_interval)
        val shortDate = remember { DateTimeFormatter.ofPattern("dd.MM") }

        val titleString = "${key.charCode} " +
                            "(${state.from.format(shortDate)}–${state.to.format(shortDate)})"

        SideEffect {
            topBar?.set(
                TopBarState(
                    title = titleString,
                        //"${key.charCode} " +
                        //    "(${state.from.format(shortDate)}–${state.to.format(shortDate)})",
                    actions = listOf(
                        TopBarAction.MyTopBarActionIconButton(
                            icon = Icons.Default.DateRange,
                            contentDescription = pickInterval,
                            onClick = { viewModel.requestIntervalPicker() },
                        ),
                    ),
                ),
                owner = key,
            )
        }
        DisposableEffect(Unit) {
            onDispose { topBar?.clear(key) }
        }

        CurrencyDynamicsScreen(
            viewModel = viewModel,
            currencyId = key.currencyId,
            charCode = key.charCode,
            name = key.name,
        )
    }
}
