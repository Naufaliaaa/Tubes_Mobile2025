package id.ac.unpas.blogging.ui.features.post_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete // Import icon delete
import androidx.compose.material.icons.filled.Edit // Import icon edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // Penting
import id.ac.unpas.blogging.data.model.Comment // Model tetap diimport jika dipakai di preview
import id.ac.unpas.blogging.data.model.FullPost // Model tetap diimport jika dipakai di preview
import id.ac.unpas.blogging.ui.theme.BloggingTheme // Sesuaikan
import id.ac.unpas.blogging.viewmodel.PostDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postDetailViewModel: PostDetailViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToEditPost: (postId: String) -> Unit
) {
    val uiState by postDetailViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.commentPostError) {
        uiState.commentPostError?.let { error ->
            keyboardController?.hide()
            snackbarHostState.showSnackbar(message = error, duration = SnackbarDuration.Short)
            postDetailViewModel.errorCommentShown()
        }
    }

    if (showDeleteConfirmDialog && uiState.post != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus postingan \"${uiState.post?.title}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        postDetailViewModel.deletePost {
                            onNavigateBack()
                        }
                        showDeleteConfirmDialog = false
                    }
                ) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(uiState.post?.title ?: "Detail Postingan", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    if (uiState.post != null && uiState.isUserOwner) {
                        IconButton(onClick = { onNavigateToEditPost(uiState.post!!.id) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit Postingan")
                        }
                        IconButton(onClick = { showDeleteConfirmDialog = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Hapus Postingan")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            }
        } else if (uiState.post == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Postingan tidak ditemukan.")
            }
        } else {
            val post = uiState.post!!
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Text(text = post.title, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Oleh: ${post.author} • ${post.date}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = post.content, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text("Komentar (${uiState.comments.size})", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = uiState.newCommentText,
                        onValueChange = { postDetailViewModel.updateNewCommentText(it) },
                        label = { Text("Tulis komentarmu...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        isError = uiState.commentPostError != null
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (uiState.isCommentPosting) {
                            CircularProgressIndicator(modifier = Modifier.size(36.dp)) // Ukuran disesuaikan Button
                        } else {
                            Button(
                                onClick = {
                                    keyboardController?.hide()
                                    postDetailViewModel.postComment()
                                },
                                enabled = !uiState.isCommentPosting
                            ) {
                                Text("KIRIM")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (uiState.comments.isEmpty()) {
                    item {
                        Text("Belum ada komentar.", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    items(uiState.comments, key = { it.id }) { comment ->
                        CommentItemView(comment = comment)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CommentItemView(comment: Comment) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = comment.author, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(text = comment.text, style = MaterialTheme.typography.bodyMedium)
        Text(text = comment.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
    }
}

@Preview(showBackground = true, name = "Post Detail Preview (Static)")
@Composable
fun PostDetailScreenStaticPreview() {
    BloggingTheme {
        val dummyPost = FullPost("prev1", "Judul Preview", "Konten preview...", "Author Preview", "30 Mei 2025")
        val dummyComments = listOf(Comment("pc1", "prev1", "User Preview", "Komentar Preview.", "30 Mei 2025"))

        PostDetailScreen(
            onNavigateBack = {},
            onNavigateToEditPost = {}
        )
    }
}