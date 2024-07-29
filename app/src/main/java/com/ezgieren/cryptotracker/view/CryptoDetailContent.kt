package com.ezgieren.cryptotracker.view

import android.widget.LinearLayout
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.ui.theme.*
import com.ezgieren.cryptotracker.util.*
import com.ezgieren.cryptotracker.util.extensions.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.*

@Composable
fun CryptoDetailContent(crypto: CryptoCurrencyItem, selectedCurrency: Currency) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        CryptoDetailHeader(crypto)
        VerticalSpacer(16.dp)
        CryptoDetailMainInfo(crypto, selectedCurrency)
        VerticalSpacer(16.dp)
        CryptoPriceChart(crypto)
        VerticalSpacer(16.dp)
        CryptoDetailInfoGrid(crypto, selectedCurrency)
    }
}

@Composable
fun CryptoDetailHeader(crypto: CryptoCurrencyItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.paddingSmall()
    ) {
        Image(
            painter = rememberImagePainter(data = crypto.image),
            contentDescription = crypto.name,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(2.dp, SecondaryColor, CircleShape)
        )
        CustomText(
            text = crypto.name ?: AppConstants.UNKNOWN_TEXT,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CryptoDetailMainInfo(crypto: CryptoCurrencyItem, selectedCurrency: Currency) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .paddingSymmetric()
            .background(PrimaryColor, RoundedCornerShape(10.dp))
            .paddingNormal()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CustomText(
                text = "1 ${crypto.symbol?.uppercase()} = ${crypto.marketData?.currentPrice?.get(selectedCurrency.symbol.lowercase())?.toFormattedString() ?: "null"} ${selectedCurrency.symbol.uppercase()}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OnPrimaryColor,
            )
            CustomText(
                text = "${AppConstants.LEARN_MORE} ${crypto.name}",
                fontSize = 14.sp,
                color = SecondaryColor,
                modifier = Modifier.clickable { /*TODO Navigate to more details */ }
            )
        }
    }
}
@Composable
fun CryptoPriceChart(crypto: CryptoCurrencyItem) {
    val prices = crypto.marketData?.currentPrice?.values?.toList() ?: listOf()
    val volumes = crypto.marketData?.totalVolume?.values?.toList() ?: listOf()
    val priceEntries = prices.mapIndexed { index, price -> Entry(index.toFloat(), price.toFloat()) }
    val volumeEntries = volumes.mapIndexed { index, volume -> Entry(index.toFloat(), volume.toFloat()) }

    AndroidView(
        factory = { context ->
            val chart = LineChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    labelRotationAngle = -45f
                    textColor = PrimaryColor.toArgb()
                    textSize = 12f
                    setDrawGridLines(false)
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(prices.indices.map { "${AppConstants.Day} $it" })
                    setLabelCount(prices.size, true)
                    setDrawAxisLine(true)
                    axisLineColor = PrimaryColor.toArgb()
                    setAxisMinimum(0f)
                    setAxisMaximum(prices.size.toFloat() - 1)
                }

                axisLeft.apply {
                    textColor = PrimaryColor.toArgb()
                    textSize = 12f
                    setDrawGridLines(false)
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return if (value >= 1_000_000) {
                                "${(value / 1_000_000).toInt()}M"
                            } else if (value >= 1_000) {
                                "${(value / 1_000).toInt()}K"
                            } else {
                                value.toInt().toString()
                            }
                        }
                    }
                    setDrawAxisLine(true)
                    axisLineColor = PrimaryColor.toArgb()
                }

                axisRight.isEnabled = false

                description.apply {
                    text = AppConstants.PRICE_AND_VOLUME
                    textColor = Pink40.toArgb()
                    textSize = 10f
                }
                legend.apply {
                    textColor = PrimaryColor.toArgb()
                    textSize = 12f
                }

                data = LineData(
                    LineDataSet(priceEntries, AppConstants.PRICE_OVER_TIME).apply {
                        color = PrimaryColor.toArgb()
                        valueTextColor = TextColor.toArgb()
                        lineWidth = 2f
                        setDrawValues(false)
                        setDrawCircles(true)
                        circleRadius = 4f
                        circleHoleColor = PrimaryColor.toArgb()
                        circleColors = listOf(SecondaryColor.toArgb())
                    },
                    LineDataSet(volumeEntries, AppConstants.VOLUME_OVER_TIME).apply {
                        color = AccentColor.toArgb()
                        valueTextColor = TextColor.toArgb()
                        lineWidth = 2f
                        setDrawValues(false)
                        setDrawCircles(true)
                        circleRadius = 4f
                        circleHoleColor = AccentColor.toArgb()
                        circleColors = listOf(PrimaryColor.toArgb())
                    }
                )

                xAxis.setLabelCount(prices.size, true)
            }
            chart
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    )
}

@Composable
fun CryptoDetailInfoGrid(crypto: CryptoCurrencyItem, selectedCurrency: Currency) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 130.dp),
        contentPadding = PaddingValues(1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .paddingSmall()
    ) {
        item {
            DetailCard(
                label = AppConstants.CURRENT_PRICE,
                value = crypto.marketData?.currentPrice.getDoubleFormattedValue(selectedCurrency.symbol).formatWithCurrency(selectedCurrency.symbol.uppercase())
            )
        }
        item {
            DetailCard(
                label = AppConstants.PRICE_CHANGE_24,
                value = "${crypto.marketData?.priceChangePercentage24h.toFormattedString()}%"
            )
        }
        item {
            DetailCard(
                label = AppConstants.MARKET_CAP,
                value = crypto.marketData?.marketCap.getLongFormattedValue(selectedCurrency.symbol).formatWithCurrency(selectedCurrency.symbol.uppercase())
            )
        }
        item {
            DetailCard(
                label = AppConstants.VOLUME_24H,
                value = crypto.marketData?.totalVolume.getDoubleFormattedValue(selectedCurrency.symbol).formatWithCurrency(selectedCurrency.symbol.uppercase())
            )
        }
        item {
            DetailCard(
                label = AppConstants.CIRCULATING_SUPPLY,
                value = crypto.marketData?.circulatingSupply.toFormattedString()
            )
        }
        item {
            DetailCard(
                label = AppConstants.MAX_SUPPLY,
                value = crypto.marketData?.maxSupply.toFormattedString()
            )
        }
        item {
            DetailCard(
                label = AppConstants.ATH,
                value = crypto.marketData?.ath.getDoubleFormattedValue(selectedCurrency.symbol).formatWithCurrency(selectedCurrency.symbol.uppercase())
            )
        }
        item {
            DetailCard(
                label = AppConstants.ATL,
                value = crypto.marketData?.atl.getDoubleFormattedValue(selectedCurrency.symbol).formatWithCurrency(selectedCurrency.symbol.uppercase())
            )
        }
    }
}

@Composable
fun DetailCard(label: String, value: String) {
    CustomCard(
        modifier = Modifier
            .paddingNormal()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.paddingSymmetric()
        ) {
            CustomText(
                text = label,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryColor
            )
            CustomText(
                text = value,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )
        }
    }
}