package com.example.ensihub.ui.screens

import android.graphics.fonts.FontStyle
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ensihub.mainClasses.Role
import com.example.ensihub.mainClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.ensihub.mainClasses.LoginViewModel
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore

@Composable
fun SettingsView(navHostController: NavHostController) {
    val popupControl = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(
                        alpha = 255,
                        red = 247,
                        green = 152,
                        blue = 23
                    )
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "SETTINGS",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                modifier = Modifier.padding(16.dp)
            )
        }

        Text(
            text = "Application Settings",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = Color(
                    alpha = 255,
                    red = 247,
                    green = 152,
                    blue = 23
                ),
                textAlign = TextAlign.Left
            ),
            modifier = Modifier.padding(8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Build Icon",
                tint = Color.White,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )

            ClickableText(
                text = AnnotatedString("Number of posts loaded"),
                onClick = {
                          navHostController.navigate("settings/numberOfPosts")
                },
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize

                ),
                modifier = Modifier
                    .padding(8.dp)
            )


            Text(
                text = ">",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Account Settings",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = Color(
                    alpha = 255,
                    red = 247,
                    green = 152,
                    blue = 23
                ),
                textAlign = TextAlign.Left
            ),
            modifier = Modifier.padding(8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Build Icon",
                tint = Color.White,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )

            ClickableText(
                text = AnnotatedString("Change Username"),
                onClick = {
                    navHostController.navigate("settings/changeUsername")
                },
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize

                ),
                modifier = Modifier
                    .padding(8.dp)
            )


            Text(
                text = ">",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                ),
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Build Icon",
                tint = Color.White,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )

            ClickableText(
                text = AnnotatedString("Change Password"),
                onClick = {
                    navHostController.navigate("settings/changePassword")
                },
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize

                ),
                modifier = Modifier
                    .padding(8.dp)
            )


            Text(
                text = ">",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "About",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = Color(
                    alpha = 255,
                    red = 247,
                    green = 152,
                    blue = 23
                ),
                textAlign = TextAlign.Left
            ),
            modifier = Modifier.padding(8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.MailOutline,
                contentDescription = "Build Icon",
                tint = Color.White,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )

            ClickableText(
                text = AnnotatedString("Contact"),
                onClick = {},
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize

                ),
                modifier = Modifier
                    .padding(8.dp)
            )


            Text(
                text = ">",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                ),
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Build Icon",
                tint = Color.White,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )

            ClickableText(
                text = AnnotatedString("About us"),
                onClick = {},
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize

                ),
                modifier = Modifier
                    .padding(8.dp)
            )


            Text(
                text = ">",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                ),
            )
        }



        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
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
                            LoginViewModel().updateStatusLogin(false)
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
                color = Color(0xFF, 0x99, 0x02)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumPostsView(navHostController: NavHostController) {
    val postNumber = remember { mutableStateOf("10") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(
                        alpha = 255,
                        red = 247,
                        green = 152,
                        blue = 23
                    )
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "SETTINGS",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                modifier = Modifier.padding(16.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            
            Text(
                modifier = Modifier.padding(32.dp),
                text = "Number of posts per page",
                style = TextStyle(
                    color = Color.White
                ),
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            OutlinedTextField(
                value = postNumber.value,
                onValueChange = {
                    postNumber.value = it
                },
                textStyle = TextStyle(
                    color = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(8.dp)
                    .width(50.dp)
                    .height(50.dp),
            )
        }

        Button(
            onClick = { },
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                Color(
                    alpha = 255,
                    red = 247,
                    green = 152,
                    blue = 23
                )
            )
        ) {
            Text(text = "Save", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeUsernameView(navHostController: NavHostController) {
    val newUsername = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(
                        alpha = 255,
                        red = 247,
                        green = 152,
                        blue = 23
                    )
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "SETTINGS",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "New Username",
            modifier = Modifier
                .padding(start = 32.dp)
                .align(Alignment.Start),
            style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
            color = Color(
                alpha = 255,
                red = 247,
                green = 152,
                blue = 23
            )
        )

        TextField(
            value = newUsername.value,
            onValueChange = {
                newUsername.value = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.LightGray
                )
            },
            label = { Text("Enter your new username") },
            modifier = Modifier
                .padding(16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White, // Set the text color to white
                cursorColor = Color.White, // Set the cursor color to white
                focusedBorderColor = Color.White, // Set the focused border color to white
                unfocusedBorderColor = Color.White // Set the unfocused border color to white
            ),
            textStyle = TextStyle(color = Color.White) // Set the text color to white
        )


        Button(
            onClick = {
                if (newUsername.value != "") {
                    val userId = Firebase.auth.currentUser!!.uid
                    val db = Firebase.firestore
                    db.collection("users").document(userId)
                        .update("username", newUsername.value)
                        .addOnSuccessListener {
                            Log.d("TAG", "DocumentSnapshot successfully updated!")
                        }
                }
                navHostController.navigate(BottomBarScreen.Settings.route)
            },
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                Color(
                    alpha = 255,
                    red = 247,
                    green = 152,
                    blue = 23
                )
            )
        ) {
            Text(text = "Change Username")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordView(navHostController: NavHostController) {
    val newPassword = remember { mutableStateOf("") }
    val newPasswordConfirm = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(
                        alpha = 255,
                        red = 247,
                        green = 152,
                        blue = 23
                    )
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "SETTINGS",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "New Password",
            modifier = Modifier
                .padding(start = 32.dp)
                .align(Alignment.Start),
            style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
            color = Color(
                alpha = 255,
                red = 247,
                green = 152,
                blue = 23
            )
        )

        TextField(
            value = newPassword.value,
            onValueChange = {
                newPassword.value = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.LightGray
                )
            },
            label = { Text("Enter your new Password") },
            modifier = Modifier
                .padding(16.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White, // Set the text color to white
                cursorColor = Color.White, // Set the cursor color to white
                focusedBorderColor = Color.White, // Set the focused border color to white
                unfocusedBorderColor = Color.White // Set the unfocused border color to white
            ),
            textStyle = TextStyle(color = Color.White) // Set the text color to white
        )

        TextField(
            value = newPasswordConfirm.value,
            onValueChange = {
                newPasswordConfirm.value = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.LightGray
                )
            },
            label = { Text("Confirm your new Password") },
            modifier = Modifier
                .padding(16.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White, // Set the text color to white
                cursorColor = Color.White, // Set the cursor color to white
                focusedBorderColor = Color.White, // Set the focused border color to white
                unfocusedBorderColor = Color.White // Set the unfocused border color to white
            ),
            textStyle = TextStyle(color = Color.White) // Set the text color to white
        )


        Button(
            onClick = {
                if (newPassword.value != "" && newPassword.value == newPasswordConfirm.value) {
                    val userId = Firebase.auth.currentUser!!.uid
                    Firebase.auth.currentUser!!.updatePassword(newPassword.value)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("TAG", "User password updated.")
                            }
                        }
                }
                if (newPassword.value != newPasswordConfirm.value){
                    Log.d("TAG", "Passwords do not match")
                }
                if (newPassword.value == ""){
                    Log.d("TAG", "Password cannot be empty")
                }
                if (newPasswordConfirm.value == ""){
                    Log.d("TAG", "Please confirm password")
                }
                navHostController.navigate(BottomBarScreen.Settings.route)
            },
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                Color(
                    alpha = 255,
                    red = 247,
                    green = 152,
                    blue = 23
                )
            )
        ) {
            Text(text = "Change Password")
        }

    }
}

fun disconnect() {
    Firebase.auth.signOut()
    FirebaseAuth.getInstance().signOut()
}

@Preview
@Composable
fun SettingsViewPreview() {
    SettingsView(navHostController = rememberNavController())
}

@Preview
@Composable
fun NumPostsViewPreview() {
    NumPostsView(navHostController = rememberNavController())
}

@Preview
@Composable
fun ChangeUsernameViewPreview() {
    ChangeUsernameView(navHostController = rememberNavController())
}

@Preview
@Composable
fun ChangePasswordPreview() {
    ChangePasswordView(navHostController = rememberNavController())
}