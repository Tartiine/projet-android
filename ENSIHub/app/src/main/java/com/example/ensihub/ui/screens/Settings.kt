package com.example.ensihub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.ensihub.MainClasses.Role
import com.example.ensihub.MainClasses.User
import com.example.ensihub.R
import com.google.firebase.auth.FirebaseAuth


@Composable
fun SettingsView(user: User) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExtendedFloatingActionButton(
                onClick = { disconnect }
            )
            Text(
                text = "ENSIHub",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp),
                color = Color(0xFF, 0x99, 0x02 )
            )
            Text(
                text = "v1.0.0",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )
            Text(
                text = "Developed by "
            )
            Text(
                text = "MIT License"
            )
        }
    }

}


@Preview
@Composable
fun SettingsViewPreview() {
    SettingsView(user = User("1", "thib", "thibaut.herault@uha.fr", Role.ADMIN))
}

fun disconnect() {
    FirebaseAuth.getInstance().signOut()
    R.string.
}
