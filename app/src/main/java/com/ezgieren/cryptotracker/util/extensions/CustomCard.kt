package com.ezgieren.cryptotracker.util.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.ezgieren.cryptotracker.ui.theme.Pink80
import com.ezgieren.cryptotracker.ui.theme.Surface

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Surface, CircleShape)
            .shadow(5.dp, CircleShape),
        shape = RoundedCornerShape(8.dp),
        content = content
    )
}

@Composable
fun ColumnScope.LargeCustomCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(Pink80, CircleShape)
            .shadow(9.dp, CircleShape),
        content = content
    )
}