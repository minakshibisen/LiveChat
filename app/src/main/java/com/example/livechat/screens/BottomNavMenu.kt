package com.example.livechat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livechat.DestinationScreens
import com.example.livechat.R
import com.example.livechat.navigateTo

// Corrected enum names for StatusList and ProfileList
enum class BottomNavMenu(val icon: Int, val navDestination: DestinationScreens) {
    CHATLIST(R.drawable.ic_chat, DestinationScreens.ChatList),
    STATUSLIST(R.drawable.status, DestinationScreens.StatusList), // Fixed typo
    PROFILELIST(R.drawable.baseline_person_24, DestinationScreens.Profile) // Fixed typo
}

@Composable
fun BottomNavMenu(
    selectedItem: BottomNavMenu,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(Color.Red)
    ) {
        // Iterate over all enum values to create nav menu items
        BottomNavMenu.entries.forEach { item ->
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .weight(1f) // Distribute space evenly
                    .clickable {
                        navigateTo(navController, item.navDestination.route)
                    },
                // Apply color filter based on whether the item is selected or not
                colorFilter = if (item == selectedItem)
                    ColorFilter.tint(color = Color.Black) // Highlight selected item
                else
                    ColorFilter.tint(color = Color.Gray) // Non-selected items
            )
        }
    }
}
