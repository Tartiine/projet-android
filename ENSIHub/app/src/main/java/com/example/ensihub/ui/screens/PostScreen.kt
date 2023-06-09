package com.example.ensihub.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ensihub.mainClasses.Post

@Composable
fun PostView(post: Post) {
    val showImage = remember { mutableStateOf(true) }

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

        Row(horizontalArrangement = Arrangement.End) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                onClick = {
                    showImage.value = !showImage.value
                },
                elevation = null,
                ) {

                if (showImage.value) {
                    Text("Reduce to hide image")
                    Spacer(modifier = Modifier.fillMaxWidth(0.9f))
                    Icon(imageVector = Icons.Filled.KeyboardArrowDown, null)
                } else {
                    Text("Extend to see image")
                    Spacer(modifier = Modifier.fillMaxWidth(0.9f))
                    Icon(imageVector = Icons.Filled.KeyboardArrowRight, null)
                }
            }
        }
        Divider()

        if (showImage.value) {
            AsyncImage(post.imageUrl, null)
        }

    }
}

@Preview
@Composable
fun PostViewPreview() {
    PostView(post = Post(0, "body", 1000, "titi\n difdc,", 0, "https://macollectiondepoupees.files.wordpress.com/2019/05/dsc_0023-1.jpg"))
}