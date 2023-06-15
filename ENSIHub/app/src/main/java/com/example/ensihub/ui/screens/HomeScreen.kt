package com.example.ensihub.ui.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.res.Resources
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ensihub.R
import com.example.ensihub.mainClasses.Comment
import com.example.ensihub.mainClasses.CommentViewModel
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.Post
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * @brief Écran d'accueil.
 *
 * @param viewModel ViewModel du flux.
 * @param navController Contrôleur de navigation.
 */
@RequiresApi(Build.VERSION_CODES.O)
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

    myList.swapPost(posts)

    /**
     * Fonction pour rafraîchir la liste.
     */
    val onUpdateClick = {
        // Do something that updates the list
        swipeRefreshState.isRefreshing = true

        viewModel.reload {
            myList.swapPost(posts)
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
                                    myList.swapPost(posts)
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


/**
 * @brief Écran de détails d'un message.
 *
 * @param postId ID du message.
 * @param viewModel ViewModel du flux.
 * @param commentViewModel ViewModel des commentaires.
 */
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostDetailScreen(
    postId: String,
    viewModel: FeedViewModel,
    commentViewModel: CommentViewModel
) {
    val isLikedByUser = remember(postId) {
        mutableStateOf(viewModel.isLikedByUserMap[postId] ?: false)
    }
    val likesCount = viewModel.likesCountMap[postId] ?: 0
    val post: Post? by viewModel.getPost(postId).observeAsState()
    val currentPost = viewModel.getPost(postId).value
    val currentUser = viewModel.currentUser.collectAsState().value
    var text by remember { mutableStateOf("") }
    var textFieldHeight by remember { mutableStateOf(0) }

    val comments: List<Comment> by commentViewModel.comments.observeAsState(initial = emptyList())
    val myList = remember { mutableStateListOf<Comment>() }

    myList.swapComment(comments)

    Box(modifier = Modifier.fillMaxSize()) {
        post?.let {
            Column(
                modifier = Modifier
                    .background(color = Color(0xFF1B232E))
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.defaultuser
                                ),
                                contentDescription = "defaultuser",
                                modifier = Modifier
                                    .size(30.dp)
                                    .weight(1f)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = post!!.author,
                                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                modifier = Modifier.weight(8f)
                            )
                            Button(
                                modifier = Modifier
                                    .width(80.dp)
                                    .weight(2f),
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

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = post!!.text,
                            style = MaterialTheme.typography.body1,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Divider()

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                modifier = Modifier.size(60.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = if (isLikedByUser.value) Color(
                                        247,
                                        152,
                                        23
                                    ) else Color.White,
                                    backgroundColor = Color.Transparent
                                ),
                                onClick = {
                                    if (isLikedByUser.value) {
                                        viewModel.unlikePost(currentPost!!)
                                    } else {
                                        viewModel.likePost(currentPost!!)
                                    }
                                    isLikedByUser.value = !isLikedByUser.value
                                },
                                elevation = null
                            ) {
                                androidx.compose.material.Icon(
                                    if (isLikedByUser.value) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                    null
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = "$likesCount",
                                style = MaterialTheme.typography.body1,
                                color = Color.White,
                                modifier = Modifier.padding(start = 8.dp, end = 20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = viewModel.calculateTimePassed(
                                    viewModel.convertTimestampToLocalDateTime(
                                        currentPost!!.timestamp
                                    )
                                ),
                                style = MaterialTheme.typography.body2.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                color = Color.White
                            )
                        }
                            if (post!!.imageUrl != null) {
                                // Display the image only if showImage is true and imageUrl is not null
                                AsyncImage(
                                    post!!.imageUrl,
                                    null,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth()
                                        .clip(shape = RoundedCornerShape(20.dp))
                                )
                            }
                            if (post!!.videoUrl != null) {
                                VideoPlayer(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .height(800.dp),
                                    uri = android.net.Uri.parse(post!!.videoUrl)
                                )
                            }

                        }

                    }
                    Divider()
                    CommentList(myList)

                    Spacer(modifier = Modifier.height((textFieldHeight / Resources.getSystem().displayMetrics.density + 0.5f).toInt().dp))
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFF1B232E)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    textStyle = TextStyle(color = Color.White),
                    placeholder = { Text("Enter your comment here...", color = Color.White) },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color(0xFF2D3949),
                        unfocusedIndicatorColor = Color(0xFF2D3949),
                        containerColor = Color(0xFF2D3949),
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .onGloballyPositioned {
                            textFieldHeight = it.size.height
                        }
                        .background(Color(0xFF2D3949))
                )
                Button(
                    onClick = {
                        val comment =
                            Comment(postId, text = text, author = currentUser?.username ?: "")
                        commentViewModel.addComment(comment)
                        text = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = Color(alpha = 255, red = 247, green = 152, blue = 23)
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .height((textFieldHeight / Resources.getSystem().displayMetrics.density + 0.5f).toInt().dp)
                ) {
                    Text("Send")
                }
            }
        }
    }

/**
 * @brief Liste de commentaires.
 *
 * @param comments Liste des commentaires.
 */
    @Composable
    fun CommentList(comments: List<Comment>) {
        Column {
            Text(
                text = "Comments",
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )
            for (comment in comments) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(12.dp))

                    Image(
                        painter = painterResource(
                            id = R.drawable.defaultuser
                        ),
                        contentDescription = "defaultuser",
                        modifier = Modifier
                            .size(15.dp)
                    )

                    Text(
                        text = comment.author + " :",
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                    Text(
                        text = comment.text,
                        style = MaterialTheme.typography.body2,
                        color = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(bottom = 12.dp))
        }
    }




fun SnapshotStateList<Post>.swapPost(newList: List<Post>){
    newList.reversed().forEach {
        if (this.contains(it) && this[this.indexOf(it)].likes != it.likes) this[this.indexOf(it)] = it
        val post = this.firstOrNull { p-> p.timestamp < it.timestamp }
        if (!this.contains(it) && post != null) this.add(this.indexOf(post), it)
        if (!this.contains(it)) this.add(it)
    }

}

fun SnapshotStateList<Comment>.swapComment(newList: List<Comment>){
    newList.reversed().forEach {
        val comment = this.firstOrNull { p-> p.timestamp < it.timestamp }
        if (!this.contains(it) && comment != null) this.add(this.indexOf(comment), it)
        if (!this.contains(it)) this.add(it)
    }
}





