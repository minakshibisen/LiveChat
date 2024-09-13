package com.example.livechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.livechat.screens.LoginScreen
import com.example.livechat.ui.theme.LiveChatTheme

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

class MainActivity : ComponentActivity() {
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

    @Composable
    fun ChatAppNavigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = DestinationScreens.SignUp.route ){
            composable(DestinationScreens.SignUp.route){

            }
        }
        LoginScreen()

    }

}
