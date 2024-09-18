package com.example.livechat

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.livechat.screens.ChatListScreen
import com.example.livechat.screens.LoginScreen
import com.example.livechat.screens.SignUpScreen
import com.example.livechat.ui.theme.LiveChatTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreens(var route: String) {
    object SignUp : DestinationScreens("signUp")
    object Login : DestinationScreens("login")
    object Profile : DestinationScreens("profile")
    object ChatList : DestinationScreens("chatList")
    object SingleChat : DestinationScreens("singleChat/{chatId}") {
        fun createRoute(id: String) = "singlechat/$id"
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
        NavHost(navController = navController, startDestination = DestinationScreens.SignUp.route) {
            composable(DestinationScreens.SignUp.route) {
                SignUpScreen(vm = vm, navController = navController)
            }
            composable(DestinationScreens.Login.route) {
                LoginScreen(navController,vm)
            }
            composable(DestinationScreens.ChatList.route) {
                ChatListScreen()
            }
        }


    }

}
