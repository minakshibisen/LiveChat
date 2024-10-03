package com.example.livechat.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livechat.CommonDivider
import com.example.livechat.CommonImage
import com.example.livechat.CommonProgressBar
import com.example.livechat.LCViewModel
import androidx.compose.foundation.layout.systemBarsPadding

@Composable
fun ProfileScreen(navController: NavController, vm: LCViewModel) {
    val inProgress = vm.inProgress.value
    if (inProgress) {
        CommonProgressBar()
    } else {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .systemBarsPadding() // Ensuring system bars are respected
        ) {
            ProfileContent(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
                    .background(color = Color.White),
                vm = vm,
                name = vm.userData.value?.name ?: "", // Placeholder for user's name
                number = vm.userData.value?.number ?: "", // Placeholder for user's number
                onNameChange = { },
                onNumberChange = {},
                onBack = { navController.popBackStack() }, // Navigate back
                onSave = {  }, // Handle profile save
                onLogout = {  } // Handle logout action
            )
            // Bottom Navigation Menu at the bottom
           /* BottomNavMenu(
                selectedItem = BottomNavMenu.PROFILELIST,
                navController = navController
            )*/
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier,
    vm: LCViewModel,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Column(modifier = modifier) {
        val imageUrl = vm.userData.value?.imageUrl
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween // Aligned back and save buttons
        ) {
            Text(text = "Back", Modifier.clickable { onBack() })
            Text(text = "Save", Modifier.clickable { onSave() })
        }

        CommonDivider()

        ProfileImage(imageUrl = imageUrl, vm = vm)

        CommonDivider()

        // Name Input Field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(
                value = name,
                onValueChange = onNameChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
        }

        // Number Input Field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Number", modifier = Modifier.width(100.dp))
            TextField(
                value = number,
                onValueChange = onNumberChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
        }

        CommonDivider()

        // Logout Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Logout", modifier = Modifier.clickable { onLogout() })
        }
    }
}

@Composable
fun ProfileImage(imageUrl: String?, vm: LCViewModel) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let { vm.uploadProfileImage(uri) }
        }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                launcher.launch("image/*")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            if (imageUrl != null) {
                CommonImage(data = imageUrl)
            }
            Text(text = "Change profile picture")
        }
        if (vm.inProgress.value) {
            CommonProgressBar()
        }
    }
}
