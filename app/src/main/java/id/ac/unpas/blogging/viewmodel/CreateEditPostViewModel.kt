package id.ac.unpas.blogging.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateEditPostUiState(
    val title: String = "",
    val content: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditMode: Boolean = false,
    val postSavedOrUpdated: Boolean = false,
    val pageTitle: String = "Buat Postingan Baru"
)

class CreateEditPostViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEditPostUiState())
    val uiState: StateFlow<CreateEditPostUiState> = _uiState.asStateFlow()

    private val postId: String? = savedStateHandle["postId"]

    init {
        if (postId != null) {
            _uiState.value = _uiState.value.copy(
                isEditMode = true,
                pageTitle = "Edit Postingan",
                isLoading = true
            )
            loadPostDataForEdit(postId)
        }
    }

    private fun loadPostDataForEdit(currentPostId: String) {
        viewModelScope.launch {
            delay(500)
            _uiState.value = _uiState.value.copy(
                title = "Judul Post $currentPostId (Dari ViewModel)",
                content = "Ini adalah konten lama dari post $currentPostId yang dimuat oleh ViewModel untuk diedit.",
                isLoading = false
            )
        }
    }

    fun updateTitle(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle, error = null)
    }

    fun updateContent(newContent: String) {
        _uiState.value = _uiState.value.copy(content = newContent, error = null)
    }

    fun saveOrUpdatePost() {
        val currentState = _uiState.value
        if (currentState.title.isBlank() || currentState.content.isBlank()) {
            _uiState.value = currentState.copy(error = "Judul dan Isi postingan tidak boleh kosong.")
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, error = null)
            delay(2000)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                postSavedOrUpdated = true
            )
        }
    }

    fun navigationCompleted() {
        _uiState.value = _uiState.value.copy(postSavedOrUpdated = false)
    }

    fun errorShown() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
