package com.example.livechat.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livechat.DestinationScreens
import com.example.livechat.R

enum class BottomNavMenu(val icon: Int, val navDestination: DestinationScreens) {
    CHATLIST(R.drawable.ic_chat, DestinationScreens.ChatList),
    STATUSLIST(R.drawable.status, DestinationScreens.StatusList),
    PROFILELIST(R.drawable.baseline_person_24, DestinationScreens.Profile)
}

@Composable
fun BottomNavMenu(
    selectedItem: BottomNavMenu,
    navController: NavController
) {
    BottomNavigation(
        modifier = Modifier.padding(bottom = 20.dp),
        backgroundColor = Color.White,
        contentColor = Color.White
    ) {
        BottomNavMenu.entries.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp) // Set the size to 20dp

                    )
                },
                selected = selectedItem == item,
                onClick = {
                    if (selectedItem != item) {
                        navController.navigate(item.navDestination.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray
            )
        }
    }
}
