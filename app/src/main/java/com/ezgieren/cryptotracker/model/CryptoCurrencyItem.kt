package com.ezgieren.cryptotracker.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class CryptoCurrencyItem(
    @SerializedName("id") val id: String?,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("image") @JsonAdapter(ImageTypeAdapter::class) val image: String?,
    @SerializedName("market_data") val marketData: MarketData?,
    @SerializedName("ath") val ath: Double?,
    @SerializedName("ath_change_percentage") val athChangePercentage: Double?,
    @SerializedName("ath_date") val athDate: String?,
    @SerializedName("atl") val atl: Double?,
    @SerializedName("atl_change_percentage") val atlChangePercentage: Double?,
    @SerializedName("atl_date") val atlDate: String?,
    @SerializedName("circulating_supply") val circulatingSupply: Double?,
    @SerializedName("current_price") val currentPrice: Double?,
    @SerializedName("fully_diluted_valuation") val fullyDilutedValuation: Long?,
    @SerializedName("high_24h") val high24h: Double?,
    @SerializedName("low_24h") val low24h: Double?,
    @SerializedName("market_cap") val marketCap: Long?,
    @SerializedName("market_cap_change_24h") val marketCapChange24h: Double?,
    @SerializedName("market_cap_change_percentage_24h") val marketCapChangePercentage24h: Double?,
    @SerializedName("market_cap_rank") val marketCapRank: Int?,
    @SerializedName("max_supply") val maxSupply: Double?,
    @SerializedName("price_change_24h") val priceChange24h: Double?,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @SerializedName("roi") val roi: Roi?,
    @SerializedName("total_supply") val totalSupply: Double?,
    @SerializedName("total_volume") val totalVolume: Double?
)

data class MarketData(
    @SerializedName("current_price") val currentPrice: Map<String, Double>?,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @SerializedName("market_cap") val marketCap: Map<String, Long>?,
    @SerializedName("total_volume") val totalVolume: Map<String, Double>?,
    @SerializedName("circulating_supply") val circulatingSupply: Double?,
    @SerializedName("max_supply") val maxSupply: Double?,
    @SerializedName("high_24h") val high24h: Map<String, Double>?,
    @SerializedName("low_24h") val low24h: Map<String, Double>?,
    @SerializedName("ath") val ath: Map<String, Double>?,
    @SerializedName("atl") val atl: Map<String, Double>?
)

class ImageTypeAdapter : JsonDeserializer<String> {
    override fun deserialize(
        json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): String {
        return if (json.isJsonObject) {
            json.asJsonObject.get("large").asString
        } else {
            json.asString
        }
    }
}