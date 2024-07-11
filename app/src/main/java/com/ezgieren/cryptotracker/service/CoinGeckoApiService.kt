package com.ezgieren.cryptotracker.service

import com.ezgieren.cryptotracker.model.CryptoCurrency
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApiService {
    @GET("coins/markets")
    suspend fun getCryptoList(
        @Query("vs_currency") vsCurrency: String
    ): CryptoCurrency

    @GET("coins/{id}")
    suspend fun getCryptoDetail(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String
    ): CryptoCurrencyItem
}