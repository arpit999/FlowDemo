package com.hometech.flowdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.hometech.flowdemo.ui.theme.FlowDemoTheme
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainActivity : ComponentActivity() {
    //declare consumer
    private val data: Flow<Int> = produceUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Returns a scope that's cancelled when F is removed from composition
            val coroutineScope = rememberCoroutineScope()
            var users by remember {
                mutableStateOf("")
            }
            var delayCollect by remember {
                mutableStateOf("")
            }
            FlowDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(text = "After 3 Seconds \n$delayCollect")

                        Button(modifier = Modifier, onClick = {
                           coroutineScope.launch(Dispatchers.Main) {
                                data.collect {
                                    Log.d("UI", "Normal Collect: $it")
                                    users = users.plus("User $it").plus("\n")
                                }
                            }

                            coroutineScope.launch(Dispatchers.Main) {
                                // Collect each emission after 3 second and getting all  emissions from start to end
                                delay(3000)
                                data.collect {
                                    Log.d("delayCollect", "Delay Collect: $it")
                                    delayCollect = delayCollect.plus("User $it").plus("\n")
                                }
                            }

                        }) {
                            Text(text = "Fire Flow", color = Color.White)
                        }
                        Text(text = users, color = Color.DarkGray)

                    }
                }
            }
        }
    }
}

/**
 * This is simple example for cold flow. Cold flow never loose data if consumer join in middle of emissions than it will provide emission from start. It is advantage and disadvantage of cold flow.
 * If we would like to show all events to user in UI that it will make sense that we show all the event when user are not in that screen. But if we just need to show latest emission then  we can use Hot flow like Channel or SharedFlow.
 *
 * Here you can see that in UI we are starting second collector after 3 seconds and it is getting all the emissions from start to finish.
 */
fun produceUser() = flow<Int> {
    val list = listOf(1, 2, 3, 4, 5, 6)

    list.forEach {
        delay(1000)
        emit(it)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlowDemoTheme {

    }
}