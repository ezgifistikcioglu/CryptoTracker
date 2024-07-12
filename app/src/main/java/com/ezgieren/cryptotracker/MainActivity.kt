package com.ezgieren.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ezgieren.cryptotracker.ui.theme.CryptoTrackerTheme
import com.ezgieren.cryptotracker.view.CryptoDetailScreen
import com.ezgieren.cryptotracker.view.CryptoListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "crypto_list_screen") {

                    composable("crypto_list_screen") {
                        // CryptoListScreen
                        CryptoListScreen(navController = navController)
                    }

                    composable(
                        "crypto_detail_screen/{cryptoId}/{cryptoPrice}/{cryptoName}/{cryptoImage}",
                        arguments = listOf(
                            navArgument("cryptoId") {
                                type = NavType.StringType
                            },
                            navArgument("cryptoPrice") {
                                type = NavType.StringType
                            },
                            navArgument("cryptoName") {
                                type = NavType.StringType
                            },
                            navArgument("cryptoImage") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        //CryptoDetailScreen
                        val cryptoId = remember { it.arguments?.getString("cryptoId") }
                        val cryptoPrice = remember { it.arguments?.getString("cryptoPrice") }
                        val cryptoName = remember { it.arguments?.getString("cryptoName") }
                        val cryptoImage = remember { it.arguments?.getString("cryptoImage") }

                        CryptoDetailScreen(
                            id = cryptoId ?: "",
                            price = cryptoPrice ?: "",
                            name = cryptoName ?: "",
                            image = cryptoImage ?: "",
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}