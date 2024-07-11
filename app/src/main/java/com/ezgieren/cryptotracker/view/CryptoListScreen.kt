package com.ezgieren.cryptotracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.util.LargeTitleText
import com.ezgieren.cryptotracker.util.MediumTitleText
import com.ezgieren.cryptotracker.util.SmallTitleText
import com.ezgieren.cryptotracker.util.Strings
import com.ezgieren.cryptotracker.util.VerticalSpacer
import com.ezgieren.cryptotracker.viewmodel.CryptoListViewModel

@Composable
fun CryptoListScreen(
    navController: NavController,
    viewModel: CryptoListViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            LargeTitleText(
                text = Strings.APP_TITLE,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                textAlign = TextAlign.Center
            )
            VerticalSpacer(10)
            SearchBar(
                hint = Strings.SEARCH_HINT,
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
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

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
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
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

    CryptoListView(cryptos = cryptoList, navController = navController)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        if (errorMessage.isNotEmpty()) {
            RetryView(error = errorMessage) {
                viewModel.fetchCryptoCurrencies()
            }
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondary)
            .clickable {
                navController.navigate(
                    "crypto_detail_screen/${crypto.id}/${crypto.current_price}/${crypto.name}/${crypto.image}"
                )
            }
    ) {
        MediumTitleText(
            text = crypto.name,
            modifier = Modifier.padding(2.dp),
        )
        SmallTitleText(
            text = "${crypto.current_price} USD",
            modifier = Modifier.padding(2.dp),
        )
    }
}

@Composable
fun RetryView(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 20.sp)
        VerticalSpacer(10)
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = Strings.RETRY)
        }
    }
}