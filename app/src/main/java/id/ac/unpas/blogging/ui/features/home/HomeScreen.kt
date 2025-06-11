package id.ac.unpas.blogging.ui.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.unpas.blogging.data.model.Post
import id.ac.unpas.blogging.ui.theme.BloggingTheme
import id.ac.unpas.blogging.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    onPostClick: (postId: String) -> Unit,
    onNavigateToCreatePost: () -> Unit
) {
    val uiState by homeViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.error != null) {
            // ... (tampilan error)
        } else if (uiState.posts.isEmpty()) {
            // ... (tampilan kosong)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.posts, key = { it.id }) { post ->
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
            Text(text = post.excerpt, style = MaterialTheme.typography.bodySmall, maxLines = 3, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Oleh: ${post.author}", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenWithViewModelPreview() {
    BloggingTheme {
        HomeScreen(onPostClick = {}, onNavigateToCreatePost = {})
    }
}

@Preview(showBackground = true, name = "Home Screen Empty Preview")
@Composable
fun HomeScreenEmptyPreview() {
    val emptyViewModel = HomeViewModel()

    BloggingTheme {
        HomeScreen(
            homeViewModel = emptyViewModel,
            onPostClick = {},
            onNavigateToCreatePost = {}
        )
    }
}