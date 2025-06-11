package id.ac.unpas.blogging.viewmodel



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val loginError: String? = null,
    val loginSuccess: Boolean = false
)

class LoginViewModel : ViewModel() {

    var emailOrUsername by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmailOrUsername(input: String) {
        emailOrUsername = input
        if (_uiState.value.loginError != null) {
            _uiState.value = _uiState.value.copy(loginError = null)
        }
    }

    fun updatePassword(input: String) {
        password = input
        if (_uiState.value.loginError != null) {
            _uiState.value = _uiState.value.copy(loginError = null)
        }
    }

    fun attemptLogin() {
        if (emailOrUsername.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(loginError = "Email/Username dan Password tidak boleh kosong!")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true, loginError = null)
            delay(2000)

            if (emailOrUsername == "user" && password == "pass") {
                _uiState.value = LoginUiState(isLoading = false, loginSuccess = true)
            } else {
                _uiState.value = LoginUiState(isLoading = false, loginError = "Email/Username atau Password salah.")
            }
        }
    }

    fun errorShown() {
        _uiState.value = _uiState.value.copy(loginError = null)
    }

    fun navigatedToHome() {
        _uiState.value = LoginUiState(loginSuccess = false)
    }
}