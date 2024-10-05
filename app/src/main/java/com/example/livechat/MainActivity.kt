package com.example.livechat

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.livechat.screens.BottomNavMenu
import com.example.livechat.screens.ChatListScreen
import com.example.livechat.screens.LoginScreen
import com.example.livechat.screens.ProfileScreen
import com.example.livechat.screens.SignUpScreen
import com.example.livechat.screens.SingleChatScreen
import com.example.livechat.screens.SingleStatusScreen
import com.example.livechat.screens.StatusScreen
import com.example.livechat.ui.theme.LiveChatTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreens(var route: String) {
    object SignUp : DestinationScreens("signUp")
    object Login : DestinationScreens("login")
    object Profile : DestinationScreens("profile")
    object ChatList : DestinationScreens("chatList")
    object SingleChat : DestinationScreens("singleChat/{chatId}") {
        fun createRoute(id: String) = "singleChat/$id"
    }

    object StatusList : DestinationScreens("StatusList")
    object SingleStatus : DestinationScreens("singleStatus/{userId}") {
        fun createRoute(id: String) = "singleStatus/$id"
    }
}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    fun ChatAppNavigation() {
        val navController = rememberNavController()
        val vm = hiltViewModel<LCViewModel>()

        // Determine which BottomNav item is selected based on the current route
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        val selectedItem = when (currentRoute) {
            DestinationScreens.ChatList.route -> BottomNavMenu.CHATLIST
            DestinationScreens.StatusList.route -> BottomNavMenu.STATUSLIST
            DestinationScreens.Profile.route -> BottomNavMenu.PROFILELIST
            else -> BottomNavMenu.CHATLIST // Default to ChatList
        }

        Scaffold(
            bottomBar = {
                BottomNavMenu(selectedItem = selectedItem, navController = navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = DestinationScreens.ChatList.route,
                modifier = Modifier.padding(innerPadding) // Adjust for bottom bar
            ) {
                composable(DestinationScreens.SignUp.route) {
                    SignUpScreen(vm = vm, navController = navController)
                }
                composable(DestinationScreens.Login.route) {
                    LoginScreen(navController, vm)
                }
                composable(DestinationScreens.ChatList.route) {
                    ChatListScreen(navController, vm)
                }
                composable(DestinationScreens.SingleChat.route) {
                   val chatId = it.arguments?.getString("chatId")
                    chatId?.let { SingleChatScreen(navController = navController,vm=vm,chatId=chatId) }
                }
                composable(DestinationScreens.StatusList.route) {
                    StatusScreen(navController, vm)
                }
                composable(DestinationScreens.SingleStatus.route) {
                    val userId = it.arguments?.getString("userId")
                    userId?.let{
                        SingleStatusScreen(navController=navController,vm=vm, userId = it)

                    }
                }
                composable(DestinationScreens.Profile.route) {
                    ProfileScreen(navController, vm)
                }
            }
        }
    }



}
