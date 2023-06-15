package com.example.ensihub.ui.screens

import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ensihub.mainClasses.Post
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ensihub.R
import com.example.ensihub.mainClasses.FeedViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * @brief Écran du profil utilisateur.
 */
@Composable
fun UserProfileScreen() {
    val viewModel: FeedViewModel = viewModel()
    val currentUser = viewModel.currentUser.collectAsState().value
    val userPosts: List<Post> by viewModel.userPosts.observeAsState(initial = emptyList())
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Black
    )

    if (currentUser != null) {
        viewModel.loadUserPosts()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B232E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(top = 50.dp, bottom = 25.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.orangeuser),
                    contentScale = ContentScale.Crop,
                    contentDescription = "profileLogo",
                    modifier = Modifier
                        .requiredWidth(180.dp)
                        .requiredHeight(180.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    currentUser?.let {
                        Text(
                            text = it.username,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.h3,
                            color = Color.White
                        )
                    }
                }
            }

            PostList(posts = userPosts)
        }
    }
}

/**
 * @brief Liste des publications d'utilisateurs.
 *
 * @param posts Liste des publications à afficher.
 */
@Composable
fun PostList(posts: List<Post>) {
    Box(

    ) {
        LazyColumn {
            item {
                OrangeRectangle() // Add the orange rectangle as the first item
            }
            items(posts) { post ->
                PostItem2(post = post)
            }
        }
    }
}

@Composable
fun OrangeRectangle() {
    Box(
        modifier = Modifier
            .height(25.dp)
            .fillMaxWidth()
            .background(Color(
                alpha = 255,
                red = 247,
                green = 152,
                blue = 23
            ))
    ) {
    }
}

/**
 * @brief Élément de publication.
 *
 * @param post Publication à afficher.
 */
@Composable
fun PostItem2(post: Post) {

    Column( modifier = Modifier
        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        .fillMaxSize()
        .clip(shape = RoundedCornerShape(20.dp))
        .background(color = Color(0xFF2D3949))) {
        Text(
            text = post.text,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = getTimeSincePost(post.timestamp),
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(8.dp)
        )

        if (post!!.imageUrl != null) {
            // Display the image only if showImage is true and imageUrl is not null
            AsyncImage(
                post!!.imageUrl,
                null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(20.dp))
            )
        }
    }
}



/**
 * @brief Récupère le temps écoulé depuis une publication sous forme de chaîne de caractères.
 *
 * @param timestamp Horodatage de la publication.
 * @return Temps écoulé depuis la publication.
 */
fun getTimeSincePost(timestamp: Long): String{
    val now = System.currentTimeMillis()
    return DateUtils.getRelativeTimeSpanString(timestamp, now , DateUtils.MINUTE_IN_MILLIS).toString()
}


@Preview
@Composable
fun UserProfileScreenPreview(){
    //UserProfileScreen(user)
}
