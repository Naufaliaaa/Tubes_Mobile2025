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
        } else {
            _uiState.value = _uiState.value.copy(
                isEditMode = false,
                pageTitle = "Buat Postingan Baru",
                isLoading = false
            )
        }
    }

    private fun loadPostDataForEdit(currentPostId: String) {
        viewModelScope.launch {
            delay(500)

            val dummyExistingTitle = "Judul Post $currentPostId (Dari ViewModel)"
            val dummyExistingContent = "Ini adalah konten lama dari post $currentPostId yang dimuat oleh ViewModel untuk diedit."

            _uiState.value = _uiState.value.copy(
                title = dummyExistingTitle,
                content = dummyExistingContent,
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
        if (_uiState.value.title.isBlank() || _uiState.value.content.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Judul dan Isi postingan tidak boleh kosong.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            delay(2000)

            if (_uiState.value.isEditMode) {
                println("ViewModel: Mengupdate post ID $postId dengan judul '${_uiState.value.title}'")
            } else {
                println("ViewModel: Menyimpan post baru dengan judul '${_uiState.value.title}'")
            }
            _uiState.value = _uiState.value.copy(isLoading = false, postSavedOrUpdated = true)
        }
    }

    fun navigationCompleted() {
        _uiState.value = _uiState.value.copy(postSavedOrUpdated = false)
    }

    fun errorShown() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}