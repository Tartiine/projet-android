package com.example.ensihub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ensihub.mainClasses.Role
import com.example.ensihub.mainClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.ensihub.mainClasses.LoginViewModel

@Composable
fun SettingsView(user: User, navHostController: NavHostController, loginViewModel: LoginViewModel) {
    val popupControl = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            SettingsGroup(name = "Application Settings") {
                SettingsElement(icon = Icons.Filled.Edit, name = "Number of posts loaded")
            }

            SettingsGroup(name = "Account Settings") {
                SettingsElement(icon = Icons.Filled.AccountCircle, name = "Change username")
                SettingsElement(icon = Icons.Filled.Lock, name = "Change password")
            }

            SettingsGroup(name = "About") {
                SettingsElement(icon = Icons.Filled.Person, name = "Contact")
                SettingsElement(icon = Icons.Filled.Build, name = "Info & Build")
                Row {

                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button (
                shape = RoundedCornerShape(10.dp),
                onClick = { popupControl.value = true },
                colors = ButtonDefaults.buttonColors(Color.Red),
            ) {
                Text(text = "Logout", style = MaterialTheme.typography.bodyLarge)
            }
            if (popupControl.value) {
                AlertDialog(shape = RoundedCornerShape(8.dp),
                    onDismissRequest = {
                        popupControl.value = false
                    }, confirmButton = {
                        Button(onClick = {
                            disconnect()
                            navHostController.navigate(LoginRoutes.SignIn.name)
                            loginViewModel.updateStatusLogin(false)
                            popupControl.value = false
                        }) {
                            Text("Confirm", color = Color.Red)
                        }
                    }, title = {
                        Text(text = "Are you sure you want to disconnect?")
                    }, dismissButton = {
                        Button(onClick = { popupControl.value = false }) {
                            Text("Cancel")
                        }
                    })
            }
            Text(
                text = "ENSIHub",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp),
                color = Color(0xFF, 0x99, 0x02 )
            )
            Text(
                text = "v0.1.0",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )
            Text(
                text = "Developed by Ensisoft",
                color = Color.DarkGray
            )
            Text(
                text = "MIT License",
                color = Color.DarkGray
            )
        }
    }

}

@Preview
@Composable
fun SettingsViewPreview() {
    SettingsView(user = User("1", "thib", "thibaut.herault@uha.fr", Role.ADMIN), rememberNavController(), LoginViewModel())
}

fun disconnect() {
    Firebase.auth.signOut()
    FirebaseAuth.getInstance().signOut()
}

@Composable
fun SettingsGroup(
    name: String,
    // to accept only composables compatible with column
    content: @Composable ColumnScope.() -> Unit ){
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(name, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4),
        ) {
            Column {
                content()
            }
        }
    }
}


@Composable
fun SettingsElement(
    icon: ImageVector,
    name: String,
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.onSurface)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.padding(8.dp)) {
                    // setting text title
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(Modifier.fillMaxWidth())


            }
            Divider()
        }
    }
}
