package com.example.myapplication.features.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.R
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.ui.DefaultTextField
import com.example.myapplication.utils.FingerprintUtils
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject



//@ExperimentalUnitApi
@ExperimentalCoroutinesApi
@ExperimentalComposeApi
@Composable
fun AddNewPassword(fingerprintUtil: FingerprintUtils, mainActivity: MainActivity, viewModel: MainActivityViewModel, alphaState: Float){


    Crossfade(targetState = alphaState,
        animationSpec = tween(3000)) { alpha ->
        Surface(modifier = Modifier.alpha(alpha)) {
            MainContent(fingerprintUtil = fingerprintUtil, mainActivity = mainActivity, viewModel = viewModel)
        }
    }


    
}

@Composable
private fun MainContent(fingerprintUtil: FingerprintUtils, mainActivity: MainActivity, viewModel: MainActivityViewModel){

    val webTextState = remember { mutableStateOf(TextFieldValue()) }

    val webNameTextState = remember { mutableStateOf(TextFieldValue()) }

    val buzzTextState = remember { mutableStateOf(TextFieldValue()) }

    val usernameState = remember { mutableStateOf(TextFieldValue()) }

    val buttonState = remember{ mutableStateOf(false) }
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
            modifier = Modifier.padding(20.dp),
            fontSize = 18.sp,
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

            fingerprintUtil.register(mainActivity as FragmentActivity)
                .observe(mainActivity){
                    Log.d("DefaultTextField", "T_HIS $it")
                    it.errorString?.apply {
                        Log.d("DefaultTextField", "T_HIS $this")
                    }
                    it.cryptoObject?.cipher?.apply {
                        viewModel.savePassword(webTextState.value.text,
                            webNameTextState.value.text,
                            passwordTextState.value.text,
                            buzzWord = buzzTextState.value.text, "null", this)
                    }
                }}, enabled = buttonState.value,
            modifier = Modifier
                .padding(12.dp)
                .requiredHeight(50.dp)
                .fillMaxWidth()
        ) {
            Text("Save Password", fontSize = 18.sp)
        }
    }
}

val disposable = object : DisposableHandle {
    override fun dispose() {
        TODO("Not yet implemented")
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
