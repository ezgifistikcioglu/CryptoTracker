package com.ezgieren.cryptotracker.repository

import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.service.CoinGeckoApiService
import com.ezgieren.cryptotracker.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CryptoRepository @Inject constructor(
    private val apiService: CoinGeckoApiService
) {
    suspend fun getCryptoList(currency: String): Resource<List<CryptoCurrencyItem>> {
        val response = try {
            apiService.getCryptoList(currency)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error("Error fetching crypto list.")
        }
        return Resource.Success(response)
    }

    suspend fun getCryptoDetail(id: String, currency: String): Resource<CryptoCurrencyItem> {
        val response = try {
            apiService.getCryptoDetail(id, currency)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error("Error fetching crypto details.")
        }
        return Resource.Success(response)
    }
}