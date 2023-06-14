package com.example.ensihub.ui.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.ensihub.R
import com.example.ensihub.mainClasses.AuthRepository
import com.example.ensihub.mainClasses.LoginViewModel
import com.example.ensihub.mainClasses.User
import com.example.ensihub.ui.theme.ENSIHubTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController


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
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Black
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Black)
            .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ensihub),
            contentScale = ContentScale.Crop,
            contentDescription = "APPLOGO",
            modifier = Modifier
                .requiredWidth(450.0.dp)
                .requiredHeight(180.0.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Login",
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

        if (isError) {
            Text(text = loginUiState?.value?.loginError ?: "unknown error", color = Color.Red)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = loginUiState?.value?.eMail ?: "",
                onValueChange = { newValue ->
                    if(!newValue.contains(" ")){
                        loginViewModel?.onEmailChange(newValue)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.LightGray
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

            Text(
                text = "@uha.fr",
                color = Color.White,
                fontSize = 16.sp,
            )

        }



        TextField(
            value = loginUiState?.value?.password ?: "",
            onValueChange = { loginViewModel?.onPasswordChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.LightGray
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
                loginViewModel?.loginUser(context)
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
            Text(text = "Login")
        }

        Text(
            text = "New Member?",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontSize = 16.sp, color = Color.White))

        Button(
            onClick = { onNavToSignUpPage.invoke() },
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                Color(
                    alpha = 255,
                    red = 247,
                    green = 152,
                    blue = 23
                )
            )
        ) {
            Text(text = "Sign Up")
        }

        ClickableText(
            text = AnnotatedString("Forgot Password?"),
            onClick = { onNavToForgotPasswordPage.invoke() },
            modifier = Modifier
                .padding(20.dp),
            style = TextStyle(
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                color = Color.White
            )
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
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Black
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Black)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ensihub),
            contentScale = ContentScale.Crop,
            contentDescription = "APPLOGO",
            modifier = Modifier
                .requiredWidth(450.0.dp)
                .requiredHeight(180.0.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Sign Up",
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

        if (isError) {
            Text(text = loginUiState?.value?.signUpError ?: "unknown error", color = Color.Red)
        }
        TextField(
            value = loginUiState?.value?.userName ?: "",
            onValueChange = { newValue ->
                if(!newValue.contains(" ")){
                    loginViewModel?.onUserNameChange(newValue)
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.LightGray

                )
            },
            label = { Text(text = "Enter your username") },
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

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = loginUiState?.value?.eMailSignUp ?: "",
                onValueChange = { newValue ->
                    if(!newValue.contains(" ")){
                        loginViewModel?.onEmailSignUpChange(newValue)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.LightGray
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

            Text(
                text = "@uha.fr",
                color = Color.White,
                fontSize = 16.sp,
            )

        }

        TextField(
            value = loginUiState?.value?.passwordSignUp ?: "",
            onValueChange = { loginViewModel?.onPasswordSignUpChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.LightGray

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

        TextField(
            value = loginUiState?.value?.confirmPasswordSignUp ?: "",
            onValueChange = { loginViewModel?.onConfirmPasswordSignUpChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null ,
                    tint = Color.LightGray
                )
            },
            label = { Text(text = "Confirm your password") },
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
                loginViewModel?.createUser(context)
                loginViewModel?.sendUser(context)
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
            Text(text = "Sign Up")
        }

        Text(
            text = "Already have an Account?",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontSize = 16.sp, color = Color.White)
        )

        Button(
            onClick = { onNavToLoginPage.invoke() },
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
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Black
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Black)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ensihub),
            contentScale = ContentScale.Crop,
            contentDescription = "APPLOGO",
            modifier = Modifier
                .requiredWidth(450.0.dp)
                .requiredHeight(180.0.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Forgot Password",
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

        if (isError) {
            Text(text = loginUiState?.value?.loginError ?: "unknown error", color = Color.Red)
        }

        TextField(
            value = loginUiState?.value?.eMail ?: "",
            onValueChange = { newValue ->
                if(!newValue.contains(" ")){
                    loginViewModel?.onEmailChange(newValue)
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Color.LightGray
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


        Button(
            onClick = { loginViewModel?.resetPassword(context)
                      onNavToLoginPage.invoke()},
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
            Text(text = "Reset Password")
        }

    }

    if (loginUiState?.value?.isLoading == true) {
        CircularProgressIndicator()
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PrevLoginScreen() {
    val navController = rememberNavController()
    ENSIHubTheme {
        LoginScreen(onNavToHomePage = {}, onNavToSignUpPage = {}, onNavToForgotPasswordPage = {})
        }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PrevSignUpScreen() {
    ENSIHubTheme {
        SignUpScreen(onNavToHomePage = {},
            onNavToLoginPage = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PrevForgotPasswordScreen() {
    ENSIHubTheme {
        ForgotPasswordScreen(onNavToLoginPage = {})
    }

}

