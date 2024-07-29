package com.ezgieren.cryptotracker.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.ui.theme.PrimaryColor
import com.ezgieren.cryptotracker.util.AppConstants
import com.ezgieren.cryptotracker.util.Currency
import com.ezgieren.cryptotracker.util.Resource
import com.ezgieren.cryptotracker.util.extensions.BaseScaffold
import com.ezgieren.cryptotracker.util.extensions.CustomBox
import com.ezgieren.cryptotracker.util.extensions.RetryView
import com.ezgieren.cryptotracker.viewmodel.CryptoDetailViewModel

@Composable
fun CryptoDetailScreen(
    id: String,
    currency: String,
    navController: NavController,
    viewModel: CryptoDetailViewModel = hiltViewModel()
) {
    val selectedCurrency = Currency.valueOf(currency.uppercase())
    val cryptoCurrencyItem by produceState<Resource<CryptoCurrencyItem>>(initialValue = Resource.Loading()) {
        value = viewModel.getCrypto(id, selectedCurrency.symbol)
    }

    BaseScaffold(
        title = AppConstants.CRYPTO_DETAILS,
        onBackClick = { navController.popBackStack() },
        onHomeClick = { navController.navigate(AppConstants.CRYPTO_LIST_SCREEN) },
        isPaddingNeeded = true
    ) {
        when (cryptoCurrencyItem) {
            is Resource.Success -> {
                val selectedCrypto = cryptoCurrencyItem.data
                selectedCrypto?.let {
                    CryptoDetailContent(it, selectedCurrency)
                }
            }
            is Resource.Error -> {
                RetryView(cryptoCurrencyItem.message!!) {
                    navController.popBackStack()
                }
            }
            is Resource.Loading -> {
                CustomBox(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryColor)
                }
            }
        }
    }
}