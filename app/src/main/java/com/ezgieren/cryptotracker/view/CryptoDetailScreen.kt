package com.ezgieren.cryptotracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.util.BaseScaffold
import com.ezgieren.cryptotracker.util.Resource
import com.ezgieren.cryptotracker.util.RetryView
import com.ezgieren.cryptotracker.viewmodel.CryptoDetailViewModel

@Composable
fun CryptoDetailScreen(
    id: String,
    price: String,
    name: String,
    image: String,
    navController: NavController,
    viewModel: CryptoDetailViewModel = hiltViewModel()
) {
    val cryptoCurrencyItem by produceState<Resource<CryptoCurrencyItem>>(initialValue = Resource.Loading()) {
        value = viewModel.getCrypto(id)
    }

    BaseScaffold(
        title = name,
        onBackClick = { navController.popBackStack() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            when (cryptoCurrencyItem) {
                is Resource.Success -> {
                    val selectedCrypto = cryptoCurrencyItem.data!!
                    CryptoDetailContent(selectedCrypto, price, image)
                }

                is Resource.Error -> {
                    RetryView(cryptoCurrencyItem.message!!) {
                        navController.popBackStack()
                    }
                }

                is Resource.Loading -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}