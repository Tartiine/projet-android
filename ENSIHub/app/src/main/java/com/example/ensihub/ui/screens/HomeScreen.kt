package com.example.ensihub.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.Post
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun HomeScreen(viewModel: FeedViewModel) {
    val posts: List<Post> by viewModel.posts.observeAsState(initial = emptyList())
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    Column(modifier = Modifier.background(Color.Black).fillMaxSize()) {
        SwipeRefresh(state = swipeRefreshState, onRefresh = {
            swipeRefreshState.isRefreshing = true
            viewModel.reload()
            swipeRefreshState.isRefreshing = false
        }) {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                items(posts) { post ->
                    Log.d("HomeScreen", "Post ID: ${post.id}")
                    val showImage = remember { mutableStateOf(false) }
                    PostView(
                        post = post,
                        showImage = showImage.value,
                        onToggleShowImage = { showImage.value = !showImage.value },
                        viewModel = viewModel
                    )
                }
                item {
                    Button(onClick = { viewModel.loadMore() }, modifier = Modifier.padding(20.dp)) {
                        Text("Load More")
                    }
                }
            }
        }


    }
}




@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    //HomeScreen()
}



