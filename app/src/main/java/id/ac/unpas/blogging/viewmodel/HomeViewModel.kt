package id.ac.unpas.blogging.viewmodel

import androidx.lifecycle.ViewModel
import id.ac.unpas.blogging.data.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Dummy posts dipisah agar tidak diinisialisasi ulang
    private val dummyPosts = listOf(
        Post("1", "Tutorial Jetpack Compose Keren", "Ini adalah cuplikan singkat dari tutorial Jetpack Compose yang sangat mudah diikuti...", "Andi Developer"),
        Post("2", "Tips Produktif Ngoding", "Beberapa tips agar ngodingmu makin produktif dan menyenangkan setiap hari.", "Budi Koder"),
        Post("3", "Belajar Kotlin dari Dasar", "Panduan lengkap untuk pemula yang ingin menguasai bahasa Kotlin.", "Citra Programmer"),
        Post("4", "Apa itu API?", "Penjelasan sederhana tentang API dan kegunaannya dalam pengembangan software.", "Dani SysAdmin"),
        Post("5", "Review Laptop Gaming Terbaru", "Kupas tuntas performa dan fitur laptop gaming idaman para gamer.", "Eva TechReviewer")
    )

    init {
        loadPosts()
    }

    private fun loadPosts() {
        _uiState.value = _uiState.value.copy(posts = dummyPosts, isLoading = false)
    }

    fun refreshPosts() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        loadPosts()
    }
}
