package com.example.ensihub.ui.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.StateRecord
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.Post
import com.example.ensihub.mainClasses.Role
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: FeedViewModel, navController: NavController) {
    val posts: List<Post> by viewModel.posts.observeAsState(initial = emptyList())
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    val myList = remember { mutableStateListOf<Post>() }
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Black
    )

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
                            viewModel = viewModel,
                            navigateToPostDetails = { postId ->
                                navController.navigate("postDetails/$postId")
                            }
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

@Composable
fun PostDetailScreen(
    postId: String,
    viewModel: FeedViewModel,
    navController: NavController
) {
    val post: Post? by viewModel.getPost(postId).observeAsState()
    val isLiked = remember { post?.let { mutableStateOf(it.isLiked) } }
    val currentUser = viewModel.currentUser.collectAsState().value

    post?.let {
        Column(
            modifier = Modifier
                .background(color = Color(0xFF1B232E))
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color(0xFF2D3949))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = post!!.author,
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        if (currentUser != null) {
                            if (currentUser.role == Role.USER) {
                                Button(
                                    modifier = Modifier.width(80.dp),
                                    onClick = {
                                        viewModel.reportPost(post!!)
                                        Log.d(
                                            "PostView",
                                            "reportPost clicked for postId = ${post!!.id}"
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                    elevation = null
                                ) {
                                    androidx.compose.material.Icon(
                                        Icons.Default.Warning,
                                        contentDescription = "Report post",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = post!!.text,
                        style = MaterialTheme.typography.body1,
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Divider()

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isLiked != null) {
                            Button(
                                modifier = Modifier.size(60.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = if (isLiked.value) Color(
                                        247,
                                        152,
                                        23
                                    ) else Color.White, backgroundColor = Color.Transparent
                                ),
                                onClick = {
                                    if (isLiked.value) {
                                        viewModel.unlikePost(post!!)
                                        Log.d(
                                            "PostView",
                                            "unlikePost clicked for postId = ${post!!.id}"
                                        )
                                    } else {
                                        viewModel.likePost(post!!)
                                        Log.d(
                                            "PostView",
                                            "likePost clicked for postId = ${post!!.id}"
                                        )
                                    }
                                    isLiked.value = !isLiked.value
                                },
                                elevation = null
                            ) {
                                androidx.compose.material.Icon(
                                    if (isLiked.value) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                    null
                                )
                            }
                        }

                        Text(
                            text = "Likes: ${post!!.likesCount}",
                            style = MaterialTheme.typography.body1,
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp, end = 20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                Divider()

                if (post!!.imageUrl != null) {
                    // Display the image only if showImage is true and imageUrl is not null
                    AsyncImage(
                        post!!.imageUrl,
                        null,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
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




