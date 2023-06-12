package com.example.ensihub.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.Post


@Composable
fun HomeScreen(viewModel: FeedViewModel) {
    val posts: List<Post> by viewModel.posts.observeAsState(initial = emptyList())

    Column {
        Text(text = "Home Screen")
        LazyColumn {
            items(posts) { post ->
                Log.d("HomeScreen", "Post ID: ${post.id}")
                val showImage = remember { mutableStateOf(false) }
                PostView(post = post, showImage = showImage.value, onToggleShowImage = { showImage.value = !showImage.value }, viewModel = viewModel)
            }
        }
        Button(onClick = { viewModel.loadMore() }) {
            Text("Load More")
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    //HomeScreen()
}



