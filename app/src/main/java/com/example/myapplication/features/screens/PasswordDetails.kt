package com.example.myapplication.features.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.ui.StateCodelabTheme

@Composable
fun PasswordDetails(activity: MainActivity, viewModel: MainActivityViewModel){
    MainContent(viewModel = viewModel)
}

@Composable
@SuppressLint("unused expression")
private fun MainContent(viewModel: MainActivityViewModel){
    Column(modifier = Modifier.fillMaxSize()) {
        TopCornerBar()
        BodyContent()
    }

}

@Composable
private fun BodyContent(){
    Card(elevation = 2.dp) {
        Column(modifier = Modifier.fillMaxWidth()
            .padding(end = 20.dp, start = 20.dp)) {

        }
    }
}

@Composable
private fun TopCornerBar(){
    Box(modifier = Modifier
        .background(
            color =
            MaterialTheme.colors.secondary
        ).fillMaxHeight(fraction = 0.2f)
        .clip(
            RoundedCornerShape(
                0,
                0, 40,
                40
            )
        )){
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier
                .clickable {

                }
                .padding(bottom = 40.dp)) {
                Icon(painter = painterResource(id = android.R.drawable.arrow_down_float), "Go Back")
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = stringResource(R.string.action_back), fontWeight = FontWeight.Bold)
            }

        }
    }
}