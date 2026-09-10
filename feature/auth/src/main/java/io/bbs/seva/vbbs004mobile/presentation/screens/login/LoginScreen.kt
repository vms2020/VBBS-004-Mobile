package io.bbs.seva.vbbs004mobile.presentation.screens.login
// presentation/login/LoginScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToSignup: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            //.imePadding()
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

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            enabled = state !is LoginUiState.Loading,
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(
                        if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        "Visibility"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = state !is LoginUiState.Loading
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state is LoginUiState.Error) {
            Text(
                text = (state as LoginUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

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

        TextButton(
            onClick = onNavigateToSignup,
            enabled = state !is LoginUiState.Loading
        ) {
            Text("Don't have an account? Sign Up")
        }
    }
}
