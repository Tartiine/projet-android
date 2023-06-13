package com.example.ensihub.ui.screens

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ensihub.mainClasses.Moderation
import com.example.ensihub.mainClasses.Post
import com.example.ensihub.mainClasses.Role
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ModerationView(
    post: Post,
    showImage: Boolean,
    onToggleShowImage: () -> Unit,
    moderationViewModel : Moderation
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(10.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.author,
                style = MaterialTheme.typography.subtitle1,
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

        Button(
            modifier = Modifier.width(80.dp),
            onClick = {
                moderationViewModel.approvePost(post)
                Log.d("ModerationView", "The post = ${post.id} is approved")
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            elevation = null
        ) {
            Text("Approve")
        }

        Button(
            modifier = Modifier.width(80.dp),
            onClick = {
                moderationViewModel.rejectPost(post)
                Log.d("ModerationView", "The post = ${post.id} is rejected and deleted")
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            elevation = null
        ) {
            Text("Reject")
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
fun ModerationViewPreview() {
    val moderation = Moderation()
    val showImage = remember { mutableStateOf(true)}
    ModerationView(
        post = Post(
            text = "Sometimes my genius is almost frightening",
            author = "My\n Self,",
            imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fmacollectiondepoupees.fr%2F2019%2F05%2F05%2Fhistorique-kiki-sekiguchi-ajena-monchhichi%2F&psig=AOvVaw3jXWSqFvxRp2mjB5uxrn7u&ust=1686322016766000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCPDCh5n1s_8CFQAAAAAdAAAAABAD"
        ),
        showImage = showImage.value,
        onToggleShowImage = { showImage.value = !showImage.value },
        moderationViewModel = moderation
    )
}
