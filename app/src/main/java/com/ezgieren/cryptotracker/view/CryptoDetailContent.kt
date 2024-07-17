package com.ezgieren.cryptotracker.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ezgieren.cryptotracker.model.CryptoCurrencyItem
import com.ezgieren.cryptotracker.util.Constants
import com.ezgieren.cryptotracker.util.CustomText
import com.ezgieren.cryptotracker.util.VerticalSpacer
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@OptIn(ExperimentalCoilApi::class)
@SuppressLint("DefaultLocale")
@Composable
fun CryptoDetailContent(crypto: CryptoCurrencyItem) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = rememberImagePainter(data = crypto.image),
                contentDescription = crypto.name,
                modifier = Modifier
                    .padding(16.dp)
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )
            CustomText(
                text = crypto.name ?: Constants.UNKNOWN_TEXT,
                fontSize = 24,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            VerticalSpacer(8)
            CustomText(
                text = " ${crypto.marketData?.currentPrice}",
                fontSize = 20,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
            VerticalSpacer(8)

            CustomText(
                text = "${Constants.CHANGE_TEXT}: ${crypto.marketData?.priceChangePercentage24h?.let { String.format("%.2f", it) } ?: "N/A"}%",
                fontSize = 18,
                fontWeight = FontWeight.Normal,
                color = if ((crypto.marketData?.priceChangePercentage24h ?: 0.0) > 0) Color.Green else Color.Red,
                textAlign = TextAlign.Center
            )
            VerticalSpacer(16)
            CryptoPriceChart()
        }
    }
}

@Composable
fun CryptoPriceChart() {
    val prices = listOf(1.0, 1.1, 1.2, 1.15, 1.3, 1.25)
    val entries = prices.mapIndexed { index, price -> Entry(index.toFloat(), price.toFloat()) }

    AndroidView(factory = { context ->
        val chart = LineChart(context)
        val dataSet = LineDataSet(entries, Constants.PRICE_OVER_TIME).apply {
            color = android.graphics.Color.BLUE
            valueTextColor = android.graphics.Color.BLACK
            lineWidth = 2f
        }
        chart.data = LineData(dataSet)
        chart.description = Description().apply {
            text = Constants.PRICE_OVER_TIME
            textSize = 12f
        }
        chart
    }, modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(16.dp))
}