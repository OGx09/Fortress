package com.example.myapplication.features.ui.screens

import android.annotation.SuppressLint
import android.text.Layout
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.ui.AlertDialogComponent
import com.example.myapplication.features.ui.DefaultTopbar
import com.example.myapplication.features.ui.UiState
import com.example.myapplication.features.ui.greyShade
import com.example.myapplication.repository.database.PasswordEntity
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
@SuppressLint("unused expression")
fun PasswordDetails(activity: MainActivity, navControllerState: NavController, viewModel: MainActivityViewModel){
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.secondary

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = color,
        )

        // setStatusBarsColor() and setNavigationBarsColor() also exist
    }
    MainContent(viewModel)
}


@ExperimentalAnimationApi
@Composable
private fun MainContent(viewModel: MainActivityViewModel) = Scaffold() {

    val passwordDetails = viewModel.passwordDetails.collectAsState(initial = null).value

   passwordDetails?.run {
       Column(
           modifier = Modifier
               .fillMaxWidth()
               .fillMaxHeight()
       ) {
           Card(
               modifier = Modifier
                   .fillMaxHeight(0.4f)
                   .fillMaxWidth(), elevation = 10.dp, shape = RoundedCornerShape(bottomEnd = 100.dp),
               backgroundColor = MaterialTheme.colors.secondary
           ) {
              Center {

                  Row{

                      Card(
                          elevation = 6.dp,
                          modifier = Modifier
                              .background(
                                  color = Color.Transparent
                              )
                              .width(80.dp)
                              .height(80.dp),
                          shape = CircleShape,
                      ) {
                          Center {
                              Text(
                                  website[0].toString(), style = TextStyle(
                                      fontSize = 37.sp,
                                      fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colors.primary
                                  )
                              )
                          }
                      }
                      Box(modifier = Modifier
                          .background(
                              color = MaterialTheme
                                  .colors.primary
                          )
                          .height(100.dp)
                          .width(2.dp))
                      Column(modifier = Modifier.padding(15.dp)) {
                          Text(websiteName, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 21.sp))
                          Text(website, style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp))
                      }
                  }
              }
           }


           LazyColumn(modifier = Modifier
               .fillMaxWidth(0.9f)
               .fillMaxHeight()
               .padding(top = 30.dp)
               .align(alignment = Alignment.CenterHorizontally)) {
               passwordDetails.run {
                   //buildItem(websiteName, "Website Name", R.drawable.ic_baseline_web_24, Color.DarkGray)
                  // buildItem(website, "Website URL", R.drawable.ic_baseline_web_24, Color.LightGray)
                   buildItem(secretDataWrapper?.platformPassword.let { it ?: "N/A" }, "Website Password", R.drawable.ic_baseline_lock_open_24, Color.DarkGray)
                   buildItem(secretDataWrapper?.userName.let { it ?: "N/A" }, "Website Username", R.drawable.ic_baseline_person_24, Color.Gray)
                   buildItem(secretDataWrapper?.otherInfo.let { it ?: "N/A" }, "Other info", R.drawable.ic_baseline_subject_24, Color.LightGray)
               }
           }

       }

   }

}

@ExperimentalAnimationApi
private fun LazyListScope.buildItem(subTitle: String, title: String, iconRes: Int? = null, iconBg: Color){

    item {

            Card(elevation = 10.dp, modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(), shape = RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp)) {
                Row {
                    if (iconRes != null){
                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .background(color = iconBg)) {
                            Icon(painter = painterResource(id = iconRes), contentDescription = "")
                        }}
                    Spacer(modifier = Modifier.width(2.dp))
                    Column(modifier = Modifier.padding(15.dp)) {
                        Text(title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp))
                        Text(subTitle, style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp))
                    }

            }
        }
    }
}
