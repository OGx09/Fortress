package com.example.myapplication.features.managepassword

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.features.ui.ThemeBaseActivity
import com.example.myapplication.features.ui.white100
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPasswordActivity : ThemeBaseActivity(){

    private val viewModel : AddPaswordActivityViewModel by viewModels()

    companion object{
        fun start(context: Context){
            val starter = Intent(context, AddPasswordActivity::class.java)
            context.startActivity(starter)
        }
    }


    @ExperimentalComposeApi
    @Composable
    override fun MainContent() {
        Content()
    }


    @ExperimentalComposeApi
    @Preview
    @Composable
    fun Content(){
        Column(modifier = Modifier.padding(20.dp)) {
            Image(painter = painterResource(id = (R.drawable.ic_password_big)),
                contentDescription = null, modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp)

            )
            Text("Add New Password!",
                modifier = Modifier.padding(20.dp),
                fontSize = TextUnit(18F, TextUnitType.Sp),
                color = white100, style = TextStyle(fontWeight = FontWeight.Bold)
            )


            val webTextState = remember { mutableStateOf(TextFieldValue()) }

            val webNameTextState = remember { mutableStateOf(TextFieldValue()) }

            val buzzTextState = remember { mutableStateOf(TextFieldValue()) }

            val buttonState = remember{mutableStateOf(false)}
            val passwordTextState = remember { mutableStateOf(TextFieldValue()) }

            TextField(
                value = webTextState.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onValueChange = { webTextState.value =it
                    buttonState.value = isValid(webNameTextState, buzzTextState, webTextState) },
                label = {Text("Website")},
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri),
                leadingIcon = { Icon(painter = painterResource(id = (R.drawable.ic_website)),
                    contentDescription =null )},
            )

            TextField(value = webNameTextState.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onValueChange = { webNameTextState.value =it
                    buttonState.value = isValid(webNameTextState, buzzTextState, webTextState) },
                label = {Text("Website Name")},
                leadingIcon = { Icon(painter =
                painterResource(id = (R.drawable.ic_website)),
                    contentDescription =null )}
            )

            Password(passwordTextState)

            TextField(value = buzzTextState.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onValueChange = {
                    buzzTextState.value =it
                    buttonState.value = isValid(webNameTextState, buzzTextState, webTextState) },
                label = {Text("Buzz Word")},
                leadingIcon = { Icon(painter = painterResource(id = (R.drawable.ic_buzz_msg)),
                    contentDescription =null )}
            )


            Button(onClick = {
                viewModel.savePassword(webTextState.value.text,
                    webNameTextState.value.text,
                    passwordTextState.value.text,
                    buzzWord = buzzTextState.value.text)

            }, enabled = buttonState.value, modifier =
            Modifier
                .padding(15.dp)
                .requiredHeight(50.dp)
                .fillMaxWidth()
            ) {
                Text("Save Password", fontSize = 18.sp)
            }
        }
    }


    private fun isValid(vararg states: MutableState<TextFieldValue>): Boolean{
        states.forEach {
            if(TextUtils.isEmpty(it.value.text)){
                return false
            }
        }
        return true;
    }

    @Composable
    fun Password(passwordTextState: MutableState<TextFieldValue>){
        TextField(value = passwordTextState.value,
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