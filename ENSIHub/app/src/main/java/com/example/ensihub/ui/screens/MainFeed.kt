package com.example.ensihub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ensihub.mainClasses.Post
import com.example.ensihub.mainClasses.Feed
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color


@Composable
fun PostItem(post : Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Red)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = post.author)
            Text(text = post.text)
        }
    }
}

@Composable
fun MainFeed(feed : Feed) {
    val posts = feed.getData()
    LazyColumn {
        items(posts) { post ->
            PostItem(post)
        }
    }
}
