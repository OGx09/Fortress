package com.example.myapplication.features.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.utils.Routes
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.sharp.Add
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.fragment.app.FragmentActivity
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.myapplication.features.ui.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.onEach


@Composable
fun MainPasswordList(activity: MainActivity,
                     viewModel : MainActivityViewModel,
                     navController: NavHostController) {

    val savePassword : List<PasswordEntity> by viewModel.savePasswordEntityLiveData.observeAsState(
        emptyList()
    )


//    result.value?.apply{
//
//        this.errorString?.apply {
//            Log.d("SavedPasswordItem", "$this")
//            activity.viewModel.showMessage(this)
//        }
//
//        this.cryptoObject?.cipher?.apply{
//            Log.d("SavedPasswordItems", "$this")
//            navController.navigate(Routes.PASSWORD_DETAILS)
//        }
//    }



    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Row(modifier = Modifier
                .fillMaxHeight(fraction = 0.1f)
                .fillMaxWidth(1f)
                .padding(paddingValues = Paddings.normalAll),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.padding(top = 5.dp, start = 8.dp)) {
                    Icon(Icons.Rounded.Menu,
                        contentDescription = "Add password",
                        modifier = Modifier.size(30.dp))
                }
                PoppedButton(clickable = {
                    //Show search box
                })

            }
        },
        //scaffoldColor
        modifier = Modifier
            .background(brush = Brush.linearGradient(colors = listOf(Color.White, Color.Blue, Color.Yellow))),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Routes.ADD_NEW_PASSWORD)}) {
                Icon(Icons.Sharp.Add, contentDescription = "")
            }
        },
        content = {
            val username = viewModel.openWelcomeOrPasswordMain.observeAsState()
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(paddingValues = Paddings.normalAll)) {
                Spaces.Small()
                Text("Welcome, ${username.value?.data}", // To be replaced with data store value later
                    fontWeight = FontWeight.Bold, fontSize = 14.sp,
                    modifier = Modifier.padding(start = 10.dp,
                        end = 10.dp))
                Text("Your Passwords In One Secure Place",
                    fontWeight = FontWeight.Bold, fontSize = 28.sp, modifier = Modifier.padding(10.dp))
                Spaces.Small()
                SavePasswordContents(scaffoldState = scaffoldState, activity, list = savePassword, navController)
            }
        }
    )
}

@Composable
@Suppress("unused")
fun PoppedButton(clickable: () -> Unit) = Card(modifier = Modifier
    .padding(10.dp)
    .clickable { clickable }
    .shadow(elevation = 5.dp, shape = RoundedCornerShape(5.dp))
    .size(
        width = 55.dp,
        height = 30.dp
    )){
    Icon(
        Icons.Rounded.Search,
        contentDescription = "Search", modifier = Modifier.size(30.dp
        ))
}

@Composable
fun SavePasswordContents(scaffoldState: ScaffoldState, activity: MainActivity, list: List<PasswordEntity>, navController: NavHostController){
    //passwordEntityList

    val lazyState = rememberLazyListState()
    LazyColumn(
        state = lazyState,
        contentPadding = PaddingValues(all = Dp(value = 10f))
    ) {

        items(list) {passwordEntity ->
            //fadeInItemState.setValue()
            SavedPasswordItem(scaffoldState, activity, passwordEntity = passwordEntity, navController)
        }
    }
}

@Composable
fun SavedPasswordItem(scaffoldState: ScaffoldState, mainActivity: MainActivity, passwordEntity: PasswordEntity, navController: NavHostController){

    val fingerPrintFlow = mainActivity.fingerprintUtil._mutableLiveAuthResultChannel
    LaunchedEffect(key1 = fingerPrintFlow){
        fingerPrintFlow.consumeEach {
            Log.d("SavedPasswordItem", "hello : $it")
        it.errorString?.apply {
            Log.d("SavedPasswordItem", "$this")
            mainActivity.viewModel.showMessage(this)
        }

            it.cryptoObject?.cipher?.apply{
            Log.d("SavedPasswordItems", "$this")
            navController.navigate(Routes.PASSWORD_DETAILS)
        }
        }
    }

    Box(modifier = Modifier.padding(top = 20.dp, bottom = 15.dp)) {
        Card(elevation = 10.dp,
            shape = RoundedCornerShape(15),
            modifier = Modifier.clickable {
                mainActivity.fingerprintUtil.register(mainActivity as FragmentActivity)
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
