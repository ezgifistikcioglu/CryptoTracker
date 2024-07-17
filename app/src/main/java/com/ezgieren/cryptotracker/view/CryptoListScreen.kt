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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.util.BaseScaffold
import com.ezgieren.cryptotracker.util.Constants
import com.ezgieren.cryptotracker.util.Currency
import com.ezgieren.cryptotracker.util.CustomText
import com.ezgieren.cryptotracker.util.HorizontalSpacer
import com.ezgieren.cryptotracker.util.RetryView
import com.ezgieren.cryptotracker.util.VerticalSpacer
import com.ezgieren.cryptotracker.viewmodel.CryptoListViewModel

@Composable
fun CryptoListScreen(
    navController: NavController,
    viewModel: CryptoListViewModel = hiltViewModel()
) {
    BaseScaffold(
        title = Constants.APP_TITLE,
        onBackClick = null
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CurrencyDropdown(viewModel = viewModel)
            SearchBar(
                hint = Constants.SEARCH_HINT,
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
fun CurrencyDropdown(viewModel: CryptoListViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val currencies = Currency.entries
    val selectedCurrency by remember { viewModel.selectedCurrency }

    Box(modifier = Modifier.padding(16.dp)) {
        Text(
            text = selectedCurrency.name,
            modifier = Modifier
                .clickable { expanded = true }
                .background(Color.LightGray)
                .padding(8.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = {
                        CustomText(text =currency.name )
                    },
                    onClick = {
                        viewModel.setCurrency(currency)
                        expanded = false

                    } )
            }
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

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CryptoRow(navController: NavController, crypto: CryptoCurrencyItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable {
                navController.navigate(
                    "crypto_detail_screen/${crypto.id}"
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
                text = crypto.name ?: "None",
                fontSize = 20,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            CustomText(
                text = "${crypto.currentPrice ?: 0.0} ${Constants.USD_TEXT}",
                fontSize = 16,
                color = MaterialTheme.colorScheme.onSurface
            )
            CustomText(
                text = "Change: ${crypto.priceChangePercentage24h ?: 0.0}%",
                fontSize = 14,
                color = if ((crypto.priceChangePercentage24h ?: 0.0) > 0) Color.Green else Color.Red
            )
        }
    }
}