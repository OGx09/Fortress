package com.example.myapplication.features.detials

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.ui.StateCodelabTheme
import javax.inject.Inject

@Composable
fun PasswordDetails(activity: MainActivity, viewModel: MainActivityViewModel){
    StateCodelabTheme(content = {
        Surface(color = MaterialTheme.colors.primaryVariant) {
            MainContent(topBar = {TopCornerBar()}, body = { BodyContent()}, viewModel = viewModel)
        }
    }, activity = activity)
}

@Composable
@SuppressLint("unused expression")
private fun MainContent(topBar : @Composable Composable.() -> Unit, body: @Composable () -> Unit,
                        viewModel: MainActivityViewModel){
    Column(modifier = Modifier.fillMaxSize()) {
        topBar
        body
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
            MaterialTheme.colors.primaryVariant
        )
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