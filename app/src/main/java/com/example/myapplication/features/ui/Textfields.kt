package com.example.myapplication.features.ui

import android.text.TextUtils
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun DefaultTextField(textState: MutableState<TextFieldValue>,
                     buttonState: MutableState<Boolean>?,
                     statesToCheck: Array<MutableState<TextFieldValue>>,
                     keyboardOptions: KeyboardOptions,
                     leadingIcon: @Composable () -> Unit,
                     label: @Composable () -> Unit){
    OutlinedTextField(
        singleLine = true,
        value = textState.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onValueChange = {
            textState.value = it
            if (buttonState != null) {
                buttonState.value = isValid(states = statesToCheck)
            }
        },
        label = label,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.primary
        ),
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
    )
}


private fun isValid(vararg states: MutableState<TextFieldValue>): Boolean{
    states.forEach {
        if(TextUtils.isEmpty(it.value.text)){
            return false
        }
    }
    return true;
}