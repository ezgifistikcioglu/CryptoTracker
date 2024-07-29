package com.ezgieren.cryptotracker.dependencyInjection

import com.ezgieren.cryptotracker.repository.CryptoRepository
import com.ezgieren.cryptotracker.service.CoinGeckoApiService
import com.ezgieren.cryptotracker.util.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCryptoRepository(
        api: CoinGeckoApiService
    ) = CryptoRepository(api)

    @Provides
    @Singleton
    fun provideCoinGeckoApiService(): CoinGeckoApiService {
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinGeckoApiService::class.java)
    }
}