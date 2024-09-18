package com.example.livechat.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livechat.CommonDivider
import com.example.livechat.CommonImage
import com.example.livechat.CommonProgressBar
import com.example.livechat.LCViewModel

@Composable
fun ProfileScreen(navController: NavController, vm: LCViewModel) {
    val inProgress = vm.inProgress.value
    if (inProgress) {
        CommonProgressBar()
    } else {
        Column {
            //ProfileContent()
            BottomNavMenu(selectedItem = BottomNavMenu.PROFILElIST, navController = navController)

        }
    }


}

@Composable
fun ProfileContent(
    vm: LCViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit
) {

    Column {
        val imageUrl = vm.userData.value?.imageUrl
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Back", Modifier.clickable { })
            Text(text = "Save", Modifier.clickable { })

            CommonDivider()
            ProfileImage(imageUrl = imageUrl, vm = vm)

            CommonDivider()
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), verticalAlignment = Alignment.CenterVertically){

            }
        }
    }

}

@Composable
fun ProfileImage(imageUrl: String?, vm: LCViewModel) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                vm.uploadProfileImage(uri)
            }
        }
    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
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
}