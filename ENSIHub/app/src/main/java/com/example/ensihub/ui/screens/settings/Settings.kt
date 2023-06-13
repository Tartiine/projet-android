package com.example.ensihub.ui.screens.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.ensihub.R
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText
import com.google.relay.compose.RelayVector
import com.google.relay.compose.tappable

/**
 * This composable was generated from the UI Package 'settings'.
 * Generated code; do not edit directly
 */
@Composable
fun Settings(
    modifier: Modifier = Modifier,
    myProfileTextContent: String = "",
    termsConditionsTextContent: String = "",
    generalInfoTextContent: String = "",
    contactUsTextContent: String = "",
    outButton: () -> Unit = {},
    profileButton: () -> Unit = {},
    termsButton: () -> Unit = {},
    infoButton: () -> Unit = {},
    contactButton: () -> Unit = {}
) {
    TopLevel(modifier = modifier) {
        Rectangle7()
        Settings(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 122.0.dp,
                    y = 42.0.dp
                )
            )
        )
        SignOut(
            outButton = outButton,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 42.0.dp,
                    y = 584.0.dp
                )
            )
        )
        MyProfile(
            myProfileTextContent = myProfileTextContent,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 42.0.dp,
                    y = 239.0.dp
                )
            )
        )
        TermsConditions(
            termsConditionsTextContent = termsConditionsTextContent,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 42.0.dp,
                    y = 308.0.dp
                )
            )
        )
        GeneralInfo(
            generalInfoTextContent = generalInfoTextContent,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 42.0.dp,
                    y = 377.0.dp
                )
            )
        )
        ContactUs(
            contactUsTextContent = contactUsTextContent,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 42.0.dp,
                    y = 446.0.dp
                )
            )
        )
        ArrowRight(
            profileButton = profileButton,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 357.0.dp,
                    y = 240.0.dp
                )
            )
        ) {
            Vector(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
        ArrowRight1(
            termsButton = termsButton,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 357.0.dp,
                    y = 302.0.dp
                )
            )
        ) {
            Vector1(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
        ArrowRight2(
            infoButton = infoButton,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 357.0.dp,
                    y = 371.0.dp
                )
            )
        ) {
            Vector2(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
        ArrowRight3(
            contactButton = contactButton,
            modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
        ) {
            Vector3(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
        Line3(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 162.0.dp
                )
            )
        )
    }
}

@Preview(widthDp = 428, heightDp = 926)
@Composable
private fun SettingsPreview() {
    MaterialTheme {
        RelayContainer {
            Settings(
                outButton = {},
                myProfileTextContent = "My Profile",
                termsConditionsTextContent = "Terms & conditions",
                generalInfoTextContent = "General Info",
                contactUsTextContent = "Contact Us",
                profileButton = {},
                termsButton = {},
                infoButton = {},
                contactButton = {},
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun Rectangle7(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.settings_rectangle_7),
        modifier = modifier.requiredWidth(428.0.dp).requiredHeight(142.0.dp)
    )
}

@Composable
fun Settings(modifier: Modifier = Modifier) {
    RelayText(
        content = "Settings",
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
fun SignOut(
    outButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = "Sign out",
        fontSize = 25.0.sp,
        color = Color(
            alpha = 255,
            red = 247,
            green = 152,
            blue = 23
        ),
        height = 1.171875.em,
        textAlign = TextAlign.Left,
        maxLines = -1,
        modifier = modifier.tappable(onTap = outButton).requiredWidth(108.0.dp).requiredHeight(31.0.dp)
    )
}

@Composable
fun MyProfile(
    myProfileTextContent: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = myProfileTextContent,
        fontSize = 20.0.sp,
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
fun TermsConditions(
    termsConditionsTextContent: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = termsConditionsTextContent,
        fontSize = 20.0.sp,
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
fun GeneralInfo(
    generalInfoTextContent: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = generalInfoTextContent,
        fontSize = 20.0.sp,
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
fun ContactUs(
    contactUsTextContent: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = contactUsTextContent,
        fontSize = 20.0.sp,
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
fun Vector(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.settings_vector),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 16.8748779296875.dp,
                top = 7.29168701171875.dp,
                end = 15.0001220703125.dp,
                bottom = 7.291648864746094.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun ArrowRight(
    profileButton: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        content = content,
        modifier = modifier.tappable(onTap = profileButton).requiredWidth(45.0.dp).requiredHeight(35.0.dp)
    )
}

@Composable
fun Vector1(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.settings_vector1),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 16.8748779296875.dp,
                top = 7.291683197021484.dp,
                end = 15.0001220703125.dp,
                bottom = 7.291650772094727.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun ArrowRight1(
    termsButton: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        content = content,
        modifier = modifier.tappable(onTap = termsButton).requiredWidth(45.0.dp).requiredHeight(35.0.dp)
    )
}

@Composable
fun Vector2(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.settings_vector2),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 16.8748779296875.dp,
                top = 7.291683197021484.dp,
                end = 15.0001220703125.dp,
                bottom = 7.291648864746094.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun ArrowRight2(
    infoButton: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        content = content,
        modifier = modifier.tappable(onTap = infoButton).requiredWidth(45.0.dp).requiredHeight(35.0.dp)
    )
}

@Composable
fun Vector3(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.settings_vector3),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 16.8748779296875.dp,
                top = 7.29168701171875.dp,
                end = 15.0001220703125.dp,
                bottom = 7.2916412353515625.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun ArrowRight3(
    contactButton: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        content = content,
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 357.0.dp,
                top = 440.0.dp,
                end = 26.0.dp,
                bottom = 451.0.dp
            )
        ).tappable(onTap = contactButton).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun Line3(modifier: Modifier = Modifier) {
    RelayVector(modifier = modifier.requiredWidth(428.0.dp).requiredHeight(0.0.dp))
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
