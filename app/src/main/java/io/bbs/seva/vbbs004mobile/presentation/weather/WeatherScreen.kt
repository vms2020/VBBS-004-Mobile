package io.bbs.seva.vbbs004mobile.presentation.weather
// presentation/weather/WeatherScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

//import androidx.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Vbbs004 Home") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                },
            )
        },
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()

                state.error != null -> {
                    Text(text = "Error: ${state.error}", color = MaterialTheme.colorScheme.error)
                }

                state.weather != null -> {
                    Column {
                        Text(
                            text = "Current Temperature: ${state.weather?.temperature}°C",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "Condition: ${state.weather?.condition}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }

}