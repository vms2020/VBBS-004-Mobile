package io.bbs.seva.vbbs004mobile.presentation.screens.settings

import android.R.attr.enabled
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Http
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Router
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Application Settings") }
//            )
//        },
//        modifier = modifier.fillMaxSize()
//    ) { innerPadding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            //.padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Section 1: Network Connection Endpoint ---
        Text(
            text = "Network Target",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        OutlinedTextField(
            value = uiState.baseUrl,
            onValueChange = viewModel::onBaseUrlChanged,
            label = { Text("Server Base URL") },
            placeholder = { Text("https://example.com") },
            leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Section 2: Debug Proxy Configurations ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Use Debug Proxy",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Route background traffic through a custom proxy server",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = uiState.isProxyEnabled,
                        onCheckedChange = viewModel::onProxyToggleChanged
                    )
                }

                // Smooth transition to show proxy input fields only when enabled
                AnimatedVisibility(
                    visible = uiState.isProxyEnabled,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Protocol Dropdown Selector (HTTP vs SOCKS)
                        var dropdownExpanded by remember { mutableStateOf(false) }
                        val protocols = listOf("SOCKS", "HTTP")

                        ExposedDropdownMenuBox(
                            expanded = dropdownExpanded,
                            onExpandedChange = { dropdownExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = uiState.proxyProtocol,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Proxy Protocol") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Http,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenuBoxScope@ ExposedDropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false }
                            ) {
                                protocols.forEach { protocol ->
                                    DropdownMenuItem(
                                        text = { Text(protocol) },
                                        onClick = {
                                            viewModel.onProxyProtocolChanged(protocol)
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Host Address Field
                        OutlinedTextField(
                            value = uiState.proxyHost,
                            onValueChange = viewModel::onProxyHostChanged,
                            label = { Text("Proxy Host Address") },
                            placeholder = { Text("127.0.0.1") },
                            leadingIcon = { Icon(Icons.Default.Router, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            )
                        )

                        // Port Field
                        OutlinedTextField(
                            value = uiState.proxyPort,
                            onValueChange = viewModel::onProxyPortChanged,
                            label = { Text("Proxy Port") },
                            placeholder = { Text("9150") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.NetworkCheck,
                                    contentDescription = null
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

// --- Section 3: Action & Success States ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.End
        ) {
            if (uiState.saveSuccess) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    //modifier = Modifier.padding(end = 16.dp)
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Saved successfully!",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Spacer(Modifier.weight(1f))
            }
            Button(
                onClick = viewModel::saveSettings,
                enabled = !uiState.isSaving,
                //modifier = Modifier.fillMaxWidth(if (uiState.saveSuccess) 0.5f else 1f)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                } else {
                    Text(text = "Save Configurations")
                }
            }
        }
    }
    //}
}
