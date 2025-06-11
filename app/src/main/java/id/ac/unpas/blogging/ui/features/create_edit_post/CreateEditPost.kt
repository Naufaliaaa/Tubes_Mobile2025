package id.ac.unpas.blogging.ui.features.create_edit_post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.unpas.blogging.ui.theme.BloggingTheme
import id.ac.unpas.blogging.viewmodel.CreateEditPostViewModel
import androidx.compose.material3.TextFieldDefaults
import id.ac.unpas.blogging.ui.theme.White


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditPostScreen(
    createEditPostViewModel: CreateEditPostViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onPostSavedOrUpdated: (postId: String?) -> Unit
) {
    val uiState by createEditPostViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState.postSavedOrUpdated) {
        if (uiState.postSavedOrUpdated) {
            keyboardController?.hide()
            val savedPostId = if (uiState.isEditMode) createEditPostViewModel.uiState.value.title.hashCode().toString() else null // Ini cuma ID dummy sementara, idealnya dari respons backend/repo
            onPostSavedOrUpdated(savedPostId)
            createEditPostViewModel.navigationCompleted()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            keyboardController?.hide()
            snackbarHostState.showSnackbar(message = error, duration = SnackbarDuration.Short)
            createEditPostViewModel.errorShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(uiState.pageTitle) },
                navigationIcon = {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        onNavigateBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading && uiState.title.isBlank() && uiState.content.isBlank() && uiState.isEditMode) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { createEditPostViewModel.updateTitle(it) },
                    label = { Text("Judul Postingan") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleLarge,
                    isError = uiState.error != null && uiState.title.isBlank()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown State
                val categories = listOf("Teknologi", "Kesehatan", "Gaya Hidup", "Pendidikan")
                var expanded by remember { mutableStateOf(false) }
                var selectedCategory by remember { mutableStateOf(categories.first()) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kategori Postingan", color = Color.Black) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.LightGray,
                            unfocusedContainerColor =  Color.LightGray ,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.DarkGray,
                            unfocusedLabelColor = Color.Gray,
                            focusedIndicatorColor = Color.LightGray,
                            unfocusedIndicatorColor = Color.LightGray
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category, color = Color.Black) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = uiState.content,
                    onValueChange = { createEditPostViewModel.updateContent(it) },
                    label = { Text("Isi Postingan...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    isError = uiState.error != null && uiState.content.isBlank()
                )
                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            createEditPostViewModel.saveOrUpdatePost()
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        enabled = !uiState.isLoading
                    ) {
                        Text(if (uiState.isEditMode) "SIMPAN PERUBAHAN" else "PUBLIKASIKAN")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Create Post Preview")
@Composable
fun CreatePostScreenPreview() {
    BloggingTheme {
        CreateEditPostScreen(onNavigateBack = {}, onPostSavedOrUpdated = {})
    }
}