package com.ezgieren.cryptotracker.repository

import com.ezgieren.cryptotracker.model.CryptoCurrency
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.service.CoinGeckoApiService
import com.ezgieren.cryptotracker.util.Constants
import com.ezgieren.cryptotracker.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CryptoRepository @Inject constructor(
    private val apiService: CoinGeckoApiService
) {
    suspend fun getCryptoList(): Resource<CryptoCurrency> {
        val response = try {
            apiService.getCryptoList(Constants.VS_CURRENCY)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error("Error fetching crypto list.")
        }
        return Resource.Success(response)
    }

    suspend fun getCrypto(id: String): Resource<CryptoCurrencyItem> {
        val response = try {
            apiService.getCryptoDetail(id, Constants.VS_CURRENCY)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error("Error fetching crypto details.")
        }
        return Resource.Success(response)
    }
}