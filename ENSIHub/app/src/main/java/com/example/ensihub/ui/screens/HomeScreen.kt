package com.example.ensihub.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ensihub.mainClasses.Feed

@Composable
fun HomeScreen(feed: Feed) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        feed.getData().forEach {
            PostView(post = it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(Feed())
}

