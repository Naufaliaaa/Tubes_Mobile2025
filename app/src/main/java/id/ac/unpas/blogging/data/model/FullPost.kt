package id.ac.unpas.blogging.data.model

data class FullPost(
    val id: String,
    val title: String,
    val content: String,
    val author: String,
    val date: String,
    val bool: Boolean
)