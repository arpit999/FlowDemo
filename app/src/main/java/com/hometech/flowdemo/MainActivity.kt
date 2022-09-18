package com.hometech.flowdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            FlowDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(modifier = Modifier, onClick = {
                            coroutineScope.launch(Dispatchers.Main) {
                                data.collect {
                                    Log.d("UI", "Normal Collect: $it")
                                    users = users.plus("User $it").plus("\n")
                                }
                            }

                        }) {
                            Text(text = "Fire Flow", color = Color.White)
                        }
                        Button(modifier = Modifier.padding(top = 10.dp), onClick = {
                            /**
                             * Flow can be cancelled when coroutine is cancelled. If there is no collector then it will automatically be cancelled.
                             * So here I am using coroutine job object to cancel the flow.
                             * Here I am using coroutineContext to cancel the coroutine children. Because if I use coroutineScope.Cancel() then I can cancel that coroutine but it will not be start again.
                             */
                            coroutineScope.coroutineContext.cancelChildren()
                        }) {
                            Text(text = "Cancel Flow", color = Color.White)
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
 */
fun produceUser() = flow<Int> {
    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)

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