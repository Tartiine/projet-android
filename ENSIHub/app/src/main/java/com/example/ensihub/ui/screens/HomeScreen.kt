package com.example.ensihub.ui.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.StateRecord
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.Post
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: FeedViewModel, navController: NavController) {
    val posts: List<Post> by viewModel.posts.observeAsState(initial = emptyList())
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    val myList = remember { mutableStateListOf<Post>() }

    myList.swapList(posts)

    // Function to refresh the list
    val onUpdateClick = {
        // Do something that updates the list
        swipeRefreshState.isRefreshing = true

        viewModel.reload {
            myList.swapList(posts)
            swipeRefreshState.isRefreshing = false
        }
        // Get the updated list to trigger a recompose
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("newPost") }, backgroundColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = "Add Post", tint = Color(0xFFFF8C00))
            }
        }
    ) {
        Column(modifier = Modifier
            .background(color = Color(0xFF1B232E))
            .fillMaxSize()) {
            SwipeRefresh(state = swipeRefreshState, onRefresh = {
                onUpdateClick()
            }) {
                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                    items(myList) { post ->
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
                        LaunchedEffect(true) {
                            viewModel.loadMore {
                                MainScope().launch {
                                    Log.d(TAG, "loaded")
                                    myList.swapList(posts)
                                    Log.d(TAG, "Swapped")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun SnapshotStateList<Post>.swapList(newList: List<Post>){
    newList.reversed().forEach {
        if (this.contains(it) && this[this.indexOf(it)].likes != it.likes) this[this.indexOf(it)] = it
        val post = this.firstOrNull { p-> p.timestamp < it.timestamp }
        if (!this.contains(it) && post != null) this.add(this.indexOf(post), it)
        if (!this.contains(it)) this.add(it)
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    //HomeScreen()
}




