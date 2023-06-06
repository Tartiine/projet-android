package com.example.ensihub.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp

// Sample Post data class
data class Post(val username: String, val content: String)

@Preview
@Composable
fun PostView(@PreviewParameter(PostProvider::class) post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = post.username,
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = post.content,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

class PostProvider : PreviewParameterProvider<Post> {
    override val values: Sequence<Post> = sequenceOf(
        Post("John Doe", "This is a sample post."),
        Post("Jane Smith", "Another post for preview.")
    )
}
