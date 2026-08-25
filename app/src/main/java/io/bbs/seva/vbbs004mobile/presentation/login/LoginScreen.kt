package io.bbs.seva.vbbs004mobile.presentation.login
// presentation/login/LoginScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Observe state flows safely from your Clean Architecture ViewModel
    val state by viewModel.uiState.collectAsState()

    // 2. Local state tracking for input inputs
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.imePadding(),
        topBar = { TopAppBar(title = { Text("Vbbs004 Mobile") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // 3. Email Input Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = state !is LoginUiState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Password Input Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = state !is LoginUiState.Loading
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Dynamic Error Display
            if (state is LoginUiState.Error) {
                Text(
                    text = (state as LoginUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // 6. Action Submission Button
            Button(
                onClick = { viewModel.login(email, password, onNavigateToHome) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = email.isNotBlank() && password.isNotBlank() && state !is LoginUiState.Loading
            ) {
                if (state is LoginUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), // Define size cleanly via layout modifier
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Secondary option link
            TextButton(
                onClick = { /* Proactively setup signup navigation later */ },
                enabled = state !is LoginUiState.Loading
            ) {
                Text("Don't have an account? Sign Up")
            }
        }
    }
}
