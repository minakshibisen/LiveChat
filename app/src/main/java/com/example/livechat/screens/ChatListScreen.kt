package com.example.livechat.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.livechat.CommonProgressBar
import com.example.livechat.LCViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun ChatListScreen(navController: NavController,vm :LCViewModel) {
  val inProgress =vm.inProgress.value
    if (inProgress){
        CommonProgressBar()
    }else{
        Column (modifier = Modifier
            .fillMaxHeight()
            .systemBarsPadding() ){
            ChatScreenContent()
        }
    }

}

@Composable
fun ChatScreenContent() {
}
