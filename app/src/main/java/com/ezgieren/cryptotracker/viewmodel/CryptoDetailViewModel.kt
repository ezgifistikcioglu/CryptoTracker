package com.ezgieren.cryptotracker.viewmodel

import androidx.lifecycle.ViewModel
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.repository.CryptoRepository
import com.ezgieren.cryptotracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CryptoDetailViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

    suspend fun getCrypto(id: String, currency: String): Resource<CryptoCurrencyItem> {
        return repository.getCryptoDetail(id, currency)
    }
}