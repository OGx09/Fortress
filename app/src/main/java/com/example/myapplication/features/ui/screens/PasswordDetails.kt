package com.example.myapplication.features.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel

@Composable
fun PasswordDetails(activity: MainActivity, viewModel: MainActivityViewModel){
   Scaffold(topBar = {
       TopAppBar(
           title = {
               Text(text = "Pets Show")
           },
           navigationIcon = {
               IconButton(onClick = {
                   Toast.makeText(activity, "Splash!!", Toast.LENGTH_LONG).show()
               }) {
                   Icon(imageVector = Icons.Filled.ArrowBack,
                       contentDescription = "Back Btn")
               }
           },
           backgroundColor = MaterialTheme.colors.background,
           contentColor = MaterialTheme.colors.primary,
           elevation = 0.dp
       )
   }){
       MainContent(viewModel = viewModel)
   }
}

@Composable
@SuppressLint("unused expression")
private fun MainContent(viewModel: MainActivityViewModel){
    Column(modifier = Modifier.fillMaxSize()) {
        BodyContent()
    }

}

@Composable
private fun BodyContent(){
    Card(elevation = 2.dp) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp, start = 20.dp)) {

        }
    }
}
