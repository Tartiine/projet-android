package com.example.ensihub.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ensihub.MainClasses.Post

@Preview
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
    }
}
