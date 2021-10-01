package com.example.myapplication.features.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Preview(showBackground = true)
@Composable
fun NewButton(){
    Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier
            .align(alignment = Alignment.CenterVertically)
            .fillMaxWidth(0.94f)) {
            Image(painter = painterResource(id = R.drawable.ic_baseline_subject_24),
                contentDescription = "", alignment = Alignment.CenterStart)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "John Doe", textAlign = TextAlign.Start, modifier = Modifier.align(alignment = Alignment.CenterVertically))
        }
        Image(painter = painterResource(id = R.drawable.ic_baseline_subject_24),
            contentDescription = "")
    }
}

/*
     Text(text = "John Doe", textAlign = TextAlign.Start,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
        Image(painter = painterResource(id = R.drawable.ic_baseline_subject_24), contentDescription = "",
            alignment = Alignment.End)
 */