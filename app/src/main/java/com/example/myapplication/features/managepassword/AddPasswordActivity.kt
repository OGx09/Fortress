package com.example.myapplication.features.managepassword

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.features.ui.ThemeBaseActivity
import com.example.myapplication.features.ui.white100

class AddPasswordActivity : ThemeBaseActivity(){


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
        Column {
            Image(painter = painterResource(id = (R.drawable.ic_password_big)),
                contentDescription = null, modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .background(color = Color.White, shape = RoundedCornerShape(bottomEnd = 10.dp,
                        bottomStart = 10.dp))

            )
            Text("Add New Password!",
                fontSize = TextUnit(18F, TextUnitType.Sp),
                color = white100, style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
    }
}