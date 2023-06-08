package com.example.ensihub.ui.screens

import android.text.format.DateUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ensihub.mainClasses.Post
import com.example.ensihub.mainClasses.Role
import com.example.ensihub.mainClasses.User
import androidx.compose.ui.unit.sp


val user = User("1", "joel_dion", "joel.dion@uha.fr", Role.USER)
val posts = listOf(
    Post("1", "Merci Jacquie et Michel", System.currentTimeMillis(), user.id, 10),
    Post("2", "Quelle matinée incroyable ! J'ai eu la chance de rencontrer @manuelferrara, une véritable légende de l'industrie cinématographique. Non seulement nous avons partagé un délicieux petit-déjeuner, mais nous avons également échangé sur notre passion commune pour le cinéma. Merci pour cette expérience inoubliable, Manuel ! #RencontreDeRêve #Cinéma", System.currentTimeMillis(), user.id, 5),
    Post("3", "wsh Sofia", System.currentTimeMillis(), user.id, 3)
)
@Composable
fun UserProfileScreen(user: User, posts: List<Post>) {

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your profile",
                    style = MaterialTheme.typography.h4,
                    color = Color(0xFFFFA500),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(width = 350.dp, height = 60.dp)
                        .fillMaxWidth()
                )
            }
            Text(
                text = user.username,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
            PostList(posts = posts)
        }
    }
}



@Composable
fun PostList(posts: List<Post>) {
    LazyColumn {
        items(posts) { post ->
            PostItem2(post = post)
        }
    }
}

@Composable
fun PostItem2(post: Post) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .border(
                border = BorderStroke(1.dp, Color.White),
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Column {
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
                modifier = Modifier.padding(8.dp))
        }
    }
}


fun getTimeSincePost(timestamp: Long): String{
    val now = System.currentTimeMillis()
    val elapsedTime = now - timestamp
    return DateUtils.getRelativeTimeSpanString(timestamp, now , DateUtils.MINUTE_IN_MILLIS).toString()
}


@Preview
@Composable
fun UserProfileScreenPreview(){
    UserProfileScreen(user, posts)


}
