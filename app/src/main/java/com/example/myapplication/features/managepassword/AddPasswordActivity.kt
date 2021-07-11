package com.example.myapplication.features.managepassword

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.features.ui.DefaultTextField
import com.example.myapplication.features.ui.ThemeBaseActivity
import com.example.myapplication.features.ui.white100
import com.example.myapplication.utils.FingerprintUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddPasswordActivity @Inject constructor() : ThemeBaseActivity() {

    private val viewModel : AddPaswordActivityViewModel by viewModels()

    @Inject lateinit var fingerprintUtil : FingerprintUtils

    companion object{
        fun start(context: Context){
            val starter = Intent(context, AddPasswordActivity::class.java)
            context.startActivity(starter)
        }
    }


    @ExperimentalCoroutinesApi
    @ExperimentalComposeApi
    @Composable
    override fun MainContent() {
        Content()
    }


    @ExperimentalCoroutinesApi
    @ExperimentalComposeApi
    @Preview
    @Composable
    fun Content(){

        val webTextState = remember { mutableStateOf(TextFieldValue()) }

        val webNameTextState = remember { mutableStateOf(TextFieldValue()) }

        val buzzTextState = remember { mutableStateOf(TextFieldValue()) }

        val buttonState = remember{mutableStateOf(false)}
        val passwordTextState = remember { mutableStateOf(TextFieldValue()) }

        val statesToCheck = arrayOf(buzzTextState,
            webNameTextState, passwordTextState, webTextState)

        val scrollableState = rememberScrollableState {delta ->
            delta
        }

        Column(modifier = Modifier
            .padding(20.dp)
            .scrollable(orientation = Orientation.Vertical,
                state = scrollableState)
        ) {
            Text("Add New Password!",
                modifier = Modifier.padding(20.dp),
                fontSize = TextUnit(18F, TextUnitType.Sp),
                color = MaterialTheme.colors.primary,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.size(30.dp))

            DefaultTextField(
                statesToCheck= statesToCheck,
                textState = webTextState,
                buttonState = buttonState,
                label = {Text("Website")},
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri),
                leadingIcon = { Icon(painter = painterResource(id = (R.drawable.ic_website)),
                    contentDescription =null )},
            )

            DefaultTextField(
                textState = webNameTextState,
                statesToCheck= statesToCheck,
                buttonState = buttonState,
                label = {Text("Website Name")},
                leadingIcon = { Icon(painter =
                painterResource(id = (R.drawable.ic_website)),
                    contentDescription =null )},
                keyboardOptions = KeyboardOptions
                    .Default.copy(keyboardType = KeyboardType.Text),
            )

            Password(passwordTextState)

            DefaultTextField(
                textState = buzzTextState,
                statesToCheck= statesToCheck,
                buttonState = buttonState,
                label = {Text("Buzz Word")},
                leadingIcon = { Icon(painter = painterResource(id = (R.drawable.ic_buzz_msg)),
                    contentDescription =null )},
                keyboardOptions = KeyboardOptions
                    .Default.copy(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick = {

                fingerprintUtil.register(this@AddPasswordActivity)
                    .observe(this@AddPasswordActivity){
                        Log.d("DefaultTextField", "T_HIS $it")
                        it.errorString?.apply {
                            Log.d("DefaultTextField", "T_HIS $this")
                        }
                        it.cryptoObject?.cipher?.apply {
                            viewModel.savePassword(webTextState.value.text,
                                webNameTextState.value.text,
                                passwordTextState.value.text,
                                buzzWord = buzzTextState.value.text, this)
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

//                    if (!fingerprintUtils.isCancelled) {
//                        viewModel.savePassword(
//                            webTextState.value.text,
//                            webNameTextState.value.text,
//                            passwordTextState.value.text,
//                            buzzWord = buzzTextState.value.text
//                        )

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
            label = {Text("Password")},
            leadingIcon = { Icon(painter = painterResource(id = (R.drawable.ic_password)),
                contentDescription =null )}
        )
    }


}