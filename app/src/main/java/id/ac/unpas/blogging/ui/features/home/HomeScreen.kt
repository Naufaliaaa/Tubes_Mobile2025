package id.ac.unpas.blogging.ui.features.home

import id.ac.unpas.blogging.ui.theme.BloggingTheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.unpas.blogging.data.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: List<Post>,
    onPostClick: (postId: String) -> Unit,
    onNavigateToCreatePost: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Blog Saya") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreatePost) {
                Icon(Icons.Filled.Add, contentDescription = "Buat Postingan Baru")
            }
        }
    ) { innerPadding ->
        if (posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada postingan. Yuk, buat yang pertama!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(posts, key = { it.id }) { post ->
                    PostItemCard(post = post, onClick = { onPostClick(post.id) })
                }
            }
        }
    }
}

@Composable
fun PostItemCard(post: Post, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = post.excerpt, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Oleh: ${post.author}", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BloggingTheme {
        val dummyPosts = listOf(
            Post("1", "Tutorial Jetpack Compose Keren", "Ini adalah cuplikan singkat dari tutorial Jetpack Compose yang sangat mudah diikuti...", "Andi Developer"),
            Post("2", "Tips Produktif Ngoding", "Beberapa tips agar ngodingmu makin produktif dan menyenangkan setiap hari.", "Budi Koder")
        )
        HomeScreen(posts = dummyPosts, onPostClick = {}, onNavigateToCreatePost = {})
    }
}