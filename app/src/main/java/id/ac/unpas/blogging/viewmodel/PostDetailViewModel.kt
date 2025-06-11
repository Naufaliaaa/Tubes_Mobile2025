package id.ac.unpas.blogging.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.unpas.blogging.data.model.Comment
import id.ac.unpas.blogging.data.model.FullPost
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PostDetailUiState(
    val post: FullPost? = null,
    val comments: List<Comment> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val newCommentText: String = "",
    val isCommentPosting: Boolean = false,
    val commentPostError: String? = null,
    val isUserOwner: Boolean = false
)

class PostDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    private val postId: String = checkNotNull(savedStateHandle["postId"])

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch {
            _uiState.value = PostDetailUiState(isLoading = true)
            delay(1000)

            val dummyFullPost = FullPost(
                id = postId,
                title = "Detail Postingan ID: $postId",
                content = "Ini adalah isi lengkap dari postingan dengan ID $postId. Konten ini diambil dari ViewModel. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                author = "Penulis ViewModel $postId",
                date = " 30 Mei 2025"
            )
            val dummyComments = listOf(
                Comment("c1_vm", postId, "User A (VM)", "Komentar dari ViewModel untuk post $postId.", "30 Mei 2025"),
                Comment("c2_vm", postId, "User B (VM)", "Mantap, infonya berguna!", "30 Mei 2025")
            )

            val isOwner = postId == "1"

            _uiState.value = _uiState.value.copy(
                post = dummyFullPost,
                comments = dummyComments,
                isLoading = false,
                isUserOwner = isOwner
            )
        }
    }

    fun updateNewCommentText(text: String) {
        _uiState.value = _uiState.value.copy(newCommentText = text, commentPostError = null)
    }

    fun postComment() {
        if (_uiState.value.newCommentText.isBlank()) {
            _uiState.value = _uiState.value.copy(commentPostError = "Komentar tidak boleh kosong.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCommentPosting = true, commentPostError = null)
            delay(1500)

            val newComment = Comment(
                id = "c_new_${System.currentTimeMillis()}",
                postId = postId,
                author = "User Sekarang (VM)",
                text = _uiState.value.newCommentText,
                date = "Baru saja"
            )

            val updatedComments = _uiState.value.comments + newComment

            _uiState.value = _uiState.value.copy(
                comments = updatedComments,
                newCommentText = "",
                isCommentPosting = false
            )
        }
    }

    fun errorCommentShown() {
        _uiState.value = _uiState.value.copy(commentPostError = null)
    }

    fun refreshContent() {
        loadContent()
    }

    fun deletePost(onSuccess: () -> Unit) {
        viewModelScope.launch {
            println("ViewModel: Menghapus post $postId...")
            delay(1000)
            println("ViewModel: Post $postId berhasil dihapus (simulasi).")
            onSuccess()
        }
    }
}