package com.example.myapplication.features.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion.foldIn
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.example.myapplication.features.ui.screens.Center

@Composable
fun AlertDialogComponent(openDialog: MutableState<Boolean>) {

    if (openDialog.value) {
        // AlertDialog is a Composable that is used to show a dialog with some urgent
        // information/detail on it. For example, while logging out, you can show a dialog
        // to the user "Are you sure?". If you click outside of the dialog or the back button,
        // then dialog will disappear. To disable this feature, use empty onCloseRequest
        val dialogWidth = 70.dp
        val dialogHeight = 70.dp

        Dialog(onDismissRequest = {
            //Don't allow manual dismiss
        }) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                val popupWidth =  70.dp
                val popupHeight = 70.dp
                val cornerSize = 16.dp

                Box(
                    Modifier
                        .size(popupWidth, popupHeight)
                        .align(alignment = Alignment.CenterHorizontally)
                        .background(MaterialTheme.colors.primary, RoundedCornerShape(cornerSize))
                ){
                    CircularProgressIndicator(color = MaterialTheme.colors.background,
                        modifier = Modifier.fillMaxWidth().fillMaxHeight()
                            .align(alignment = Alignment.Center).padding(Paddings.normalAll))
                }
            }
        }

    }
}