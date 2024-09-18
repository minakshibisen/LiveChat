package com.example.livechat.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.livechat.LCViewModel

@Composable
fun StatusScreen (navController: NavController, vm : LCViewModel){
    BottomNavMenu(selectedItem =BottomNavMenu.STATUSlIST , navController = navController)

    Text(text = "ss")
}