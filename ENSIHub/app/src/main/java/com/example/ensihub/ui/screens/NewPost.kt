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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ensihub.mainClasses.Post
import com.example.ensihub.mainClasses.FeedViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun NewPostView() {
    val messageState = remember { mutableStateOf("") }
    val viewModel: FeedViewModel = viewModel()
    val currentUser = FirebaseAuth.getInstance().currentUser // Get the current user

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
                if (currentUser != null) {
                    val newPost = Post(
                        text = messageState.value,
                        timestamp = System.currentTimeMillis(),
                        author = currentUser.displayName ?: "",  // Use the current user's display name
                        likesCount = 0  // Set the initial likes count as needed
                    )
                    viewModel.addPost(newPost)
                    messageState.value = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Post")
        }
    }
}
