package com.example.myapplication.features.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.data.LoadingState
import com.example.myapplication.data.Result
import com.example.myapplication.features.ui.*
import com.example.myapplication.utils.*
import com.example.myapplication.utils.DragonLog.spit
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.math.acos


//@ExperimentalUnitApi
@ExperimentalCoroutinesApi
@ExperimentalComposeApi
@Composable
fun AddNewPassword(mainActivity: MainActivity,
                   viewModel: MainActivityViewModel,
                   navControllerState: NavController,
                   scaffoldState : ScaffoldState = rememberScaffoldState(),
                   scrollableState : ScrollableState = rememberScrollableState{1f}){

    Scaffold(scaffoldState = scaffoldState, topBar = {
        DefaultTopbar(navControllerState = navControllerState)
    }, modifier = Modifier.scrollable(scrollableState, orientation = Orientation.Vertical)) {
        MainContent(fingerprintUtil = mainActivity.fingerprintUtil,
            mainActivity = mainActivity,
            viewModel = viewModel, scaffoldState = scaffoldState, navControllerState)
    }

}

@Composable
private fun MainContent(fingerprintUtil: FingerprintUtils, mainActivity: MainActivity, viewModel: MainActivityViewModel,
                        scaffoldState: ScaffoldState?,   navControllerState: NavController,  actionCoroutineScope : CoroutineScope = rememberCoroutineScope()){

    val webTextState = remember { mutableStateOf(TextFieldValue()) }
    val webNameTextState = remember { mutableStateOf(TextFieldValue()) }
    val buzzTextState = remember { mutableStateOf(TextFieldValue()) }
    val usernameState = remember { mutableStateOf(TextFieldValue()) }
    val openDialog = remember { mutableStateOf(false) }
    val buttonState = remember{ mutableStateOf(false) }

    val savePasswordToDbState: UiState<out Boolean?>? = viewModel
        .savePasswordDataLiveData.observeAsSingleState(initial = null).value

    AlertDialogComponent(openDialog = openDialog)

    LaunchedEffect(key1 = savePasswordToDbState){
        savePasswordToDbState?.apply {
            if (isLoading){
                openDialog.value = true
            }else{
                openDialog.value = false
                //There was an error
                error?.apply {
                    scaffoldState?.snackbarHostState?.showSnackbar(this)
                }
                data?.apply {
                    navControllerState.popBackStack()
                }
            }
        }
    }

    val passwordTextState = remember { mutableStateOf(TextFieldValue()) }

    val statesToCheck = arrayOf(buzzTextState,
        webNameTextState, passwordTextState, webTextState, usernameState)


    val scrollableState = rememberScrollableState {delta ->
        delta
    }
    
    Column(modifier = Modifier
        .padding(20.dp)
        .scrollable(
            orientation = Orientation.Vertical,
            state = scrollableState
        )
    ) {
        Text("Add New Password!",
            modifier = Modifier.padding(28.dp),
            fontSize = Sizes.titleSize,
            color = MaterialTheme.colors.primary,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.size(30.dp))

        DefaultTextField(
            statesToCheck= statesToCheck,
            textState = webTextState,
            buttonState = buttonState,
            label = { Text("Website") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri),
            leadingIcon = { Icon(painter = painterResource(id = (R.drawable.ic_website)),
                contentDescription =null )
            },
        )

        DefaultTextField(
            textState = webNameTextState,
            statesToCheck= statesToCheck,
            buttonState = buttonState,
            label = { Text("Website Name") },
            leadingIcon = { Icon(painter =
            painterResource(id = (R.drawable.ic_website)),
                contentDescription =null )
            },
            keyboardOptions = KeyboardOptions
                .Default.copy(keyboardType = KeyboardType.Text),
        )

        Password(passwordTextState)

        DefaultTextField(
            textState = usernameState,
            statesToCheck= statesToCheck,
            buttonState = buttonState,
            label = { Text(stringResource(R.string.title_username)) },
            leadingIcon = { Icon(painter = painterResource(id = (R.drawable.ic_buzz_msg)),
                contentDescription =null )
            },
            keyboardOptions = KeyboardOptions
                .Default.copy(keyboardType = KeyboardType.Text)
        )

        DefaultTextField(
            textState = buzzTextState,
            statesToCheck= statesToCheck,
            buttonState = buttonState,
            label = { Text(stringResource(R.string.title_other_info)) },
            leadingIcon = { Icon(painter = painterResource(id = (R.drawable.ic_buzz_msg)),
                contentDescription =null )
            },
            keyboardOptions = KeyboardOptions
                .Default.copy(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.size(30.dp))


        Button(onClick = {
            fingerprintUtil.register(activity = mainActivity, processResult = {
                actionCoroutineScope.launch {

                    it.cryptoObject?.cipher?.run{
                        viewModel.savePassword(webTextState.value.text,
                            webNameTextState.value.text,
                            passwordTextState.value.text,
                            otherInfo = buzzTextState.value.text,
                            username = usernameState.value.text, this)
                    }

                    it.errorString?.run{
                        scaffoldState?.snackbarHostState?.showSnackbar(this@run)
                    }
                }

            })
             }, enabled = buttonState.value,
            modifier = Modifier
                .padding(12.dp)
                .requiredHeight(50.dp)
                .fillMaxWidth()
        ) {
            Text("Save Password", fontSize = 18.sp)
        }
    }

}

@Composable
fun Password(passwordTextState: MutableState<TextFieldValue>){
    OutlinedTextField(value = passwordTextState.value,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onValueChange = { passwordTextState.value =it },
        label = { Text("Password") },
        leadingIcon = { Icon(painter = painterResource(id = (R.drawable.ic_password)),
            contentDescription =null )
        }
    )
}
