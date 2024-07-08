package com.ezgieren.cryptotracker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

@Composable
fun CryptoDetailScreen(id: String, price: String, name: String, image: String, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "ID: $id")
        Text(text = "Price: $price")
        Text(text = "Name: $name")
        Image(
            painter = rememberImagePainter(data = image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}