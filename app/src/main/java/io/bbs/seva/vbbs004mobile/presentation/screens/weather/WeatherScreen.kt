package io.bbs.seva.vbbs004mobile.presentation.screens.weather
// presentation/screens/weather/WeatherScreen.kt

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import io.bbs.seva.vbbs004mobile.domain.model.weather.Weather
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatWeatherTime(epochSecond: Long?): String {
    if (epochSecond == null) return ""
    return try {
        val formatter = DateTimeFormatter.ofPattern(
            "EEEE, h:mm a",
            Locale.getDefault()
        )
        Instant.ofEpochSecond(epochSecond)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
    } catch (e: Exception) {
        Log.e("FORMATWEATHER", "formatWeatherTime: ", e)
        ""
    }
}

/*

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()


    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = { viewModel.fetchWeather() },
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center,
        ) {
            when {
                state.isLoading -> {}//CircularProgressIndicator()

                state.error != null -> {
                    Text(text = "Error: ${state.error}", color = MaterialTheme.colorScheme.error)
                }

                state.weather != null -> {
                    Column {
                        if (state.weather?.current?.time != null) {
                            Text(
                                Instant.ofEpochSecond(state.weather?.current?.time ?: 0)
                                    .atZone(ZoneId.systemDefault()).toString()
                                //state.weather?.zonedDateTime?:""
                            )
                        }
                        if (state.weather?.current?.icon != null) {
                            AsyncImage(
                                state.weather?.current?.icon,
                                state.weather?.current?.condition
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Filled.DeviceThermostat,
                                "temperature"
                            )
                            Text(
                                text = ": ${state.weather?.current?.temperature}°C",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        Text(
                            text = "Condition: ${state.weather?.current?.condition}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

*/


@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = { viewModel.fetchWeather() },
        modifier = modifier.fillMaxSize(),
    ) {
        when {
            // Error View
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Success View with Scrollable Content
            state.weather != null -> {
                val dashboard = state.weather!!

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Current Weather Header Item
                    item {
                        CurrentWeatherHeaderCard(weather = dashboard.current)
                    }

                    // 2. Forecast Section Header
                    item {
                        Text(
                            text = "Forecast Lookahead",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }

                    // 3. Scrollable List of Forecast Rows
                    items(dashboard.forecast.items) { forecastItem ->
                        ForecastItemRow(weather = forecastItem)
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentWeatherHeaderCard(weather: Weather, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(weather.cityName!=null){
                Text(
                    text = weather.cityName!!,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }

            if (weather.time != null) {
                Text(
                    text = formatWeatherTime(weather.time),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (weather.icon != null) {
                AsyncImage(
                    model = weather.icon,
                    contentDescription = weather.condition,
                    modifier = Modifier.size(100.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.DeviceThermostat,
                    contentDescription = "Temperature Metric",
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = "${weather.temperature}°C",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = weather.condition,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ForecastItemRow(weather: Weather, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formatWeatherTime(weather.time),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = weather.condition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            if (weather.icon != null) {
                Box(
                   modifier = Modifier.size(48.dp)
                       .clip(RoundedCornerShape(8.dp))
                       .background(Color.LightGray)
                    ,
                ) {
                    AsyncImage(
                        model = weather.icon,
                        contentDescription = weather.condition,
                        //modifier = Modifier.size(48.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(start = 8.dp).weight(.5f)
            ) {
                Icon(
                    imageVector = Icons.Filled.DeviceThermostat,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${weather.temperature}°C",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

