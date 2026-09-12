package io.bbs.seva.vbbs004mobile.presentation.screens.signup
// presentation/screens/signup/SignupScreen.kt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

//import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit,
    viewModel: SignupViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // Show snackbar for errors or success messages
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is SignupUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }

            is SignupUiState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                // If email confirmation is required, navigate back to login
                //onNavigateToLogin()
                onNavigateToHome()
                viewModel.resetState()
            }

            else -> Unit
        }
    }

    Scaffold(
        //Modifier.border(4.dp, Color.Green),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                //.border(4.dp,Color.Yellow)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(
                    top = 0.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp,
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium
                )

                OutlinedTextField(
                    value = viewModel.email.collectAsState().value,
                    onValueChange = { viewModel.email.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is SignupUiState.Loading,
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                )

                OutlinedTextField(
                    value = viewModel.password.collectAsState().value,
                    onValueChange = { viewModel.password.value = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    //visualTransformation = PasswordVisualTransformation(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                    ),
                    enabled = uiState !is SignupUiState.Loading,
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
                )

                OutlinedTextField(
                    value = viewModel.fullName.collectAsState().value,
                    onValueChange = { viewModel.fullName.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is SignupUiState.Loading,
                    label = { Text("Full Name (Optional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.PersonName,
                        imeAction = ImeAction.Next
                    ),
                )

                OutlinedTextField(
                    value = viewModel.age.collectAsState().value,
                    onValueChange = { viewModel.age.value = it.filter { char -> char.isDigit() } },
                    label = { Text("Age (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = uiState !is SignupUiState.Loading
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.signup() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = uiState !is SignupUiState.Loading
                ) {
                    if (uiState is SignupUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.height(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Sign Up")
                    }
                }

                TextButton(onClick = onNavigateToLogin) {
                    Text("Already have an account? Login")
                }
            }
        }
    }
}


/*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SignupScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
*/
