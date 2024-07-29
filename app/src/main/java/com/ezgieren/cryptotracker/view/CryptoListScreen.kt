package com.ezgieren.cryptotracker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.ui.theme.BackgroundColor
import com.ezgieren.cryptotracker.ui.theme.DarkBlue
import com.ezgieren.cryptotracker.ui.theme.Green
import com.ezgieren.cryptotracker.ui.theme.RedErrorDark
import com.ezgieren.cryptotracker.ui.theme.TextColor
import com.ezgieren.cryptotracker.util.AppConstants
import com.ezgieren.cryptotracker.util.Currency
import com.ezgieren.cryptotracker.util.extensions.BaseScaffold
import com.ezgieren.cryptotracker.util.extensions.CustomBox
import com.ezgieren.cryptotracker.util.extensions.CustomCard
import com.ezgieren.cryptotracker.util.extensions.CustomText
import com.ezgieren.cryptotracker.util.extensions.HorizontalSpacer
import com.ezgieren.cryptotracker.util.extensions.RetryView
import com.ezgieren.cryptotracker.util.extensions.VerticalSpacer
import com.ezgieren.cryptotracker.viewmodel.CryptoListViewModel

@Composable
fun CryptoListScreen(
    navController: NavController,
    viewModel: CryptoListViewModel = hiltViewModel()
) {
    BaseScaffold(
        title = AppConstants.APP_TITLE,
        onHomeClick = { navController.navigate("crypto_list_screen") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues.calculateStartPadding(LayoutDirection.Ltr))
                .background(BackgroundColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CurrencyDropdown(viewModel)
                SearchBar(
                    hint = AppConstants.SEARCH_HINT,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.searchCryptoList(it)
                }
            }
            VerticalSpacer(8.dp)
            CryptoList(navController)
        }
    }
}

@Composable
fun CurrencyDropdown(viewModel: CryptoListViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val currencies = Currency.values()
    val selectedCurrency by viewModel.selectedCurrency

    CustomBox(modifier = Modifier.padding(2.dp)) {
        CustomText(
            text = selectedCurrency.name,
            modifier = Modifier
                .clickable { expanded = true }
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .shadow(5.dp, CircleShape)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { CustomText(text = currency.name) },
                    onClick = {
                        viewModel.setCurrency(currency)
                        expanded = false
                    }
                )
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
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .onFocusChanged { isHintDisplayed = it.isFocused != true && text.isEmpty() }
        )
        if (isHintDisplayed) {
            CustomText(
                text = hint,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun CryptoList(navController: NavController) {
    val viewModel: CryptoListViewModel = hiltViewModel()
    val cryptoList by remember { viewModel.cryptoList }
    val errorMessage by remember { viewModel.errorMessage }
    val isLoading by remember { viewModel.isLoading }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            errorMessage.isNotEmpty() -> RetryView(error = errorMessage) { viewModel.fetchCryptoCurrencies() }
            else -> CryptoListView(cryptoList, navController, viewModel)
        }
    }
}

@Composable
fun CryptoListView(
    cryptos: List<CryptoCurrencyItem>,
    navController: NavController,
    viewModel: CryptoListViewModel
) {
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(cryptos) { crypto ->
            CryptoRow(navController, crypto, viewModel)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CryptoRow(
    navController: NavController,
    crypto: CryptoCurrencyItem,
    viewModel: CryptoListViewModel
) {
    val selectedCurrency by viewModel.selectedCurrency
    CustomCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("crypto_detail_screen/${crypto.id}/${selectedCurrency.symbol}")
                }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(crypto.image),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            HorizontalSpacer(10.dp)
            Column {
                CustomText(
                    text = crypto.name ?: "None",
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
                CustomText(
                    text = "${crypto.currentPrice ?: 0.0} ${selectedCurrency.symbol}",
                    color = TextColor
                )
                CustomText(
                    text = "Change: ${
                        crypto.priceChangePercentage24h?.let {
                            String.format(
                                "%.2f",
                                it
                            )
                        } ?: 0.0
                    }%",
                    color = if ((crypto.priceChangePercentage24h
                            ?: 0.0) > 0
                    ) Green else RedErrorDark
                )
            }
        }
    }
}