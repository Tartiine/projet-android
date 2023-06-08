package com.example.ensihub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ensihub.mainClasses.Post

@Composable
fun PostView(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = post.author,
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = post.text,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(top = 4.dp)
        )
        Row {
            Button(
                shape = RoundedCornerShape(10.dp),
                onClick = {

            }) {
                Icon(imageVector = Icons.Filled.KeyboardArrowRight, null)
                Text("button")
            }
        }
    }
}

@Preview
@Composable
fun PostViewPreview() {
    PostView(post = Post("0", "body", 1000, "titi", 0))
}