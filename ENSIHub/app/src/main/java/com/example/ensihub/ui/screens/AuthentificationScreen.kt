package com.example.ensihub.ui.screens

import com.example.ensihub.R
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ensihub.login.LoginViewModel
import com.example.ensihub.ui.theme.ENSIHubTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
            ENSIHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation(loginViewModel = loginViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(loginViewModel: LoginViewModel? = null,
                modifier: Modifier = Modifier,
                onNavToHomePage: () -> Unit,
                onNavToSignUpPage: () -> Unit) {
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.loginError != null
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
            text = "???",
            modifier = Modifier.padding(start = 8.dp),
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp)
        )

        if(isError){
            Text(text = loginUiState?.loginError ?: "unknown error", color = Color.Red)
        }

        TextField(
            value = loginUiState?.userName ?: "",
            onValueChange = {loginViewModel?.onPasswordChange(it)},
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
            isError = isError
        )

        TextField(
            value = loginUiState?.password ?: "",
            onValueChange = { loginViewModel?.onPasswordChange(it)},
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
            isError = isError
        )

        Button(
            onClick = { loginViewModel?.loginUser(context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Login")
        }

        Text(
            text = "New Member?",
            modifier = Modifier.padding(start = 8.dp),
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp)
        )

        Button(
            onClick = { onNavToSignUpPage.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Sign Up")
        }
    }

    if(loginUiState?.isLoading == true){
        CircularProgressIndicator()
    }

    LaunchedEffect(key1 = loginViewModel?.hasUser){
        if(loginViewModel?.hasUser == true){
            onNavToHomePage.invoke()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(loginViewModel: LoginViewModel? = null,
                modifier: Modifier = Modifier,
                onNavToHomePage: () -> Unit,
                onNavToLoginPage: () -> Unit) {
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.signUpError != null
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
            text = "???",
            modifier = Modifier.padding(start = 8.dp),
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp)
        )

        if(isError){
            Text(text = loginUiState?.signUpError ?: "unknown error", color = Color.Red)
        }

        TextField(
            value = loginUiState?.userNameSignUp ?: "",
            onValueChange = {loginViewModel?.onPasswordSignUpChange(it)},
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
            isError = isError
        )

        TextField(
            value = loginUiState?.passwordSignUp ?: "",
            onValueChange = { loginViewModel?.onPasswordSignUpChange(it)},
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
            isError = isError
        )

        TextField(
            value = loginUiState?.confirmPasswordSignUp ?: "",
            onValueChange = { loginViewModel?.onConfirmPasswordSignUpChange(it)},
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
            isError = isError
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
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp)
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

    if(loginUiState?.isLoading == true){
        CircularProgressIndicator()
    }

    LaunchedEffect(key1 = loginViewModel?.hasUser){
        if(loginViewModel?.hasUser == true){
            onNavToHomePage.invoke()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PrevLoginScreen() {
    ENSIHubTheme {
        LoginScreen(onNavToHomePage = { /* TODO */}) {

        }
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
