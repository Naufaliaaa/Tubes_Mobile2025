package id.ac.unpas.blogging.ui.features.create_edit_post

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.unpas.blogging.ui.theme.BloggingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditPostScreen(
    initialTitle: String = "",
    initialContent: String = "",
    isEditMode: Boolean,
    onSavePost: (title: String, content: String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var content by remember { mutableStateOf(initialContent) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Postingan" else "Buat Postingan Baru") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Postingan") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Isi Postingan...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onSavePost(title, content)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(if (isEditMode) "SIMPAN PERUBAHAN" else "PUBLIKASIKAN")
            }
        }
    }
}

@Preview(showBackground = true, name = "Create Mode")
@Composable
fun CreatePostScreenPreview() {
    BloggingTheme {
        CreateEditPostScreen(
            isEditMode = false,
            onSavePost = { _, _ -> },
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Edit Mode")
@Composable
fun EditPostScreenPreview() {
    BloggingTheme {
        CreateEditPostScreen(
            initialTitle = "Judul Lama yang Diedit",
            initialContent = "Ini adalah konten lama yang mau diperbarui...",
            isEditMode = true,
            onSavePost = { _, _ -> },
            onNavigateBack = {}
        )
    }
}