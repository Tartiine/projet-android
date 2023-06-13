package com.example.ensihub.ui.screens


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.Moderation
import com.example.ensihub.mainClasses.Post
import com.example.ensihub.mainClasses.Role
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PostView(
    post: Post,
    showImage: Boolean,
    onToggleShowImage: () -> Unit,
    viewModel: FeedViewModel
) {

    val isLiked = remember { mutableStateOf(post.isLiked) }
    val currentUser = viewModel.currentUser.collectAsState().value

    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color(0xFF2D3949))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.author,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )

            Text(
                text = post.text,
                style = MaterialTheme.typography.body1,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Divider()


        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                modifier = Modifier.size(60.dp),
                colors = ButtonDefaults.buttonColors(contentColor = if (isLiked.value) Color(247, 152,23) else Color.White, backgroundColor = Color.Transparent),
                onClick = {
                    if (isLiked.value) {
                        viewModel.unlikePost(post)
                        Log.d("PostView", "unlikePost clicked for postId = ${post.id}")
                    } else {
                        viewModel.likePost(post)
                        Log.d("PostView", "likePost clicked for postId = ${post.id}")
                    }
                    isLiked.value = !isLiked.value
                },
                elevation = null
            ) {
                Icon(if (isLiked.value) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp, null)
            }

            Text(
                text = "Likes: ${post.likesCount}",
                style = MaterialTheme.typography.body1,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp, end = 20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
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


        if (currentUser != null) {
            if(currentUser.role==Role.USER){
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.width(100.dp),
                    onClick = {
                        /* On click function */
                        Log.d("PostView", "reportPost clicked for postId = ${post.id}")
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF213865)),
                    elevation = null
                ) {
                    Text("Report")
                }
            } else {
                Button(
                    modifier = Modifier.width(100.dp),
                    onClick = {

                        Log.d("PostView", "validatePost clicked for postId = ${post.id}")
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                    elevation = null
                ) {
                    Text("Validate",color = Color.White)
                }
                Button(
                    modifier = Modifier.width(100.dp),
                    onClick = {

                        Log.d("PostView", "deletePost clicked for postId = ${post.id}")
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    elevation = null
                    ) {
                        Text("Delete")
                    }

                }



            }
            Divider()

            if (showImage && post.imageUrl != null) {
            // Display the image only if showImage is true and imageUrl is not null
                AsyncImage(post.imageUrl, null, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
}




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
        viewModel = viewModel
    )
}




