package com.example.ensihub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.ensihub.MainActivity
import com.example.ensihub.mainClasses.SharedViewModel

@Composable
fun NewPostView(navController: NavController) {
    val messageState = remember { mutableStateOf("") }
    val imageUrlState = remember { mutableStateOf("") }
    val viewModel: FeedViewModel = viewModel()
    val currentUser = viewModel.currentUser.collectAsState().value
    val context = LocalContext.current
    val sharedViewModel: SharedViewModel = viewModel()
    val imageUrl = sharedViewModel.imageUrl.observeAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(color = Color(0xFF000000))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(color = Color(255, 152, 23))
        ) {
            Text(
                text = "CREATE POST",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontSize = androidx.compose.material3.MaterialTheme.typography.titleLarge.fontSize,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )
        }

        TextField(
            value = messageState.value,
            onValueChange = { messageState.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            label = { Text("What's happening?",color = Color.Black) },
            textStyle = MaterialTheme.typography.body1,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color(255, 152, 23), // Set the cursor color to orange
                focusedIndicatorColor = Color(255, 152, 23) // Set the focused indicator color to orange
            )
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    (context as MainActivity).showImagePicker()
                },
                modifier = Modifier
                    .weight(0.30f)
                    .height(50.dp)
                    .padding(start = 16.dp, end = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(255, 152, 23),
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Media")
            }

            Spacer(modifier = Modifier.weight(0.5f))

            Button(
                onClick = {
                    if (currentUser != null) {
                        val newPost = Post(
                            text = messageState.value,
                            author = currentUser.username,
                            imageUrl = imageUrlState.value
                        )
                        viewModel.addPost(newPost, imageUrl)
                        messageState.value = ""
                        imageUrlState.value = ""
                        navController.navigateUp()
                    }
                },
                modifier = Modifier
                    .weight(0.75f)
                    .height(50.dp)
                    .padding(end = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(255, 152, 23),
                    contentColor = Color.White
                )
            ) {
                Text("Add Post")
            }

        }
    }
}

