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

@Composable
fun NewPostView() {
    val messageState = remember { mutableStateOf("") }

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
                // Open the Post screen actualizing the post database
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Post")
        }
    }
}

@Preview
@Composable
fun NewPostViewPreview() {
    NewPostView()
}
