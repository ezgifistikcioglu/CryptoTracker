package com.ezgieren.cryptotracker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.util.BaseScaffold
import com.ezgieren.cryptotracker.util.CustomText
import com.ezgieren.cryptotracker.util.HorizontalSpacer
import com.ezgieren.cryptotracker.util.RetryView
import com.ezgieren.cryptotracker.util.Strings
import com.ezgieren.cryptotracker.util.VerticalSpacer
import com.ezgieren.cryptotracker.viewmodel.CryptoListViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CryptoListScreen(
    navController: NavController,
    viewModel: CryptoListViewModel = hiltViewModel()
) {
    BaseScaffold(
        title = Strings.appTitle,
        onBackClick = null
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                hint = Strings.searchHint,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                viewModel.searchCryptoList(it)
            }
            VerticalSpacer(16)
            CryptoList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var isHintDisplayed by remember { mutableStateOf(hint != "") }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
                }
        )
        if (isHintDisplayed) {
            CustomText(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun CryptoList(
    navController: NavController,
    viewModel: CryptoListViewModel = hiltViewModel()
) {
    val cryptoList by remember { viewModel.cryptoList }
    val errorMessage by remember { viewModel.errorMessage }
    val isLoading by remember { viewModel.isLoading }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else if (errorMessage.isNotEmpty()) {
            RetryView(error = errorMessage) {
                viewModel.fetchCryptoCurrencies()
            }
        } else {
            CryptoListView(cryptos = cryptoList, navController = navController)
        }
    }
}

@Composable
fun CryptoListView(cryptos: List<CryptoCurrencyItem>, navController: NavController) {
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(cryptos) { crypto ->
            CryptoRow(navController = navController, crypto = crypto)
        }
    }
}

@Composable
fun CryptoRow(navController: NavController, crypto: CryptoCurrencyItem) {
    val encodedName = URLEncoder.encode(crypto.name, StandardCharsets.UTF_8.toString())
    val encodedImage = URLEncoder.encode(crypto.image, StandardCharsets.UTF_8.toString())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable {
                navController.navigate(
                    "crypto_detail_screen/${crypto.id}/${crypto.current_price}/$encodedName/$encodedImage"
                )
            }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(crypto.image),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        HorizontalSpacer(10)
        Column {
            CustomText(
                text = crypto.name ?: Strings.unknownText,
                fontSize = 20,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            CustomText(
                text = "${crypto.current_price} ${Strings.usdText}",
                fontSize = 16,
                color = MaterialTheme.colorScheme.onSurface
            )
            CustomText(
                text = "${Strings.changeText}: ${crypto.price_change_percentage_24h}%",
                fontSize = 14,
                color = if ((crypto.price_change_percentage_24h ?: 0.0) > 0) Color.Green else Color.Red
            )
        }
    }
}