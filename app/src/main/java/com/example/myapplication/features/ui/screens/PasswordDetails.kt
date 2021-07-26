package com.example.myapplication.features.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.ui.DefaultTopbar


@Preview
@Composable
@SuppressLint("unused expression")
fun PasswordDetails(activity: MainActivity,viewModel: MainActivityViewModel){

    //viewModel.readSavedPassword()

    Image(painter = painterResource(id = R.drawable.data_security_img),
        contentDescription = null, modifier = Modifier
            .fillMaxHeight(fraction = 0.6f)
            .fillMaxWidth(), contentScale = ContentScale.FillHeight
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 120.dp)) {
        Spacer(modifier = Modifier
            .fillMaxHeight(fraction = 0.33f))
        Card(elevation = 6.dp, modifier = Modifier
            .background(
                color = Color.Transparent
            )
            .fillMaxWidth()
            .fillMaxHeight(),
            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        ) {
        }

    }

    Column(modifier = Modifier.padding(start = 25.dp)){
        Spacer(modifier = Modifier
            .fillMaxHeight(fraction = 0.36f))
        Image(painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = null, modifier = Modifier
                .size(110.dp)
                .border(
                    border = BorderStroke(5.dp, color = Color.White),
                    shape = CircleShape
                )
                .background(color = MaterialTheme.colors.background, shape = CircleShape))
    }
}

@Composable
private fun BodyContent(){

}
