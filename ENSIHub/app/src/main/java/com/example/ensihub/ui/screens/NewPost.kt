package com.example.ensihub.ui.screens

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.ensihub.MainActivity
import com.example.ensihub.mainClasses.SharedViewModel

@Composable
fun NewPostView(navController: NavController) {
    val messageState = remember { mutableStateOf("") }
    val imageUrlState = remember { mutableStateOf("") }
    val viewModel: FeedViewModel = viewModel()
    val currentUser = viewModel.currentUser.collectAsState().value
    var photoUri: Uri? by remember { mutableStateOf(null) }
    val launcherImage = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        //When the user has selected a photo, its URI is returned here
        photoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(color = Color(0xFF000000))
            .verticalScroll(rememberScrollState())
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
                    launcherImage.launch(
                        PickVisualMediaRequest(
                        //Here we request only photos. Change this to .ImageAndVideo if
                        //you want videos too.
                        //Or use .VideoOnly if you only want videos.
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                    Log.d(TAG, photoUri.toString())
                },
                modifier = Modifier
                    .height(50.dp)
                    .padding(start = 16.dp, end = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(255, 152, 23),
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Media")
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text("Add image")
            }


            Spacer(modifier = Modifier.weight(0.5f))

            Button(
                onClick = {
                    if (currentUser != null) {
                        photoUri?.let { viewModel.pushImage(it, Post(text = messageState.value, author = currentUser.username)) }
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
        if (photoUri != null) {
            //Use Coil to display the selected image
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(data = photoUri)
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

