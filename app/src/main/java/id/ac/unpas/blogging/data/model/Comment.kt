package id.ac.unpas.blogging.data.model

data class Comment(
    val id: String,
    val postId: String,
    val author: String,
    val text: String,
    val date: String
)