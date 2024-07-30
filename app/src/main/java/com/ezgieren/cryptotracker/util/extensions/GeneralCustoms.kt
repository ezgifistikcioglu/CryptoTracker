package com.ezgieren.cryptotracker.util.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ezgieren.cryptotracker.ui.theme.BackgroundColor
import com.ezgieren.cryptotracker.ui.theme.OnPrimaryColor
import com.ezgieren.cryptotracker.ui.theme.PrimaryColor
import com.ezgieren.cryptotracker.util.AppConstants

object AppPadding {
    val xSmallPadding = 1.dp
    val smallPadding = 4.dp
    val normalPadding = 8.dp
    val normal2xPadding = 18.dp
    val symmetricPadding = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
}

fun Modifier.paddingNormal() = this.padding(AppPadding.normalPadding)
fun Modifier.paddingSmall() = this.padding(AppPadding.smallPadding)
fun Modifier.paddingXSmall() = this.padding(AppPadding.xSmallPadding)
fun Modifier.paddingNormal2x() = this.padding(AppPadding.normal2xPadding)
fun Modifier.paddingSymmetric() = this.then(AppPadding.symmetricPadding)

fun Double?.toFormattedString(): String =
    this?.let { String.format("%.2f", it) } ?: AppConstants.UNKNOWN_TEXT

fun Map<String, Double>?.getDoubleFormattedValue(key: String): String =
    this?.get(key.lowercase())?.toFormattedString() ?: AppConstants.UNKNOWN_TEXT

fun Map<String, Long>?.getLongFormattedValue(key: String): String =
    this?.get(key.lowercase())?.toString() ?: AppConstants.UNKNOWN_TEXT

fun String.formatWithCurrency(symbol: String): String = "$this $symbol"

@Composable
fun BaseScaffold(
    title: String,
    onBackClick: (() -> Unit)? = null,
    onHomeClick: (() -> Unit)? = null,
    isPaddingNeeded: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            AppBar(title = title, onBackClick = onBackClick, onHomeClick = onHomeClick)
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .let { if (isPaddingNeeded) it.padding((paddingValues)) else it }
                    .background(BackgroundColor),
                color = BackgroundColor,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                content(paddingValues)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    onHomeClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            CustomText(
                text = title,
                color = OnPrimaryColor,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            onBackClick?.let {
                IconButton(onClick = it) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = AppConstants.BACK_BUTTON,
                        tint = OnPrimaryColor
                    )
                }
            }
        },
        actions = {
            onHomeClick?.let {
                IconButton(onClick = it) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = AppConstants.HOME_BUTTON,
                        tint = OnPrimaryColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryColor,
            titleContentColor = OnPrimaryColor
        )
    )
}

@Composable
fun RetryView(
    error: String, onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()
    ) {
        CustomText(text = error, color = Color.Red)
        VerticalSpacer(10.dp)
        Button(
            onClick = { onRetry() }, modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            CustomText(text = AppConstants.RETRY_BUTTON)
        }
    }
}