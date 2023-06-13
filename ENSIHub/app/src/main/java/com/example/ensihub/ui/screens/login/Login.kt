package com.example.ensihub.ui.screens.login

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.ensihub.R
import com.google.relay.compose.EmptyPainter
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage
import com.google.relay.compose.RelayText
import com.google.relay.compose.RelayVector
import com.google.relay.compose.tappable

/**
 * This composable was generated from the UI Package 'login'.
 * Generated code; do not edit directly
 */
@Composable
fun Login(
    modifier: Modifier = Modifier,
    onNavToForgotPasswordPage: () -> Unit = {},
    loginText: String = "",
    logoImage: Painter = EmptyPainter(),
    forgotText: String = "",
    emailText: String = "",
    passwordText: String = "",
    newText: String = "",
    forgotButton: () -> Unit = {},
    loginButton: () -> Unit = {},
    signUpButton: () -> Unit = {}
) {
    TopLevel(modifier = modifier) {
        LoginForm(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 66.0.dp,
                    y = 501.0.dp
                )
            )
        ) {
            Rectangle1(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = 19.0.dp
                    )
                )
            )
            Rectangle2(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = 94.0.dp
                    )
                )
            )
            Rectangle4(
                signUpButton = signUpButton,
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = 254.0.dp
                    )
                )
            )
            Email(
                emailText = emailText,
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 1.0.dp,
                        y = 0.0.dp
                    )
                )
            )
            Password(
                passwordText = passwordText,
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = 74.0.dp
                    )
                )
            )
            Login1(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopCenter,
                    offset = DpOffset(
                        x = -0.5.dp,
                        y = 165.0.dp
                    )
                )
            )
            SignUp(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopCenter,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = 266.0.dp
                    )
                )
            )
            NewMember(
                newText = newText,
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = 226.0.dp
                    )
                )
            )
            Input1(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 9.0.dp,
                        y = 28.0.dp
                    )
                )
            ) {
                Text()
                Cursor()
            }
        }
        Input(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 75.0.dp,
                    y = 604.0.dp
                )
            )
        ) {
            Text1()
            Cursor1()
        }
        Login(
            loginText = loginText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 67.0.dp,
                    y = 366.0.dp
                )
            )
        )
        Image1(
            logoImage = logoImage,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = -11.0.dp,
                    y = 0.0.dp
                )
            )
        )
        Line2(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 287.0.dp
                )
            )
        )
        ForgottenPassword(
            forgotButton = forgotButton,
            forgotText = forgotText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 67.0.dp,
                    y = 814.0.dp
                )
            )
        )
    }
}

@Preview(widthDp = 428, heightDp = 926)
@Composable
private fun LoginPreview() {
    MaterialTheme {
        RelayContainer {
            Login(
                loginText = "Login",
                logoImage = painterResource(R.drawable.login_image_1),
                forgotText = "Forgotten Password",
                forgotButton = {},
                loginButton = {},
                signUpButton = {},
                emailText = "Email",
                passwordText = "Password",
                newText = "New member?",
                modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun Rectangle1(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.login_rectangle_1),
        modifier = modifier
            .requiredWidth(297.0.dp)
            .requiredHeight(42.0.dp)
    )
}

@Composable
fun Rectangle2(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.login_rectangle_2),
        modifier = modifier
            .requiredWidth(297.0.dp)
            .requiredHeight(42.0.dp)
    )
}

@Composable
fun Rectangle3(
    loginButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    RelayVector(
        vector = painterResource(R.drawable.login_rectangle_3),
        modifier = modifier
            .tappable(onTap = loginButton)
            .requiredWidth(297.0.dp)
            .requiredHeight(42.0.dp)
    )
}

@Composable
fun Rectangle4(
    signUpButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    RelayVector(
        vector = painterResource(R.drawable.login_rectangle_4),
        modifier = modifier
            .tappable(onTap = signUpButton)
            .requiredWidth(297.0.dp)
            .requiredHeight(42.0.dp)
    )
}

@Composable
fun Email(
    emailText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = emailText,
        fontSize = 12.0.sp,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.171875.em,
        textAlign = TextAlign.Left,
        modifier = modifier
    )
}

@Composable
fun Password(
    passwordText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = passwordText,
        fontSize = 12.0.sp,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.171875.em,
        textAlign = TextAlign.Left,
        modifier = modifier
    )
}

@Composable
fun Login1(modifier: Modifier = Modifier) {
    RelayText(
        content = "Login",
        fontSize = 15.0.sp,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.171875.em,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(700.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun SignUp(modifier: Modifier = Modifier) {
    RelayText(
        content = "Sign up",
        fontSize = 15.0.sp,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.171875.em,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(700.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun NewMember(
    newText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = newText,
        fontSize = 13.0.sp,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.171875.em,
        textAlign = TextAlign.Left,
        modifier = modifier
    )
}

@Composable
fun Text(modifier: Modifier = Modifier) {
    RelayText(
        content = " ",
        fontSize = 18.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 0,
            green = 0,
            blue = 0
        ),
        height = 1.2102272245619032.em,
        letterSpacing = -0.36.sp,
        textAlign = TextAlign.Left,
        modifier = modifier
    )
}

@Composable
fun Cursor(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.login_cursor),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(9.0.dp)
            .requiredHeight(24.0.dp)
    )
}

@Composable
fun Input1(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.Start,
        arrangement = RelayContainerArrangement.Row,
        content = content,
        modifier = modifier
    )
}

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        clipToParent = false,
        content = content,
        modifier = modifier
            .requiredWidth(297.0.dp)
            .requiredHeight(296.0.dp)
    )
}

@Composable
fun Text1(modifier: Modifier = Modifier) {
    RelayText(
        content = " ",
        fontSize = 18.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 0,
            green = 0,
            blue = 0
        ),
        height = 1.2102272245619032.em,
        letterSpacing = -0.36.sp,
        textAlign = TextAlign.Left,
        modifier = modifier
    )
}

@Composable
fun Cursor1(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.login_cursor),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(9.0.dp)
            .requiredHeight(24.0.dp)
    )
}

@Composable
fun Input(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.Start,
        arrangement = RelayContainerArrangement.Row,
        content = content,
        modifier = modifier
    )
}

@Composable
fun Login(
    loginText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = loginText,
        fontSize = 50.0.sp,
        color = Color(
            alpha = 255,
            red = 247,
            green = 152,
            blue = 23
        ),
        height = 1.171875.em,
        textAlign = TextAlign.Left,
        modifier = modifier
    )
}

@Composable
fun Image1(
    logoImage: Painter,
    modifier: Modifier = Modifier
) {
    RelayImage(
        image = logoImage,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(450.0.dp)
            .requiredHeight(270.0.dp)
    )
}

@Composable
fun Line2(modifier: Modifier = Modifier) {
    RelayVector(modifier = modifier
        .requiredWidth(428.0.dp)
        .requiredHeight(0.0.dp))
}

@Composable
fun ForgottenPassword(
    forgotButton: () -> Unit,
    forgotText: String,
    modifier: Modifier = Modifier
){
    
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 54,
            green = 54,
            blue = 54
        ),
        isStructured = false,
        radius = 48.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
