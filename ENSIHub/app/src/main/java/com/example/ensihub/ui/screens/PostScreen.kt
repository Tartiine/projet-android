package com.example.ensihub.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ensihub.mainClasses.Feed
import com.example.ensihub.mainClasses.Post

@Composable
fun PostView(post: Post) {
    val showImage = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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

                }) {
                Spacer(modifier = Modifier.fillMaxWidth(0.9f))
                if (showImage.value) {
                    Icon(imageVector = Icons.Filled.KeyboardArrowDown, null)
                } else {
                    Icon(imageVector = Icons.Filled.KeyboardArrowRight, null)
                }
            }
        }

        if (showImage.value) {
            AsyncImage(post.imageUrl, null)
        }

    }
}

@Preview
@Composable
fun PostViewPreview() {
    PostView(post = Post("0", "body", 1000, "titi\n difdc,", 0, "https://www.google.com/url?sa=i&url=https%3A%2F%2Fmacollectiondepoupees.fr%2F2019%2F05%2F05%2Fhistorique-kiki-sekiguchi-ajena-monchhichi%2F&psig=AOvVaw3jXWSqFvxRp2mjB5uxrn7u&ust=1686322016766000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCPDCh5n1s_8CFQAAAAAdAAAAABAD"))
}