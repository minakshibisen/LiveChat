package com.example.livechat.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.livechat.LCViewModel

enum class State{
    INITIAL,ACTIVE,COMPLETED
}

@Composable
fun SingleStatusScreen(navController: NavController,vm:LCViewModel,userId:String) {
}

@Composable
fun CustomProgressIndicator(modifier: Modifier,status:State,onComplete:()->Unit){
    //val progress = if (state==State .INITIAL)


}