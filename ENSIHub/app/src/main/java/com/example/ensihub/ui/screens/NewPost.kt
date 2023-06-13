package com.example.ensihub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ensihub.mainClasses.Post
import com.example.ensihub.mainClasses.FeedViewModel

@Composable
fun NewPostView(navController: NavController) {
    val messageState = remember { mutableStateOf("") }
    val imageUrlState = remember { mutableStateOf("") }
    val viewModel: FeedViewModel = viewModel()
    val currentUser = viewModel.currentUser.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(color = Color(0xFF000000))
    ) {
        TextField(
            value = messageState.value,
            onValueChange = { messageState.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            label = { Text("Enter your message") },
            textStyle = MaterialTheme.typography.body1,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
        )

        Button(
            onClick = {
                // Handle button click
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp)
        ) {
            Text("Add Media")
        }

        Button(
            onClick = {
                if (currentUser != null) {
                    val newPost = Post(
                        text = messageState.value,
                        author = currentUser.username,
                        imageUrl = imageUrlState.value
                    )
                    viewModel.addPost(newPost)
                    messageState.value = ""
                    imageUrlState.value = ""
                    navController.navigateUp()
                }
            },
            modifier = Modifier.align(Alignment.End)
                .padding(end=16.dp)
        ) {
            Text("Add Post")
        }
    }
}


