package com.example.myapplication.features.ui.screens

import android.text.style.AlignmentSpan
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceEvenly
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.Result
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.ui.Spaces
import com.example.myapplication.features.ui.UiState
import com.example.myapplication.features.ui.copyWithResult
import com.example.myapplication.utils.Routes
import kotlinx.coroutines.delay


private const val SplashWaitTime: Long = 2000

@Composable
fun MainSplashScreen(navController: NavController, mainActivity: MainActivity) {

    val hasLogedIn = mainActivity.viewModel.openWelcomeOrPasswordMain.observeAsState()

    Surface(color = MaterialTheme.colors.background) {
        val transitionState = remember { MutableTransitionState(SplashState.Shown) }
        val transition = updateTransition(transitionState, label = "splashTransition")
        val splashAlpha by transition.animateFloat(
            transitionSpec = { tween(durationMillis = 100) }, label = "splashAlpha"
        ) {
            if (it == SplashState.Shown) 1f else 0f
        }

        Box {
            SplashScreen(
                modifier = Modifier.alpha(splashAlpha),
                onTimeout = {
                    transitionState.targetState = SplashState.Completed
                    hasLogedIn.value?.apply {
                        data?.apply {
                            navController.popBackStack()
                            navController.navigate(Routes.PASSWORD_MAIN)
                        }
                        error?.apply {
                            navController.popBackStack()
                            navController.navigate(Routes.WELCOME)
                        }

                    }
                }
            )
        }
    }
}


enum class SplashState { Shown, Completed }

@Composable
fun SplashScreen(modifier: Modifier = Modifier, onTimeout: () -> Unit) {

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Adds composition consistency. Use the value when LaunchedEffect is first called
        val currentOnTimeout by rememberUpdatedState(onTimeout)

        LaunchedEffect(Unit) {
            delay(SplashWaitTime)
            currentOnTimeout()
        }

        Center {
            Image(painterResource(id = R.drawable.splash_logo), contentDescription = null,
                modifier = Modifier.size(100.dp))
        }
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(id = R.string.app_name), fontSize = 19.sp,
                fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))

            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(MaterialTheme.colors.primary)){
                    append("Created by: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Devmike01")
                }
            }, fontSize = 13.sp)
            Spaces.Large()
        }
    }
}