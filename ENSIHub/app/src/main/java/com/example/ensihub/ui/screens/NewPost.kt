package com.example.ensihub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ensihub.Post
import java.util.UUID
import com.example.ensihub.back.Feed


@Composable
fun NewPostView() {
    val messageState = remember { mutableStateOf("") }
    val feed = Feed()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = messageState.value,
            onValueChange = { messageState.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = { Text("Enter your message") },
            textStyle = MaterialTheme.typography.body1
        )

        Button(
            onClick = {
                val newPost = Post(
                    id = UUID.randomUUID().toString(),
                    text = messageState.value,
                    timestamp = System.currentTimeMillis(),
                    author = "John Doe",  // Replace with the actual author's name or fetch from user information
                    likesCount = 0  // Set the initial likes count as needed
                )
                feed.addPost(newPost)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Post")
        }
    }
}
