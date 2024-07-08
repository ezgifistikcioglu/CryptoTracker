package com.ezgieren.cryptotracker.service

import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApiService {
    @GET("coins/markets")
    suspend fun getCryptoList(
        @Query("vs_currency") vsCurrency: String
    ): Unit
}