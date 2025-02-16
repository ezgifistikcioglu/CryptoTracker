package com.ezgieren.cryptotracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.repository.CryptoRepository
import com.ezgieren.cryptotracker.util.Currency
import com.ezgieren.cryptotracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

    var cryptoList = mutableStateOf<List<CryptoCurrencyItem>>(listOf())
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var selectedCurrency = mutableStateOf(Currency.USD)

    private var initialCryptoList = listOf<CryptoCurrencyItem>()
    private var isSearchStarting = true

    init {
        fetchCryptoCurrencies()
    }

    fun fetchCryptoCurrencies() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getCryptoList(selectedCurrency.value.symbol)
            when (result) {
                is Resource.Success -> {
                    val cryptoItems = result.data ?: emptyList()
                    cryptoList.value = cryptoItems
                    errorMessage.value = ""
                }

                is Resource.Error -> {
                    errorMessage.value = result.message ?: "Unknown error"
                }

                is Resource.Loading -> {
                    errorMessage.value = ""
                }
            }
            isLoading.value = false
        }
    }

    fun setCurrency(currency: Currency) {
        selectedCurrency.value = currency
        fetchCryptoCurrencies()
    }

    fun searchCryptoList(query: String) {
        val listToSearch = if (isSearchStarting) {
            cryptoList.value
        } else {
            initialCryptoList
        }
        viewModelScope.launch {
            if (query.isEmpty()) {
                cryptoList.value = initialCryptoList
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.name?.contains(query.trim(), ignoreCase = true) == true
            }
            if (isSearchStarting) {
                initialCryptoList = cryptoList.value
                isSearchStarting = false
            }
            cryptoList.value = results
        }
    }
}