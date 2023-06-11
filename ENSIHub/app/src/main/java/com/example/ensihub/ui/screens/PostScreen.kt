package com.example.ensihub.ui.screens


import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.Post

@Composable
fun PostView(
    post: Post,
    showImage: Boolean,
    onToggleShowImage: () -> Unit,
    viewModel: FeedViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.author,
                style = MaterialTheme.typography.subtitle1
            )

            Text(
                text = post.text,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Divider()

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                onClick = { viewModel.likePost(post)
                    Log.d("PostView", "likePost clicked for postId = ${post.id}")},
                elevation = null
            ) {
                Text("Like")
            }

            Text(
                text = "${post.likesCount}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
            if (post.imageUrl?.isNotEmpty() == true) {
                Row(modifier = Modifier.clickable { onToggleShowImage() }) {
                    Text(
                        text = if (showImage) "Reduce to hide image" else "Extend to see image",
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (showImage) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        Divider()

        if (showImage && post.imageUrl != null) {
            // Display the image only if showImage is true and imageUrl is not null
            AsyncImage(post.imageUrl, null)
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
            id = 0,
            text = "body",
            timestamp = 1000,
            author = "titi\n difdc,",
            likesCount = 0,
            imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fmacollectiondepoupees.fr%2F2019%2F05%2F05%2Fhistorique-kiki-sekiguchi-ajena-monchhichi%2F&psig=AOvVaw3jXWSqFvxRp2mjB5uxrn7u&ust=1686322016766000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCPDCh5n1s_8CFQAAAAAdAAAAABAD"
        ),
        showImage = showImage.value,
        onToggleShowImage = { showImage.value = !showImage.value },
        viewModel = viewModel
    )
}

