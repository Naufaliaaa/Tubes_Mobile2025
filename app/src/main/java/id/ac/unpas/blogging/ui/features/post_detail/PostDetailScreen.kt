package id.ac.unpas.blogging.ui.features.post_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.unpas.blogging.data.model.Comment
import id.ac.unpas.blogging.data.model.FullPost
import id.ac.unpas.blogging.ui.theme.BloggingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    post: FullPost?,
    comments: List<Comment>,
    isUserOwner: Boolean,
    onNavigateBack: () -> Unit,
    onEditPost: (postId: String) -> Unit,
    onDeletePost: (postId: String) -> Unit,
    onPostComment: (commentText: String) -> Unit
) {
    var newCommentText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(post?.title ?: "Detail Postingan", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    if (post != null && isUserOwner) {
                        TextButton(onClick = { onEditPost(post.id) }) {
                            Text("EDIT")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (post == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Atau pesan error
            }
            return@Scaffold
        }

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
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Text("Komentar (${comments.size})", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = newCommentText,
                        onValueChange = { newCommentText = it },
                        label = { Text("Tulis komentarmu...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (newCommentText.isNotBlank()) {
                                onPostComment(newCommentText)
                                newCommentText = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("KIRIM")
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Daftar Komentar
            if (comments.isEmpty()) {
                item {
                    Text("Belum ada komentar.", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                items(comments, key = { it.id }) { comment ->
                    CommentItemView(comment = comment)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
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

@Preview(showBackground = true)
@Composable
fun PostDetailScreenPreview() {
    BloggingTheme {
        val dummyPost = FullPost("1", "Judul Postingan Detail", "Ini adalah isi lengkap dari postingan yang sangat menarik dan informatif. Bisa berisi banyak paragraf dan penjelasan mendalam.", "Andi Developer", "29 Mei 2025", true)
        val dummyComments = listOf(
            Comment(
                "c1", "1", "Ajuy", "Artikelnya mantap, bro!", "29 Mei 2025"
            ),
            Comment(
                "c2", "1", "yuma", "Sangat membantu, terima kasih infonya.", "29 Mei 2025"
            )
        )
        PostDetailScreen(
            post = dummyPost,
            comments = dummyComments,
            isUserOwner = true,
            onNavigateBack = {},
            onEditPost = {},
            onDeletePost = {},
            onPostComment = {}
        )
    }
}