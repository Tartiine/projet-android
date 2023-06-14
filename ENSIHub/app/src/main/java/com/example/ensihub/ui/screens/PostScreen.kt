package com.example.ensihub.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.Post
import com.example.ensihub.mainClasses.Role
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.ensihub.R
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostView(
    post: Post,
    showImage: Boolean,
    onToggleShowImage: () -> Unit,
    viewModel: FeedViewModel,
    navigateToPostDetails: (String) -> Unit
) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser = firebaseAuth.currentUser
    val isLikedByUser = viewModel.isPostLikedByUser.observeAsState(initial = post.likes.contains(firebaseUser?.uid)).value
    val currentUser = viewModel.currentUser.collectAsState().value
    val textLimit = 200
    val fullText = post.text
    val displayText = if (fullText.length > textLimit) {
        "${fullText.substring(0, textLimit)}..."
    } else {
        fullText
    }


    Box(Modifier.clickable { navigateToPostDetails(post.id) }) {
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
                    Image(painter = painterResource(
                        id = R.drawable.defaultuser),
                        contentDescription = "defaultuser",
                        modifier = Modifier
                            .size(30.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = post.author,
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )

                    if (currentUser != null) {
                            Button(
                                modifier = Modifier
                                    .width(80.dp),
                                onClick = {
                                    viewModel.reportPost(post)
                                    Log.d("PostView", "reportPost clicked for postId = ${post.id}")
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                elevation = null
                            ) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = "Report post",
                                    tint = Color.White
                                )
                            }

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = displayText,
                    style = MaterialTheme.typography.body1,
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (fullText.length > textLimit) {
                    Text(
                        text = "Show more",
                        style = MaterialTheme.typography.body2.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                        color = Color.LightGray,
                        modifier = Modifier
                            .padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Divider()

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    modifier = Modifier.size(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = if (isLikedByUser) Color(247, 152, 23) else Color.White,
                        backgroundColor = Color.Transparent
                    ),
                    onClick = {
                        if (isLikedByUser) {
                            viewModel.unlikePost(post)
                        } else {
                            viewModel.likePost(post)
                        }
                    },
                    elevation = null
                ) {
                    Icon(if (isLikedByUser) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp, null)
                }
                if (post.imageUrl?.isNotEmpty() == true) {
                    Row(modifier = Modifier.clickable { onToggleShowImage() }) {
                        Text(
                            text = if (showImage) "Reduce to hide image" else "Extend to see image",
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = if (showImage) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
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
                        text = "Likes: ${post.likesCount}",
                        style = MaterialTheme.typography.body1,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp, end = 20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = viewModel.calculateTimePassed(
                            viewModel.convertTimestampToLocalDateTime(
                                post.timestamp
                            )
                        ),
                        style = MaterialTheme.typography.body2.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                        color = Color.White
                    )
                }

                    Spacer(modifier = Modifier.width(8.dp))

                }
                Divider()

                if (showImage && post.imageUrl != null) {
                    // Display the image only if showImage is true and imageUrl is not null
                    AsyncImage(
                        post.imageUrl,
                        null,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PostViewPreview() {
    val viewModel: FeedViewModel = viewModel()
    val showImage = remember { mutableStateOf(true) }
    PostView(
        post = Post(
            text = "body",
            author = "titi\n difdc,",
            imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fmacollectiondepoupees.fr%2F2019%2F05%2F05%2Fhistorique-kiki-sekiguchi-ajena-monchhichi%2F&psig=AOvVaw3jXWSqFvxRp2mjB5uxrn7u&ust=1686322016766000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCPDCh5n1s_8CFQAAAAAdAAAAABAD"
        ),
        showImage = showImage.value,
        onToggleShowImage = { showImage.value = !showImage.value },
        viewModel = viewModel,
        navigateToPostDetails = {}
    )
}









