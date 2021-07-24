package com.example.myapplication.features.ui.screens

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.ui.Paddings
import com.example.myapplication.features.ui.Spaces
import com.example.myapplication.features.ui.StateCodelabTheme
import com.example.myapplication.features.ui.white100
import com.example.myapplication.utils.Routes
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive

@Composable
fun WelcomePage (activity: MainActivity, viewModel: MainActivityViewModel, navController: NavController){

    val scaffoldState = rememberScaffoldState()
    Scaffold( scaffoldState = scaffoldState) {
        WelcomePageComponents(activity = activity, viewModel, scaffoldState, navController = navController)
    }
}

@Composable
fun WelcomePageComponents(activity: MainActivity, viewModel: MainActivityViewModel, scaffoldState: ScaffoldState, navController: NavController){

    val username: String = viewModel.welcomeUsername.observeAsState("").value
    val buttonState = remember{mutableStateOf(false)}
    val focusManager = LocalFocusManager.current

    //This is now working
    LaunchedEffect(viewModel.openPasswordMain){
        viewModel.openPasswordMain.collect {
            // scaffoldState.snackbarHostState.showSnackbar(it)
//            viewModel.messageState.
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween) {
        BoxWithConstraints(modifier = Modifier
            .fillMaxHeight(0.53f)
            .background(white100)) {
            Image(painter = painterResource(id = R.drawable.data_security_img),
                contentDescription = "", modifier = Modifier.align(Alignment.Center))
        }
        Text(stringResource(R.string.title_welcome_fortress),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.welcome_intro),
            modifier = Modifier.padding(10.dp),
            fontSize = 14.sp, textAlign = TextAlign.Center, color = Color.Gray)
        Spaces.Small()
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            value = username,
            onValueChange =  {
                viewModel.welcome(it)
            },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            label = {Text("Welcome username")},
            singleLine = true
        )
        Spaces.Medium()
        Button(onClick = {
            viewModel.openPasswordMain(username)
                         },
            modifier = Modifier
                .size(height = 60.dp, width = Dp.Infinity)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                ),) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(R.string.action_getting_started))
                Icon(Icons.Sharp.ArrowForward, contentDescription = "Continue")
            }
        }
    }

}