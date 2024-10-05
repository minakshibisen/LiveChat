package com.example.livechat.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livechat.CommonDivider
import com.example.livechat.CommonImage
import com.example.livechat.LCViewModel
import com.example.livechat.data.Message

@Composable
fun SingleChatScreen(navController: NavController, vm: LCViewModel, chatId: String) {
    val reply = rememberSaveable { mutableStateOf("") }

    // Action for sending reply
    val onSendReply = {
        vm.onSendReply(chatId, reply.value)
        reply.value = ""
    }
    val chatMessage = vm.chatMessage
    val myUser = vm.userData.value
    val chats = vm.chats.value
    val currentChat = chats.firstOrNull { it?.chatId == chatId }

    LaunchedEffect(key1 = chatId) {
        vm.populateMessages(chatId)
    }

    BackHandler {
        vm.depopulateMessage()
    }

    Column {
        if (currentChat == null) {
            Text(
                text = "Chat not found. Please check if the chat ID is correct.",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            // Log the chat and user details for debugging
            Log.d(
                "SingleChatScreen",
                "Chat found: ${currentChat.chatId}, User1: ${currentChat.user1?.userId}, User2: ${currentChat.user2?.userId}"
            )

            // Get the chatUser based on the current user's id
            val chatUser = if (myUser?.userId == currentChat.user1?.userId) {
                currentChat.user2
            } else {
                currentChat.user1
            }

            // Show the chat header if the chatUser is not n ull
            if (chatUser != null) {
                ChatHeader(
                    name = chatUser.name ?: "Unknown User",
                    imageUrl = chatUser.imageUrl ?: ""
                ) {
                    navController.popBackStack()
                    vm.depopulateMessage()
                }
            } else {
                // Handle null chatUser case gracefully
                Text(text = "Chat user not found", modifier = Modifier.padding(16.dp))
            }
        }

        // Display chat messages
        if (myUser != null) {
            MessageBox(
                modifier = Modifier.weight(1f),
                chatMessage = chatMessage.value,
                currentUserId = myUser.userId ?: ""
            )
        }

        ReplyBox(
            reply = reply.value,
            onReplyChange = { reply.value = it },
            onSendReply = onSendReply
        )
    }
}

@Composable
fun MessageBox(modifier: Modifier, chatMessage: List<Message>, currentUserId: String) {
    LazyColumn(modifier = modifier) {
        items(chatMessage) {

                msg ->
            val alignment = if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start
            val color = if (msg.sendBy == currentUserId) Color(0xFF68C400) else Color(0xFFC0C0C0)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalAlignment = alignment
            ) {
                Text(
                    text = msg.message ?: "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .padding(12.dp),
                    color = androidx.compose.ui.graphics.Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}

@Composable
fun ChatHeader(name: String, imageUrl: String, onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .clickable { onBackClicked.invoke() }
                .padding(8.dp)
        )
        CommonImage(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
        )
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun ReplyBox(reply: String, onReplyChange: (String) -> Unit, onSendReply: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = reply,
                onValueChange = onReplyChange,
                maxLines = 3,
                modifier = Modifier.weight(1f) // Take up available space
            )
            Button(onClick = onSendReply, modifier = Modifier.padding(start = 8.dp)) {
                Text(text = "Send")
            }
        }
    }
}
