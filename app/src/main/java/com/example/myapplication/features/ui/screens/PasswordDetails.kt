package com.example.myapplication.features.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.ui.AlertDialogComponent
import com.example.myapplication.features.ui.UiState
import com.example.myapplication.repository.database.PasswordEntity

@Preview
@Composable
@SuppressLint("unused expression")
fun PasswordDetails(activity: MainActivity,viewModel: MainActivityViewModel){
    MainContent(activity)
    
}


@Composable
private fun BodyContent(activity: MainActivity, uiState: UiState<PasswordEntity>) =  Column(modifier = Modifier.padding(start = 25.dp)){

    val data : PasswordEntity? = uiState.data

    val openDialog = remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()

    AlertDialogComponent(openDialog = openDialog)
    
    data?.run{
        Spacer(modifier = Modifier
            .fillMaxHeight(fraction = 0.36f))
        Image(painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = null, modifier = Modifier
                .size(90.dp)
                .border(
                    border = BorderStroke(5.dp, color = MaterialTheme.colors.surface),
                    shape = CircleShape
                )
                .background(color = MaterialTheme.colors.primary, shape = CircleShape))
        
        Text(data.websiteName)
        Text(data.website)
        Text(text = this.secretDataWrapper?.otherInfo ?: "N/A")
    }
}

@Composable
private fun MainContent(activity: MainActivity) = Scaffold() {
    val openPasswordDetails = activity.viewModel.getDetails()
    openPasswordDetails.observe(activity, Observer {
        Log.d("MainContent", "martian: $it}")
    })

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
    
    //BodyContent(activity = activity, uiState = openPasswordDetails.value)
}
