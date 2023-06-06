package com.example.ensihub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ensihub.ui.screens.PostView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a sample Post object
        val post = Post(
            id = "123",
            text = "Yo la street.",
            timestamp = System.currentTimeMillis(),
            author = "Marian chef eco-conception",
            likesCount = 5
        )

        // Call the PostView function to display the Post preview
        setContent {
            PostView(post)
        }
    }
}

@Preview
@Composable
fun PostViewPreview() {
    val post = Post(
        id = "123",
        text = "Yo la street.",
        timestamp = System.currentTimeMillis(),
        author = "Marian chef eco-conception",
        likesCount = 5
    )
    PostView(post)
}
