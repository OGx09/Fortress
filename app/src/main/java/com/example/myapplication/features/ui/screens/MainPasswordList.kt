package com.example.myapplication.features.ui.screens

import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.utils.Routes
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.sharp.Add
import androidx.compose.runtime.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.myapplication.features.main.showSnackbar
import com.example.myapplication.features.ui.*
import com.example.myapplication.features.ui.Sizes.titleSize
import kotlinx.coroutines.launch


@Composable
fun MainPasswordList(activity: MainActivity,
                     statusBarState : MutableState<Color?>,
                     navControllerState: NavHostController,
                     scaffoldState : ScaffoldState,
                     currentPageOpacity : MutableState<Float>,
                     openDialog : MutableState<Boolean> = remember { mutableStateOf(false) }) {

    statusBarState.value = MaterialTheme.colors.background

    val viewModel = activity.viewModel
    val savePassword : List <PasswordEntity> by activity.viewModel.savePasswordEntityLiveData.observeAsState(
        emptyList()
    )

    AlertDialogComponent(openDialog = openDialog)

    activity.viewModel.openPasswordDetails.observe(activity){ uiState ->
        when(uiState){
            is UiStateV2.Success ->{
                viewModel.readPasswordDetails(uiState.data)
                navControllerState.navigate(Routes.PASSWORD_DETAILS)
                openDialog.value = false
            }
            is UiStateV2.Failed ->{

               activity.lifecycleScope.launch {
                   scaffoldState.showSnackbar(uiState.exception)
               }
                openDialog.value = false
            }
            is UiStateV2.Loading ->{
                openDialog.value = true
            }
        }
    }



    Scaffold(
        topBar = {
            Row(modifier = Modifier
                .fillMaxHeight(fraction = 0.1f)
                .fillMaxWidth(1f)
                .padding(paddingValues = Paddings.normalAll),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.padding(top = 5.dp, start = 8.dp)) {
                    Icon(Icons.Rounded.Settings,
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
                currentPageOpacity.value = 1f
                navControllerState.navigate(Routes.ADD_NEW_PASSWORD)}) {
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
                    fontWeight = FontWeight.Bold, fontSize = titleSize, modifier = Modifier.padding(10.dp))
                Spaces.Small()
                SavePasswordContents(activity, list = savePassword, navControllerState)
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
fun SavePasswordContents(activity: MainActivity, list: List<PasswordEntity>, navControllerState: NavHostController){

    val lazyState = rememberLazyListState()

    if (list.isEmpty()){
        Center {
            Text("You have not saved any password yet!!")
        }
    }
    LazyColumn(
        state = lazyState,
        contentPadding = PaddingValues(all = Dp(value = 10f))
    ) {

        items(list) {passwordEntity ->
            //fadeInItemState.setValue()
            SavedPasswordItem( activity, passwordEntity = passwordEntity, navControllerState)
        }
    }
}

@Composable
fun SavedPasswordItem(mainActivity: MainActivity, passwordEntity: PasswordEntity, navControllerState: NavHostController){

    Box(modifier = Modifier
        .padding(top = 20.dp, bottom = 15.dp)) {
        Card(elevation = 10.dp,
            shape = RoundedCornerShape(15),
            modifier = Modifier.clickable {
                kotlin.runCatching {
                    val  iv =  Base64.decode(passwordEntity.initializationVector, Base64.NO_WRAP)
                    mainActivity.fingerprintUtil.authenticate(iv, mainActivity){
                        passwordEntity.id?.let { selectedId ->
                            mainActivity.viewModel.openPasswordDetails(selectedId, it.cryptoObject?.cipher)
                        }
                    }
                }.recoverCatching {
                    Log.d("SavedPasswordItem", "error -> ${it.message}")
                }
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
                        if (passwordEntity.website.length >10) {
                            Image(
                                painter = rememberImagePainter(
                                    data = passwordEntity.website,
                                    builder = {
                                        transformations(CircleCropTransformation())
                                    },
                                ), "", modifier = Modifier.size(40.dp)
                            )
                        }else{
                            Text(passwordEntity.websiteName.substring(0, 1));
                        }
                    }
                }
                Column(modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth(0.82f)) {
                    Text(text = passwordEntity.websiteName,
                        fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 5.dp))
                    Text(text = passwordEntity.website, color = Color.Gray)
                }

                Column(modifier = Modifier
                    .size(38.dp)
                    .background(
                        Color.Cyan,
                        shape = RoundedCornerShape(38.dp)
                    )
                    .clickable {
                        //mainActivity.fingerprintUtil.register(activity = mainActivity)
                    },
                    verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Delete password", modifier = Modifier.size(20.dp))
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
