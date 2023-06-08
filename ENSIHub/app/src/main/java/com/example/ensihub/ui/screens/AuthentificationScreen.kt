package com.example.ensihub.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ensihub.R
import com.example.ensihub.mainClasses.LoginViewModel
import com.example.ensihub.ui.theme.ENSIHubTheme
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onNavToHomePage: () -> Unit,
    onNavToSignUpPage: () -> Unit,
    onNavToForgotPasswordPage: () -> Unit
) {
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.value?.loginError != null
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "APPLOGO",
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "Login",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontSize = 16.sp)
        )

        if (isError) {
            Text(text = loginUiState?.value?.loginError ?: "unknown error", color = Color.Red)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = loginUiState?.value?.userName ?: "",
                onValueChange = { loginViewModel?.onUserNameChange(it)},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                label = { Text("Enter your email") },
                modifier = Modifier
                    .padding(16.dp),
                isError = isError,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White, // Set the text color to white
                    cursorColor = Color.White, // Set the cursor color to white
                    focusedBorderColor = Color.White, // Set the focused border color to white
                    unfocusedBorderColor = Color.White // Set the unfocused border color to white
                ),
                textStyle = TextStyle(color = Color.White) // Set the text color to white
            )

            Text(text = "@uha.fr")
        }



        TextField(
            value = loginUiState?.value?.password ?: "",
            onValueChange = { loginViewModel?.onPasswordChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            label = { Text(text = "Enter your password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            isError = isError,
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
                Log.d("LoginScreen", "LoginScreen: ${loginUiState?.value?.userName}")
                loginUiState?.value?.userName += "@uha.fr"
                Log.d("LoginScreen", "LoginScreen: ${loginUiState?.value?.userName}")
                loginViewModel?.loginUser(context) },
                modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Login")
        }

        Text(
            text = "New Member?",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontSize = 16.sp)
        )

        Button(
            onClick = { onNavToSignUpPage.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Sign Up")
        }

        ClickableText(
            text = AnnotatedString("Forgot Password?"),
            onClick = { onNavToForgotPasswordPage.invoke() },
        )

    }

    if (loginUiState?.value?.isLoading == true) {
        CircularProgressIndicator()
    }

    LaunchedEffect(key1 = loginViewModel?.hasUser) {
        if (loginViewModel?.hasUser == true) {
            onNavToHomePage.invoke()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    loginViewModel: LoginViewModel? = null,
    modifier: Modifier = Modifier,
    onNavToHomePage: () -> Unit,
    onNavToLoginPage: () -> Unit
) {
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.value?.signUpError != null
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "APPLOGO",
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "Sign Up",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontSize = 16.sp)
        )

        if (isError) {
            Text(text = loginUiState?.value?.signUpError ?: "unknown error", color = Color.Red)
        }

        TextField(
            value = loginUiState?.value?.userNameSignUp ?: "",
            onValueChange = { loginViewModel?.onUserNameSignUpChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            label = { Text("Enter your email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            isError = isError,
            colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.Black)
        )

        TextField(
            value = loginUiState?.value?.passwordSignUp ?: "",
            onValueChange = { loginViewModel?.onPasswordSignUpChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            label = { Text("Enter your password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = isError,
            colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
        )

        TextField(
            value = loginUiState?.value?.confirmPasswordSignUp ?: "",
            onValueChange = { loginViewModel?.onConfirmPasswordSignUpChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = isError,
            colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
        )

        Button(
            onClick = { loginViewModel?.createUser(context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Sign In")
        }

        Text(
            text = "Already have an Account?",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontSize = 16.sp)
        )

        Button(
            onClick = { onNavToLoginPage.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Login")
        }
    }

    if (loginUiState?.value?.isLoading == true) {
        CircularProgressIndicator()
    }

    LaunchedEffect(key1 = loginViewModel?.hasUser) {
        if (loginViewModel?.hasUser == true) {
            onNavToHomePage.invoke()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    loginViewModel: LoginViewModel? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onNavToLoginPage: () -> Unit,

) {
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.value?.loginError != null
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "APPLOGO",
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "Forgot Password",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontSize = 16.sp)
        )

        if (isError) {
            Text(text = loginUiState?.value?.loginError ?: "unknown error", color = Color.Red)
        }

        OutlinedTextField(
            value = loginUiState?.value?.userName ?: "",
            onValueChange = { loginViewModel?.onUserNameChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            label = { Text("Enter your email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            isError = isError,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White, // Set the text color to white
                cursorColor = Color.White, // Set the cursor color to white
                focusedBorderColor = Color.White, // Set the focused border color to white
                unfocusedBorderColor = Color.White // Set the unfocused border color to white
            ),
            textStyle = TextStyle(color = Color.White) // Set the text color to white
        )


        Button(
            onClick = { loginViewModel?.resetPassword(context)
                      onNavToLoginPage.invoke()},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Reset Password")
        }

    }

    if (loginUiState?.value?.isLoading == true) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmailVerificationScreen(onNavToHomePage: () -> Unit) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please verify your email",
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            auth.currentUser?.let { user ->
                user.reload().addOnSuccessListener {
                    if (user.isEmailVerified) {
                        // Navigate to the main activity
                        onNavToHomePage.invoke()
                    } else {
                        // Show snackbar or some form of feedback
                    }
                }
            }
        }) {
            Text("Check Verification Status")
        }
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PrevLoginScreen() {
    val navController = rememberNavController()
    ENSIHubTheme {
        LoginScreen(onNavToHomePage = { /*TODO*/ }, onNavToSignUpPage = { /*TODO*/ }, onNavToForgotPasswordPage = { /*TODO*/ })
        }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PrevSignUpScreen() {
    ENSIHubTheme {
        SignUpScreen(onNavToHomePage = { /*TODO*/ }) {

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PrevForgotPasswordScreen() {
    ENSIHubTheme {
        ForgotPasswordScreen(onNavToLoginPage = { /*TODO*/ })
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PrevVerifyEmailScreen() {
    val navController = rememberNavController()
    ENSIHubTheme {
        EmailVerificationScreen(onNavToHomePage = { /*TODO*/ })
    }

}
