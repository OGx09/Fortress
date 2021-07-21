package com.example.myapplication.features.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.utils.Routes
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.draw.shadow
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.myapplication.features.ui.*


@Composable
fun MainPasswordList(activity: MainActivity,
                     viewModel : MainActivityViewModel,
                     navController: NavHostController) {



    val savePassword : List<PasswordEntity> by viewModel.savePasswordEntityLiveData.observeAsState(
        emptyList()
    )


    Scaffold(
        topBar = {
            Row(modifier = Modifier
                .fillMaxHeight(fraction = 0.1f)
                .fillMaxWidth(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Icon(Icons.Rounded.Menu,
                    contentDescription = "Add password",
                    modifier = Modifier.size(30.dp))
                //.background(Color.White).shadow(elevation = 5.dp)
                PoppedButton()

            }
        },
        modifier = Modifier
            .padding(paddingValues = Paddings.normalAll)
            .background(scaffoldColor),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Routes.ADD_NEW_PASSWORD)}) {
                Icon(painter = painterResource(id = android.R.drawable.ic_input_add), contentDescription = "")
            }
        },
        content = {
            Column(verticalArrangement = Arrangement.Center) {
                Text("Welcome, Gbenga", // To be replaced with data store value later
                    fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
                Text("Your Passwords In One Secure Place",
                    fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(10.dp))
                Spacer(modifier = Modifier.size(15.dp))
                SavePasswordContents(activity, list = savePassword, navController)
            }
        }
    )
}

@Composable
fun PoppedButton() = Card(modifier = Modifier.background(Color.White)
    .shadow(elevation = 5.dp, shape = RoundedCornerShape(5.dp)).size(width = 55.dp,
        height = 30.dp)){
    Icon(
        Icons.Rounded.Search,
        contentDescription = "Search", modifier = Modifier.size(30.dp
        ))
}

@Composable
fun SavePasswordContents(activity: MainActivity, list: List<PasswordEntity>, navController: NavHostController){
    //passwordEntityList

    val lazyState = rememberLazyListState()
    LazyColumn(
        state = lazyState,
        contentPadding = PaddingValues(all = Dp(value = 10f))
    ) {

        items(list) {passwordEntity ->
            //fadeInItemState.setValue()
            SavedPasswordItem(activity, passwordEntity = passwordEntity, navController)
        }
    }
}

@Composable
fun SavedPasswordItem(activity: MainActivity, passwordEntity: PasswordEntity, navController: NavHostController){

    Box(modifier = Modifier.padding(top = 20.dp, bottom = 15.dp)) {
        Card(elevation = 10.dp,
            shape = RoundedCornerShape(15),
            modifier = Modifier.clickable {
                navController.navigate(Routes.PASSWORD_DETAILS)
            }
        ) {

            Row(modifier =
            Modifier
                .fillMaxSize()
                .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box( modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    //.border(2.dp, grey800,CircleShape)
                    .background(color = iconColor)) {
                    Center {
                        Image(painter  = rememberImagePainter(
                            data = passwordEntity.iconBytes,
                            builder = {
                                transformations(CircleCropTransformation())
                            }
                        ), "")

                    }
                }
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(text = passwordEntity.websiteName,
                        fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 5.dp))
                    Text(text = passwordEntity.website, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun Center(content: @Composable ColumnScope.() -> Unit){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content)
}
