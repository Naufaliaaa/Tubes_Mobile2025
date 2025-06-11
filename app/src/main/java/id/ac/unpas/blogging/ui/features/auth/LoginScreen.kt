package id.ac.unpas.blogging.ui.features.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.unpas.blogging.ui.theme.BloggingTheme
import id.ac.unpas.blogging.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    val emailOrUsername = loginViewModel.emailOrUsername
    val password = loginViewModel.password
    val uiState by loginViewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            keyboardController?.hide()
            onNavigateToHome()
            loginViewModel.navigatedToHome()
        }
    }

    LaunchedEffect(uiState.loginError) {
        uiState.loginError?.let { error ->
            keyboardController?.hide()
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            loginViewModel.errorShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)

                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Blog App", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Login untuk melanjutkan", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = emailOrUsername,
                onValueChange = { loginViewModel.updateEmailOrUsername(it) },
                label = { Text("Email atau Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.loginError != null && emailOrUsername.isNotBlank()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { loginViewModel.updatePassword(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.loginError != null && password.isNotBlank()
            )
            Spacer(modifier = Modifier.height(32.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    keyboardController?.hide()
                    loginViewModel.attemptLogin()
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = !uiState.isLoading
            ) {
                Text("LOGIN")
            }
            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onNavigateToRegister,
                enabled = !uiState.isLoading
            ) {
                Text("Belum punya akun? Daftar di sini")
            }

            Spacer(modifier = Modifier.height(WindowInsets.ime.asPaddingValues().calculateBottomPadding()))
        }
    }
}

@Preview(showBackground = true, name = "Login Screen Full Preview")
@Composable
fun LoginScreenFullPreview() {
    BloggingTheme {
        LoginScreen(onNavigateToHome = {}, onNavigateToRegister = {})
    }
}

@Preview(showBackground = true, name = "Login Screen Loading Preview")
@Composable
fun LoginScreenLoadingPreview() {
    BloggingTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}