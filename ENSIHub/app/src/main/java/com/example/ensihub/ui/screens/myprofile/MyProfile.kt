package com.example.ensihub.ui.screens.myprofile

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
 * This composable was generated from the UI Package 'my_profile'.
 * Generated code; do not edit directly
 */
@Composable
fun MyProfile(
    modifier: Modifier = Modifier,
    profImage: Painter = EmptyPainter(),
    profText: String = "",
    nicknameText: String = "",
    otherText: String = "",
    bioText: String = "",
    saveButton: () -> Unit = {}
) {
    TopLevel(modifier = modifier) {
        IconeUtilisateurOrange1(
            profImage = profImage,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 134.0.dp,
                    y = 82.0.dp
                )
            )
        )
        Rectangle7(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 349.0.dp
                )
            )
        )
        Rectangle8(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 65.0.dp,
                    y = 421.0.dp
                )
            )
        )
        Rectangle9(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 65.0.dp,
                    y = 509.0.dp
                )
            )
        )
        Rectangle10(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 65.0.dp,
                    y = 596.0.dp
                )
            )
        )
        MyProfile(
            profText = profText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 111.0.dp,
                    y = 242.0.dp
                )
            )
        )
        Nickname(
            nicknameText = nicknameText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 65.0.dp,
                    y = 391.0.dp
                )
            )
        )
        Other(
            otherText = otherText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 65.0.dp,
                    y = 567.0.dp
                )
            )
        )
        Bio(
            bioText = bioText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 65.0.dp,
                    y = 479.0.dp
                )
            )
        )
        Rectangle11(
            saveButton = saveButton,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 65.0.dp,
                    y = 712.0.dp
                )
            )
        )
        Save(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopCenter,
                offset = DpOffset(
                    x = -0.5.dp,
                    y = 724.0.dp
                )
            )
        )
        Line2(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 337.0.dp
                )
            )
        )
        Input(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 74.0.dp,
                    y = 430.0.dp
                )
            )
        ) {
            Text()
            Cursor()
        }
        Input1(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 74.0.dp,
                    y = 518.0.dp
                )
            )
        ) {
            Text1()
            Cursor1()
        }
        Input2(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 74.0.dp,
                    y = 606.0.dp
                )
            )
        ) {
            Text2()
            Cursor2()
        }
    }
}

@Preview(widthDp = 428, heightDp = 926)
@Composable
private fun MyProfilePreview() {
    MaterialTheme {
        RelayContainer {
            MyProfile(
                profImage = painterResource(R.drawable.my_profile_icone_utilisateur_orange_1),
                profText = "My Profile",
                nicknameText = "Nickname",
                otherText = "Other",
                bioText = "Bio",
                saveButton = {},
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun IconeUtilisateurOrange1(
    profImage: Painter,
    modifier: Modifier = Modifier
) {
    RelayImage(
        image = profImage,
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(160.0.dp).requiredHeight(160.0.dp)
    )
}

@Composable
fun Rectangle7(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.my_profile_rectangle_7),
        modifier = modifier.requiredWidth(428.0.dp).requiredHeight(577.0.dp)
    )
}

@Composable
fun Rectangle8(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.my_profile_rectangle_8),
        modifier = modifier.requiredWidth(297.0.dp).requiredHeight(42.0.dp)
    )
}

@Composable
fun Rectangle9(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.my_profile_rectangle_9),
        modifier = modifier.requiredWidth(297.0.dp).requiredHeight(42.0.dp)
    )
}

@Composable
fun Rectangle10(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.my_profile_rectangle_10),
        modifier = modifier.requiredWidth(297.0.dp).requiredHeight(42.0.dp)
    )
}

@Composable
fun MyProfile(
    profText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = profText,
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
fun Other(
    otherText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = otherText,
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
fun Bio(
    bioText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = bioText,
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
fun Rectangle11(
    saveButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    RelayVector(
        vector = painterResource(R.drawable.my_profile_rectangle_11),
        modifier = modifier.tappable(onTap = saveButton).requiredWidth(297.0.dp).requiredHeight(42.0.dp)
    )
}

@Composable
fun Save(modifier: Modifier = Modifier) {
    RelayText(
        content = "Save",
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
fun Line2(modifier: Modifier = Modifier) {
    RelayVector(modifier = modifier.requiredWidth(428.0.dp).requiredHeight(0.0.dp))
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
        image = painterResource(R.drawable.my_profile_cursor),
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
        image = painterResource(R.drawable.my_profile_cursor),
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
        image = painterResource(R.drawable.my_profile_cursor),
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(9.0.dp).requiredHeight(24.0.dp)
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
