package com.example.livechat.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.livechat.LCViewModel

@Composable
fun ChatListScreen(navController: NavController,vm :LCViewModel) {
BottomNavMenu(selectedItem =BottomNavMenu.CHATLIST , navController = navController)
}