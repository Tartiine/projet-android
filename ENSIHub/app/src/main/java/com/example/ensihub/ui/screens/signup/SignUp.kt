package com.example.ensihub.ui.screens.signup

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
 * This composable was generated from the UI Package 'sign_up'.
 * Generated code; do not edit directly
 */
@Composable
fun SignUp(
    modifier: Modifier = Modifier,
    logoImage: Painter = EmptyPainter(),
    signUpTitle: String = "",
    nicknameText: String = "",
    emailText: String = "",
    confirmText: String = "",
    passwordText: String = "",
    signUpButton: () -> Unit = {}
) {
    TopLevel(modifier = modifier) {
        Image2(
            logoImage = logoImage,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = -11.0.dp,
                    y = 656.0.dp
                )
            )
        )
        Line1(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 646.0.dp
                )
            )
        )
        LoginForm(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 64.0.dp,
                    y = 215.0.dp
                )
            )
        ) {
            Rectangle1(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 1.0.dp,
                        y = 164.0.dp
                    )
                )
            )
            Rectangle5(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = 90.0.dp
                    )
                )
            )
            Rectangle6(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = 21.0.dp
                    )
                )
            )
            Rectangle2(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 1.0.dp,
                        y = 239.0.dp
                    )
                )
            )
            Rectangle3(
                signUpButton = signUpButton,
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 2.0.dp,
                        y = 325.0.dp
                    )
                )
            )
            Nickname(
                nicknameText = nicknameText,
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 2.0.dp,
                        y = 0.0.dp
                    )
                )
            )
            Email(
                emailText = emailText,
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 2.0.dp,
                        y = 69.0.dp
                    )
                )
            )
            ConfirmPassword(
                confirmText = confirmText,
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 1.0.dp,
                        y = 219.0.dp
                    )
                )
            )
            GetStarted(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopCenter,
                    offset = DpOffset(
                        x = -1.0.dp,
                        y = 337.0.dp
                    )
                )
            )
            Password(
                passwordText = passwordText,
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 2.0.dp,
                        y = 143.0.dp
                    )
                )
            )
            Input3(
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
                    x = 73.0.dp,
                    y = 387.0.dp
                )
            )
        ) {
            Text1()
            Cursor1()
        }
        Input1(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 73.0.dp,
                    y = 464.0.dp
                )
            )
        ) {
            Text2()
            Cursor2()
        }
        SignUp(
            signUpTitle = signUpTitle,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 64.0.dp,
                    y = 82.0.dp
                )
            )
        )
        Input2(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 76.0.dp,
                    y = 318.0.dp
                )
            )
        ) {
            Text3()
            Cursor3()
        }
    }
}

@Preview(widthDp = 428, heightDp = 926)
@Composable
private fun SignUpPreview() {
    MaterialTheme {
        RelayContainer {
            SignUp(
                logoImage = painterResource(R.drawable.sign_up_image_2),
                signUpTitle = "Sign Up",
                signUpButton = {},
                nicknameText = "Nickname",
                emailText = "Email",
                confirmText = "Confirm Password",
                passwordText = "Password",
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun Image2(
    logoImage: Painter,
    modifier: Modifier = Modifier
) {
    RelayImage(
        image = logoImage,
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(450.0.dp).requiredHeight(270.0.dp)
    )
}

@Composable
fun Line1(modifier: Modifier = Modifier) {
    RelayVector(modifier = modifier.requiredWidth(428.0.dp).requiredHeight(0.0.dp))
}

@Composable
fun Rectangle1(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.sign_up_rectangle_1),
        modifier = modifier.requiredWidth(297.0.dp).requiredHeight(42.0.dp)
    )
}

@Composable
fun Rectangle5(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.sign_up_rectangle_5),
        modifier = modifier.requiredWidth(297.0.dp).requiredHeight(42.0.dp)
    )
}

@Composable
fun Rectangle6(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.sign_up_rectangle_6),
        modifier = modifier.requiredWidth(297.0.dp).requiredHeight(42.0.dp)
    )
}

@Composable
fun Rectangle2(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.sign_up_rectangle_2),
        modifier = modifier.requiredWidth(297.0.dp).requiredHeight(42.0.dp)
    )
}

@Composable
fun Rectangle3(
    signUpButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    RelayVector(
        vector = painterResource(R.drawable.sign_up_rectangle_3),
        modifier = modifier.tappable(onTap = signUpButton).requiredWidth(297.0.dp).requiredHeight(42.0.dp)
    )
}

@Composable
fun Nickname(
    nicknameText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = nicknameText,
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
fun ConfirmPassword(
    confirmText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = confirmText,
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
fun GetStarted(modifier: Modifier = Modifier) {
    RelayText(
        content = "Get Started",
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
        image = painterResource(R.drawable.sign_up_cursor),
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(9.0.dp).requiredHeight(24.0.dp)
    )
}

@Composable
fun Input3(
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
        modifier = modifier.requiredWidth(299.0.dp).requiredHeight(367.0.dp)
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
        image = painterResource(R.drawable.sign_up_cursor),
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(9.0.dp).requiredHeight(24.0.dp)
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
fun Text2(modifier: Modifier = Modifier) {
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
fun Cursor2(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.sign_up_cursor),
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(9.0.dp).requiredHeight(24.0.dp)
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
fun SignUp(
    signUpTitle: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = signUpTitle,
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
fun Text3(modifier: Modifier = Modifier) {
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
fun Cursor3(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.sign_up_cursor),
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(9.0.dp).requiredHeight(24.000001907348633.dp)
    )
}

@Composable
fun Input2(
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
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
